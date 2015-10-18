package pl.edu.agh.hubert.languages.math

import pl.edu.agh.hubert.datasets.{CSVLoader, LoadedDataSet}
import pl.edu.agh.hubert.engine._
import pl.edu.agh.hubert.groupIntoPairs

import scala.reflect.ClassTag
import scala.util.Random

class CoevolutionWithDifferentiationFitnessFunction(val experiment: Experiment) extends FitnessFunction {

  private val loadedDataSet = CSVLoader.load(experiment.dataSet)
  private val pairings = loadedDataSet.raw.indices.combinations(2).toArray.map(c => (c.seq(0), c.seq(1)))
  private val dataSetSize = loadedDataSet.differentiatedSize

  private val fitnessPredictorSize = 128
  private val fitnessPredictorMutationProbability = 0.10
  private val fitnessPredictorCrossOverProbability = 0.50
  private val fitnessPredictorPopulationSize = 512
  private var fitnessPredictorPopulation = generateFitnessPredictors(fitnessPredictorPopulationSize)

  private val trainersPopulationSize = 128
  private var trainersPopulation = Array.empty[EvaluatedIndividual]

  private var iteration = 0
  private val trainerSelectionInterval = 100

  private val crossOverOperator = new FitnessPredictorCrossOverOperator
  private val mutationOperator = new FitnessPredictorMutationOperator

  override def evaluatePopulation(toEvaluate: Array[Individual]): Array[EvaluatedIndividual] = {
    val mathPopulation = toEvaluate.asInstanceOf[Array[MathIndividual]]

    selectTrainers(mathPopulation)
    evolvePredictors()
    evaluateSolutions(mathPopulation, bestFitnessPredictor())
  }

  private def evaluateSolutions(
                                 mathPopulation: Array[MathIndividual],
                                 fitnessPredictor: FitnessPredictor
                                 ): Array[EvaluatedIndividual] = {
    mathPopulation.map(individual =>
      new EvaluatedIndividual(
        individual,
        evaluateSolutionIndividual(individual, fitnessPredictor.data)
      )
    )
  }

  private def selectTrainers(solutionPopulation: Array[MathIndividual]): Unit = {
    if (iteration % trainerSelectionInterval == 0) {
      val N = solutionPopulation.length

      val newTrainers = solutionPopulation
        .map(solution => (solution, evaluateTrainerVariance(solution, N)))
        .sortBy(_._2)(Ordering[Double].reverse)
        .take(trainersPopulationSize)
        .map(trainer => evaluateTrainer(trainer))

      trainersPopulation = newTrainers
    }

    iteration += 1
  }

  private def evaluateTrainer(trainer: (MathIndividual, Double)): EvaluatedIndividual = {
    EvaluatedIndividual(
      trainer._1,
      evaluateSolutionIndividual(trainer._1, loadedDataSet)
    )
  }

  private def evaluateTrainerVariance(trainer: MathIndividual, N: Int): Double = {

    val evaluations = fitnessPredictorPopulation
      .map(predictor => evaluateSolutionIndividual(trainer, predictor.data))
      .filter(_.isDefined)
      .map(_.get)

    val avg = evaluations.sum / N
    val variance = evaluations.map(e => (e - avg) * (e - avg)).sum / N

    variance
  }

  private def evolvePredictors() = {
    fitnessPredictorPopulation = groupIntoPairs(fitnessPredictorPopulation)
      .map(pair => crossOverOperator.crossOver(pair._1, pair._2))
      .flatMap(pair => Array(pair._1, pair._2))
      .map(predictor => mutationOperator.mutate(predictor))
  }

  private def bestFitnessPredictor(): FitnessPredictor = {
    val evaluatedPredictors = fitnessPredictorPopulation
      .map(predictor => (evaluateFitnessPredictor(predictor), predictor))
      .filter(_._1.isDefined)

    if (evaluatedPredictors.length > 0) {
      val bestFitnessPredictor = evaluatedPredictors.maxBy(_._1)._2
      return bestFitnessPredictor
    }

    fitnessPredictorPopulation(Random.nextInt(fitnessPredictorPopulationSize))
  }

