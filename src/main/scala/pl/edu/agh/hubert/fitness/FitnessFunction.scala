package pl.edu.agh.hubert.fitness

import org.slf4j.LoggerFactory
import pl.edu.agh.hubert.languages.LanguageWord
import pl.edu.agh.hubert.{MathIndividual, Individual}
import pl.edu.agh.hubert.datasets.CSVLoader
import pl.edu.agh.hubert.experiments.Experiment

trait FitnessFunction {

  def evaluateIndividual(individual: Individual): Option[Double]

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

