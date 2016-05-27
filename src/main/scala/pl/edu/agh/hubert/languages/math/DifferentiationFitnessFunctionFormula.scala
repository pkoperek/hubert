package pl.edu.agh.hubert.languages.math

import org.slf4j.LoggerFactory
import pl.edu.agh.hubert.datasets.LoadedDataSet
import pl.edu.agh.hubert.engine.Input

class DifferentiationFitnessFunctionFormula(val variablesCount: Int) {

  private val logger = LoggerFactory.getLogger(getClass)

  val maxDifferenceToZero = 1e-20f

  private val pairings = (0 to variablesCount - 1).combinations(2).toArray.map(c => (c.seq(0), c.seq(1)))

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
  def evaluateFitnessFormula(individual: MathIndividual, loadedDataSet: LoadedDataSet): Double = {
    val pairingEvaluationMethod = selectPairingEvaluationMethod(individual, loadedDataSet)
    val worstPairingScore = pairings.map(pairingEvaluationMethod).min

    if (logger.isTraceEnabled()) {
      logger.trace("Evaluated: " + individual + " fitness: " + worstPairingScore)
    }

    worstPairingScore
  }

  def selectPairingEvaluationMethod(individual: MathIndividual, loadedDataSet: LoadedDataSet): ((Int, Int)) => Double = {
    if (pairings.length > 1)
      evaluateAsDependentVariables(individual, loadedDataSet)
    else
      evaluateAsIndependentVariables(individual, loadedDataSet)
  }

  private def evaluateAsDependentVariables(individual: MathIndividual, loadedDataSet: LoadedDataSet)(pairing: (Int, Int)): Double = {
    val x = pairing._1
    val y = pairing._2

    val dx_num = loadedDataSet.seriesOfDifferences.serie(x)
    val dy_num = loadedDataSet.seriesOfDifferences.serie(y)

    val dx_by_dy_num = dx_num.zip(dy_num).map(r => r._1 / r._2)
    val dy_by_dx_num = dy_num.zip(dx_num).map(r => r._1 / r._2)

    val dx = individual.differentiatedBy(x, y)
    val dy = individual.differentiatedBy(y, x)

    val dx_sym = dx.evaluateInput(new Input(loadedDataSet.rawAlignedToDifferences.series ++ Array(dy_by_dx_num)))
    val dy_sym = dy.evaluateInput(new Input(loadedDataSet.rawAlignedToDifferences.series ++ Array(dx_by_dy_num)))

    fitnessFormula(dx_num, dy_num, dx_sym, dy_sym)
  }

  private def evaluateAsIndependentVariables(individual: MathIndividual, loadedDataSet: LoadedDataSet)(pairing: (Int, Int)): Double = {
    val x = pairing._1
    val y = pairing._2

    val dx_num = loadedDataSet.seriesOfDifferences.serie(x)
    val dy_num = loadedDataSet.seriesOfDifferences.serie(y)

    val dx = individual.differentiatedBy(x)
    val dy = individual.differentiatedBy(y)

    val dx_sym = dx.evaluateInput(new Input(loadedDataSet.rawAlignedToDifferences.series))
    val dy_sym = dy.evaluateInput(new Input(loadedDataSet.rawAlignedToDifferences.series))

    fitnessFormula(dx_num, dy_num, dx_sym, dy_sym)
  }

  private def fitnessFormula(dx_num: Array[Double], dy_num: Array[Double], dx_sym: Array[Double], dy_sym: Array[Double]): Double = {
    val filtered = dy_sym
      .zip(dx_sym)
      .zip(dx_num)
      .zip(dy_num)
      .map(x => (x._1._1._1, x._1._1._2, x._1._2, x._2))
      .filter(r => !isZero(r._2) && !isZero(r._4))

    if (filtered.length == 0) {
      Double.NegativeInfinity
    } else {
      // -filtered.length - the same as multiplying by -1 at the beginning
      val pairingError = filtered
        .map(r => Math.abs(Math.abs(r._1 / r._2) - Math.abs(r._3 / r._4)))
        .map(x => Math.log(1 + x))
        .sum / -filtered.length

      pairingError
    }
  }

  private def isZero(number: Double): Boolean = {
    Math.abs(number) < maxDifferenceToZero
  }
}