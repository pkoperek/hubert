package pl.edu.agh.hubert.languages.math

import pl.edu.agh.hubert.datasets.CSVLoader
import pl.edu.agh.hubert.engine._

class DifferentiationFitnessFunction(val experiment: Experiment) extends FitnessFunction {

  private val maxDifferenceToZero = 1e-6f
  private val loadedDataSet = CSVLoader.load(experiment.dataSet)

  override def evaluatePopulation(toEvaluate: Array[Individual]): Array[EvaluatedIndividual] = {
    toEvaluate.map(individual => evaluateIndividual(individual))
  }

  private def evaluateIndividual(individual: Individual): EvaluatedIndividual =
    new EvaluatedIndividual(
      individual,
      evaluateFitness(individual)
    )

  private def evaluateFitness(individual: Individual): Option[Double] = {
    evaluateFitness(individual.asInstanceOf[MathIndividual])
  }

  private def evaluateFitness(individual: MathIndividual): Option[Double] = {
    val N = loadedDataSet.rawSize

    val x = 0
    val y = 1

    val dx: LanguageWord = individual.differentiatedBy(x)
    val dy: LanguageWord = individual.differentiatedBy(y)

    val dx_sym = dx.evaluateInput(loadedDataSet.rawAlignedToDifferences)
    val dy_sym = dy.evaluateInput(loadedDataSet.rawAlignedToDifferences)

    val dx_num = loadedDataSet.seriesOfDifferences(x)
    val dy_num = loadedDataSet.seriesOfDifferences(y)

    val filtered = dy_sym
      .zip(dx_sym)
      .zip(dx_num)
      .zip(dy_num)
      .map(x => (x._1._1._1, x._1._1._2, x._1._2, x._2))
      .filter(r => !isZero(r._2) && !isZero(r._4))

    if (filtered.length == 0) {
      None
    } else {
      // -N - the same as multiplying by -1 at the beginning
      val pairingError = filtered
        //          .par
        .map(r => Math.abs(Math.abs(r._1 / r._2) - Math.abs(r._3 / r._4)))
        .map(x => Math.log(1 + x))
        .sum / -N

      Some(pairingError)
    }
  }

  private def isZero(number: Double): Boolean = {
    Math.abs(number) < maxDifferenceToZero
  }
}