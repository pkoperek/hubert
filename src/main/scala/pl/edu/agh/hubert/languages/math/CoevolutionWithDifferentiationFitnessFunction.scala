package pl.edu.agh.hubert.languages.math

import pl.edu.agh.hubert.datasets.CSVLoader
import pl.edu.agh.hubert.engine._

import scala.collection.mutable.ArrayBuffer
import scala.util.Random

class CoevolutionWithDifferentiationFitnessFunction(val experiment: Experiment) extends FitnessFunction {

  type FitnessPredictor = Input

  private val loadedDataSet = CSVLoader.load(experiment.dataSet)
  private val pairings = loadedDataSet.raw.indices.combinations(2).toArray.map(c => (c.seq(0), c.seq(1)))
  private val dataSetSize = loadedDataSet.size

  private val fitnessPredictorSize = 128
  private val fitnessPredictorMutationProbability = 0.10
  private val fitnessPredictorCrossOverProbability = 0.50
  private var fitnessPredictorPopulation = generateFitnessPredictors(fitnessPredictorPopulationSize)
  private val fitnessPredictorPopulationSize = 512

  private val trainersPopulationSize = 128
  private var trainersPopulation = Array[EvaluatedIndividual]()

  override def evaluatePopulation(toEvaluate: Array[Individual]): Array[EvaluatedIndividual] = {

    evaluateFitnessPredictors(fitnessPredictorPopulation)

    Array[EvaluatedIndividual]()
  }

  private def evaluateFitnessPredictors(predictors: Array[FitnessPredictor]): Array[Option[Double]] = {
    predictors.map(predictor => evaluateFitnessPredictor(predictor))
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
        evaluateSolutionIndividual(trainer.individual.asInstanceOf[MathIndividual], predictor)
        )
    ).filter(fitness => fitness._2.isDefined)

    if (evaluatedTrainers.nonEmpty) {
      val N = trainersPopulation.length
      return Some(evaluatedTrainers.map(fitness => Math.abs(fitness._1 - fitness._2.get)).sum / N)
    }

    None
  }

  private def evaluateSolutionIndividual(solutionIndividual: MathIndividual, input: Input): Option[Double] = {

    val N = dataSetSize

    val pairingErrors = pairings.par.map(pairing => {
      val x = pairing._1
      val y = pairing._2

      val dx: LanguageWord = solutionIndividual.differentiatedBy(x)
      val dy: LanguageWord = solutionIndividual.differentiatedBy(y)

      val dx_sym = dx.evaluateInput(loadedDataSet.raw)
      val dy_sym = dy.evaluateInput(loadedDataSet.raw)

      val dx_num = loadedDataSet.differentiated(x)
      val dy_num = loadedDataSet.differentiated(y)

      val filtered = dx_sym.zip(dy_sym).zip(dx_num).zip(dy_num)
        .filter(r => r._1._1._2 > 0 && r._2 > 0)

      if (filtered.length == 0) {
        None
      } else {
        // -N - the same as multiplyiing by -1 at the beginning
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

  private def generateFitnessPredictors(howMuchToGenerate: Int): Array[Input] = {
    (1 to howMuchToGenerate).map(_ => generateFitnessPredictor()).toArray
  }

  private def generateFitnessPredictor(): Input = {
    val predictorRows: Array[Int] = (1 to fitnessPredictorSize).map(_ => Random.nextInt(dataSetSize)).toArray

    val buffer = ArrayBuffer[Array[Double]]()
    for (serie <- loadedDataSet.raw) {
      val serieBuffer = ArrayBuffer[Double]()

      for (selectedRow <- predictorRows) {
        serieBuffer += serie(selectedRow)
      }

      buffer += serieBuffer.toArray
    }

    buffer.toArray
  }

  private class MutationOperator {

    val random = new Random(System.currentTimeMillis())

    def mutate(fitnessPredictor: FitnessPredictor): FitnessPredictor = {
      if (random.nextDouble() < fitnessPredictorMutationProbability) {
        val pointToChange = random.nextInt(fitnessPredictorSize)
        val newRow = random.nextInt(dataSetSize)

        for (serieIdx <- fitnessPredictor.indices) {
          fitnessPredictor(serieIdx)(pointToChange) = loadedDataSet.raw(serieIdx)(newRow)
        }
      }

      fitnessPredictor
    }

  }

  private class CrossOverOperator {

    val random = new Random(System.currentTimeMillis())

    def crossOver(left: FitnessPredictor, right: FitnessPredictor): (FitnessPredictor, FitnessPredictor) = {
      if (random.nextDouble() < fitnessPredictorCrossOverProbability) {
        val crossOverPoint = random.nextInt(fitnessPredictorSize)

        val leftChild = ArrayBuffer[Array[Double]]()
        val rightChild = ArrayBuffer[Array[Double]]()

        for (serieIdx <- left.indices) {
          val leftSerie = left(serieIdx)
          val rightSerie = right(serieIdx)

          val (leftChildHead, leftChildTail) = leftSerie.splitAt(crossOverPoint)
          val (rightChildHead, rightChildTail) = leftSerie.splitAt(crossOverPoint)

          leftChild += leftChildHead ++ rightChildTail
          rightChild += rightChildHead ++ leftChildTail
        }

        return (leftChild.toArray, rightChild.toArray)
      }

      (left, right)
    }

  }

}