  private def evaluateFitnessPredictor(predictor: FitnessPredictor): Option[Double] = {
    /**
     * Implemented formula:
     * SUM( ABS(Exact_Fitness_of_Trainer(t) - Predicted_Fitness_of_Trainer(t)) ) / size_of_trainers_population
     *
     * Source:
     * Michael D. Schmidt and Hod Lipson: Coevolution of Fitness Predictors,
     * IEEE TRANSACTIONS ON EVOLUTIONARY COMPUTATION, VOL. 12, NO. 6, DECEMBER 2008,
     * p. 738
     */

    val evaluatedTrainers = trainersPopulation.map(trainer =>
      (
        trainer.fitnessValue,
        evaluateSolutionIndividual(trainer.individual.asInstanceOf[MathIndividual], predictor.data)
        )
    ).filter(fitness => fitness._2.isDefined)

    if (evaluatedTrainers.nonEmpty) {
      val N = trainersPopulation.length
      return Some(evaluatedTrainers.map(fitness => Math.abs(fitness._1 - fitness._2.get)).sum / N)
    }

    None
  }

  private def evaluateSolutionIndividual(solutionIndividual: MathIndividual, input: LoadedDataSet): Option[Double] = {

    val N = input.rawSize

    val pairingErrors = pairings.par.map(pairing => {
      val x = pairing._1
      val y = pairing._2

      val dx: LanguageWord = solutionIndividual.differentiatedBy(x)
      val dy: LanguageWord = solutionIndividual.differentiatedBy(y)

      val dx_sym = dx.evaluateInput(input.raw)
      val dy_sym = dy.evaluateInput(input.raw)

      val dx_num = input.differentiated(x)
      val dy_num = input.differentiated(y)

      val filtered = dx_sym.zip(dy_sym).zip(dx_num).zip(dy_num)
        .filter(r => r._1._1._2 > 0 && r._2 > 0)

      if (filtered.length == 0) {
        None
      } else {
        // -N - the same as multiplying by -1 at the beginning
        val pairingError = filtered
          //          .par
          .map(r => (r._1._1._1 / r._1._1._2) - (r._1._2 / r._2))
          .map(x => Math.log(1 + (if (x > 0) x else -x)))
          .sum / -N

        Some(pairingError)
      }
    })
      .seq
      .filter(o => o.isDefined)
      .map(o => o.get)

    if (pairingErrors.nonEmpty) {
      Some(pairingErrors.min)
    } else {
      None
    }
  }

  private def generateFitnessPredictors(howMuchToGenerate: Int): Array[FitnessPredictor] = {
    (1 to howMuchToGenerate).map(_ => generateFitnessPredictor()).toArray
  }

  private def generateFitnessPredictor(): FitnessPredictor = {
    val predictorRows = (1 to fitnessPredictorSize).map(_ => Random.nextInt(dataSetSize)).toArray.sorted

    new FitnessPredictor(predictorRows, loadedDataSet.subset(predictorRows))
  }

  private class FitnessPredictorMutationOperator {

    val random = new Random(System.currentTimeMillis())

    def mutate(parentPredictor: FitnessPredictor): FitnessPredictor = {
      if (random.nextDouble() < fitnessPredictorMutationProbability) {
        val pointToChange = random.nextInt(fitnessPredictorSize)
        val newRow = random.nextInt(dataSetSize)

        parentPredictor.predictorIndices(pointToChange) = newRow

        for (serieIdx <- parentPredictor.data.raw.indices) {
          parentPredictor.data.raw(serieIdx)(pointToChange) = loadedDataSet.raw(serieIdx)(newRow)
        }
      }

      parentPredictor
    }

  }

  private class FitnessPredictorCrossOverOperator {

    val random = new Random(System.currentTimeMillis())

    def crossOver(left: FitnessPredictor, right: FitnessPredictor): (FitnessPredictor, FitnessPredictor) = {
      if (random.nextDouble() < fitnessPredictorCrossOverProbability) {
        val crossOverPoint = random.nextInt(fitnessPredictorSize)

        val (leftIndices, rightIndices) = crossOver(crossOverPoint, left.predictorIndices, right.predictorIndices)

        return (
          new FitnessPredictor(leftIndices, loadedDataSet.subset(leftIndices)),
          new FitnessPredictor(rightIndices, loadedDataSet.subset(rightIndices))
        )
      }

      (left, right)
    }

    private def crossOver[T: ClassTag](
                                        crossOverPoint: Int,
                                        leftSerie: Array[T],
                                        rightSerie: Array[T]
                                        ): (Array[T], Array[T]) = {
      val (leftChildHead, leftChildTail) = leftSerie.splitAt(crossOverPoint)
      val (rightChildHead, rightChildTail) = rightSerie.splitAt(crossOverPoint)

      val leftChildArray = leftChildHead ++ rightChildTail
      val rightChildArray = rightChildHead ++ leftChildTail
      (leftChildArray, rightChildArray)
    }
  }

  private class FitnessPredictor(val predictorIndices: Array[Int], val data: LoadedDataSet) {}

}

