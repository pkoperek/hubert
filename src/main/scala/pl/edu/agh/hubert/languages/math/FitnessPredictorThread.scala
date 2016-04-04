package pl.edu.agh.hubert.languages.math

import java.util.concurrent.atomic.AtomicReference

import org.slf4j.LoggerFactory
import pl.edu.agh.hubert._
import pl.edu.agh.hubert.datasets.LoadedDataSet
import pl.edu.agh.hubert.engine.{EvaluatedIndividual, LanguageWord}

import scala.util.Random

private class FitnessPredictorThread(
                                      val loadedDataSet: LoadedDataSet,
                                      val crossOverOperator: FitnessPredictorCrossOverOperator,
                                      val mutationOperator: FitnessPredictorMutationOperator,
                                      val fitnessPredictorSize: Int
                                      ) extends Runnable {

  private val logger = LoggerFactory.getLogger(getClass)

  private val dataSetSize = loadedDataSet.differencesSize
  private val trainersPopulationSize = 1
  private val mostRecentBestPredictor = new AtomicReference[FitnessPredictor]()
  private val _mostRecentSolutionPopulation = new AtomicReference[Array[MathIndividual]]()
  private val pairings = loadedDataSet.raw.indices.combinations(2).toArray.map(c => (c.seq(0), c.seq(1)))
  private var trainersPopulation: Array[EvaluatedIndividual] = null

  def mostRecentSolutionPopulation(population: Array[MathIndividual]): Unit = {
    _mostRecentSolutionPopulation.set(population)
  }

  private val fitnessPredictorPopulationSize = 512
  private var fitnessPredictorPopulation = generateFitnessPredictors(fitnessPredictorPopulationSize)
  private val executorThread = new Thread(this)
  private var running = false
  private var iteration = 0
  // According to http://creativemachines.cornell.edu/papers/GPTP06_Schmidt.pdf
  // it should be couple hundred, or each time evolution plateaus
  private val newTrainerShouldBeAdded = 100

  mostRecentBestPredictor.set(fitnessPredictorPopulation(0))

  def ensureWorking(): Unit = {
    if (!running) {
      logger.debug("Starting the coevolution thread")

      executorThread.start()
      running = true
    }
  }

  override def run(): Unit = {
    while(running) {
      evolvePredictors(solutionPopulation())
      iteration += 1
    }
  }

  private def solutionPopulation(): Array[MathIndividual] = {
    _mostRecentSolutionPopulation.get()
  }

  private def evolvePredictors(solutionPopulation: Array[MathIndividual]) = {
    logger.debug("Evolving predictors")

    logger.debug("Selecting trainers...")
    selectTrainers(solutionPopulation)
    logger.debug("Selecting trainers finished.")

    // cross-over and mutation of fitness predictors

    logger.debug("Evolution of fitness predictors...")
    val fitnessPredictorChildren = randomPairs(fitnessPredictorPopulation)
      .map(pair => crossOverOperator.crossOver(pair._1, pair._2))
      .flatMap(pair => Array(pair._1, pair._2))
      .map(predictor => mutationOperator.mutate(predictor))
    logger.debug("Evolution of fitness predictors finished.")

    // we want to evaluate both parents and children
    val toEvaluate = fitnessPredictorPopulation ++ fitnessPredictorChildren

    logger.debug("Evaluating fitness predictors...")
    val fitnessPredictorsEvaluated = toEvaluate
      // perform evaluation
      .map(predictor => (evaluateFitnessPredictor(predictor), predictor))
      // ignore those who can't be evaluated
      .filter(_._1.isDefined)
      // get out of Option[Double]
      .map(pair => (pair._1.get, pair._2))
      // choose the ones with smallest error
      .sortBy(_._1)
      // get just the best ones
      .slice(0, fitnessPredictorPopulationSize)

    logger.debug("Evaluating fitness predictors finished")
    fitnessPredictorPopulation = fitnessPredictorsEvaluated.map(_._2) ++ missingPredictors
    logger.debug("Best fitness predictor: " + fitnessPredictorsEvaluated(0))

    mostRecentBestPredictor.set(fitnessPredictorsEvaluated(0)._2)
  }

  private def selectTrainers(solutionPopulation: Array[MathIndividual]) = {
    if (iteration % newTrainerShouldBeAdded == 0) {
      val newTrainers = solutionPopulation
        .map(solution => (solution, evaluateTrainerVariance(solution)))
        // take the trainers with _highest_ variance
        .sortBy(_._2)(Ordering[Double].reverse)
        .take(trainersPopulationSize)
        .map(trainer => evaluateTrainer(trainer._1))

      logger.debug("Best trainer: " + newTrainers(0).fitness.get)

      trainersPopulation = newTrainers
    }
  }

  private def evaluateTrainerVariance(trainer: MathIndividual): Double = {
    val N = fitnessPredictorPopulationSize
    val evaluations = fitnessPredictorPopulation
      .map(predictor => evaluateSolutionIndividual(trainer, predictor.data))
      .filter(_.isDefined)
      .map(_.get)

    val avg = evaluations.sum / N
    val variance = evaluations.map(e => (e - avg) * (e - avg)).sum / N

    variance
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

  private def generateFitnessPredictors(howMuchToGenerate: Int): Array[FitnessPredictor] = {
    (1 to howMuchToGenerate).map(_ => generateFitnessPredictor()).toArray
  }

  private def generateFitnessPredictor(): FitnessPredictor = {
    val predictorRows = (1 to fitnessPredictorSize).map(_ => Random.nextInt(dataSetSize)).toArray.sorted

    new FitnessPredictor(predictorRows, loadedDataSet.subset(predictorRows))
  }

  def bestFitnessPredictor(): FitnessPredictor = {
    mostRecentBestPredictor.get()
  }

  private def evaluateFitnessPredictor(
                                        predictor: FitnessPredictor
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
      val N = evaluatedTrainers.length
      return Some(evaluatedTrainers.map(fitness => Math.abs(fitness._1 - fitness._2.get)).sum / N)
    }

    None
  }

  private def evaluateTrainer(trainer: MathIndividual): EvaluatedIndividual = {
    logger.debug("Evaluating real trainer fitness: " + trainer)
    EvaluatedIndividual(
      trainer,
      evaluateSolutionIndividual(trainer, loadedDataSet)
    )
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

      val dx_num = input.seriesOfDifferences(x)
      val dy_num = input.seriesOfDifferences(y)

      val filtered = dy_sym.zip(dx_sym).zip(dx_num).zip(dy_num)
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

}
