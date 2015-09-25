package pl.edu.agh.hubert.fitness

import pl.edu.agh.hubert.experiments.Experiment
import pl.edu.agh.hubert.{EvaluatedIndividual, Individual}

trait FitnessFunction {

  def evaluatePopulation(toEvaluate: Array[Individual]): Array[EvaluatedIndividual] = {
    toEvaluate.map(individual => evaluateIndividual(individual))
  }

  def evaluatePopulation(toEvaluate: Array[(Individual, Individual)]): Array[(EvaluatedIndividual, EvaluatedIndividual)] = {
    toEvaluate.map(siblings => (evaluateIndividual(siblings._1), evaluateIndividual(siblings._2)))
  }

  protected def evaluateIndividual(individual: Individual): EvaluatedIndividual =
    new EvaluatedIndividual(
      individual,
      evaluateFitness(individual)
    )

  protected def evaluateFitness(individual: Individual): Option[Double]

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

