package pl.edu.agh.hubert.fitness

import pl.edu.agh.hubert.Individual
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
  private val pairings = (0 to loadedDataSet.raw.size - 1).combinations(2).toArray.map(c => (c.seq(0), c.seq(1)))

  override def evaluateIndividual(individual: Individual): Double = {
    1.0 // TODO: implement me
  }
}