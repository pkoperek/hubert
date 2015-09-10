package pl.edu.agh.hubert.fitness

import pl.edu.agh.hubert.{MathIndividual, Individual}
import pl.edu.agh.hubert.datasets.CSVLoader
import pl.edu.agh.hubert.experiments.Experiment

trait FitnessFunction {

  def evaluateIndividual(individual: Individual): Double

}

object FitnessFunction {

  def functions() = Set[String](
    name(classOf[DifferentiationFitnessFunction])
  )

  private def name(value: Class[_]): String = value.getName

  def apply(experiment: Experiment): FitnessFunction = {
    if (classOf[DifferentiationFitnessFunction].getName == experiment.fitnessFunction) {
      return new DifferentiationFitnessFunction(experiment)
    }

    throw new IllegalArgumentException("Unknown fitness function: " + experiment.fitnessFunction)
  }

}

class DifferentiationFitnessFunction(val experiment: Experiment) extends FitnessFunction {

  private val loadedDataSet = CSVLoader.load(experiment.dataSet)
  private val pairings = loadedDataSet.raw.indices.combinations(2).toArray.map(c => (c.seq(0), c.seq(1)))

  override def evaluateIndividual(individual: Individual): Double = {
    evaluateIndividual(individual.asInstanceOf[MathIndividual])
  }

  private def evaluateIndividual(individual: MathIndividual): Double = {
    pairings.par.map(pairing => {
      val x = pairing._1
      val y = pairing._2
      val N = loadedDataSet.size

      val dx_sym = individual.differentiatedBy(x).evaluateInput(loadedDataSet.raw)
      val dy_sym = individual.differentiatedBy(y).evaluateInput(loadedDataSet.raw)

      val dx_num = loadedDataSet.differentiated(x)
      val dy_num = loadedDataSet.differentiated(y)

      val symbolic = divide(dx_sym, dy_sym)
      val numerical = divide(dx_num, dy_num)

      minus(numerical, symbolic).par.map(x => Math.log(1 + (if (x > 0) x else -x))).sum / N
    }).min
  }

  private def divide(left: Array[Double], right: Array[Double]): Array[Double] = {
    left.zip(right).map(pair => pair._1 / pair._2)
  }

  private def minus(left: Array[Double], right: Array[Double]): Array[Double] = {
    left.zip(right).map(pair => pair._1 - pair._2)
  }
}