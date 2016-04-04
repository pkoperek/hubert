package pl.edu.agh.hubert.engine

import pl.edu.agh.hubert.languages.math.DifferentiationWithFitnessPredictionFitnessFunction

trait FitnessFunction {

  def evaluatePopulation(toEvaluate: Array[Individual]): Array[EvaluatedIndividual]

  /**
   * This implementation assumes that target fitness is the max error of the solution
   */
  def targetFitnessAchieved(targetFitness: Double, currentValue: Double): Boolean = {
    targetFitness > Math.abs(currentValue)
  }

}

object FitnessFunction {

  def functions() = Set[String](
    name(classOf[DifferentiationWithFitnessPredictionFitnessFunction])
  )

  private def name(value: Class[_]): String = value.getName

  def apply(experiment: Experiment): FitnessFunction = {
    if(classOf[DifferentiationWithFitnessPredictionFitnessFunction].getName == experiment.fitnessFunction) {
      return new DifferentiationWithFitnessPredictionFitnessFunction(experiment)
    }

    throw new IllegalArgumentException("Unknown fitness function: " + experiment.fitnessFunction)
  }

}

