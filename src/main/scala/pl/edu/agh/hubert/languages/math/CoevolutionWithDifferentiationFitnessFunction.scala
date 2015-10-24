package pl.edu.agh.hubert.languages.math

import org.slf4j.LoggerFactory
import pl.edu.agh.hubert.datasets.{CSVLoader, LoadedDataSet}
import pl.edu.agh.hubert.engine._
import pl.edu.agh.hubert.randomPairs

import scala.reflect.ClassTag
import scala.util.Random

class CoevolutionWithDifferentiationFitnessFunction(val experiment: Experiment) extends FitnessFunction {

  private val logger = LoggerFactory.getLogger(getClass)

  private val loadedDataSet = CSVLoader.load(experiment.dataSet)
  private val pairings = loadedDataSet.raw.indices.combinations(2).toArray.map(c => (c.seq(0), c.seq(1)))
  private val dataSetSize = loadedDataSet.differentiatedSize

  private var bestFitnessPredictor: FitnessPredictor = null
  private val fitnessPredictorSize = 128
  private val fitnessPredictorMutationProbability = 0.10
  private val fitnessPredictorCrossOverProbability = 0.50
  private val fitnessPredictorPopulationSize = 512
  private var fitnessPredictorPopulation = generateFitnessPredictors(fitnessPredictorPopulationSize)

  private val trainersPopulationSize = 16

  private var iteration = 0
  private val predictorEvolutionInterval = 10

  private val crossOverOperator = new FitnessPredictorCrossOverOperator
  private val mutationOperator = new FitnessPredictorMutationOperator

  override def evaluatePopulation(
                                   toEvaluate: Array[(Individual, Individual)]
                                   ): Array[(EvaluatedIndividual, EvaluatedIndividual)] = {

    val mathPopulation = toEvaluate.map(
      pair => (
        pair._1.asInstanceOf[MathIndividual],
        pair._2.asInstanceOf[MathIndividual]
        )
    )

    evolvePredictors(mathPopulation.flatMap(pair => Array(pair._1, pair._2)))

    val evaluatedSolutions = mathPopulation.map(pair =>
      (asEvaluatedIndividual(pair._1), asEvaluatedIndividual(pair._2))
    )

    iteration += 1

    evaluatedSolutions
  }

  override def evaluatePopulation(toEvaluate: Array[Individual]): Array[EvaluatedIndividual] = {
    val mathPopulation = toEvaluate.map(individual => individual.asInstanceOf[MathIndividual])

    evolvePredictors(mathPopulation)

    val evaluatedSolutions = mathPopulation.map(asEvaluatedIndividual)

    iteration += 1

    evaluatedSolutions
  }

  private def asEvaluatedIndividual(individual: MathIndividual): EvaluatedIndividual = {
    new EvaluatedIndividual(
      individual,
      evaluateSolutionIndividual(individual, bestFitnessPredictor.data)
    )
  }

  private def selectTrainers(solutionPopulation: Array[MathIndividual]): Array[EvaluatedIndividual] = {
    val N = solutionPopulation.length

    val newTrainers = solutionPopulation
      .map(solution => (solution, evaluateTrainerVariance(solution, N)))
      // take the trainers with _highest_ variance
      .sortBy(_._2)(Ordering[Double].reverse)
      .take(trainersPopulationSize)
      .map(trainer => evaluateTrainer(trainer))

    newTrainers
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

    logger.debug("Evaluated trainer variance: " + variance)

    variance
  }

  private def evolvePredictors(solutionPopulation: Array[MathIndividual]) = {
    if (iteration % predictorEvolutionInterval == 0) {
      logger.debug("Evolving predictors: " + iteration)

      val trainers = selectTrainers(solutionPopulation)

      // cross-over and mutation of fitness predictors
      val fitnessPredictorChildren = randomPairs(fitnessPredictorPopulation)
        .map(pair => crossOverOperator.crossOver(pair._1, pair._2))
        .flatMap(pair => Array(pair._1, pair._2))
        .map(predictor => mutationOperator.mutate(predictor))

      // we want to evaluate both parents and children
      val toEvaluate = fitnessPredictorPopulation ++ fitnessPredictorChildren

      fitnessPredictorPopulation = toEvaluate
        // perform evaluation
        .map(predictor => (evaluateFitnessPredictor(predictor, trainers), predictor))
        // ignore those who can't be evaluated
        .filter(_._1.isDefined)
        // get out of Option[Double]
        .map(pair => (pair._1.get, pair._2))
        // choose the ones with smallest error
        .sortBy(_._1)
        // drop evaluation metric value
        .map(_._2)
        // get just the best ones
        .slice(0, fitnessPredictorPopulationSize)

      fitnessPredictorPopulation ++= missingPredictors

      bestFitnessPredictor = fitnessPredictorPopulation(0)
    } else {
      logger.debug("Omitting predictors evolution: " + iteration)
    }
  }

  private def missingPredictors: Array[FitnessPredictor] = {
    val missingPredictorsCount: Int = fitnessPredictorPopulationSize - fitnessPredictorPopulation.length

    logger.debug(
      "After evaluation got: " +
        fitnessPredictorPopulation.length +
        " predictors, filling in : " +
        missingPredictorsCount)

    generateFitnessPredictors(missingPredictorsCount)
  }

  private def evaluateFitnessPredictor(
                                        predictor: FitnessPredictor,
                                        trainersPopulation: Array[EvaluatedIndividual]
                                        ): Option[Double] = {
    /**
     * Implemented formula:
     * SUM( ABS(Exact_Fitness_of_Trainer(t) - Predicted_Fitness_of_Trainer(t)) ) / size_of_trainers_population
     *
     * The smaller - the better
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

