package pl.edu.agh.hubert.engine

import pl.edu.agh.hubert.languages.math.{CoevolutionWithDifferentiationFitnessFunction, DifferentiationFitnessFunction}

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

  protected def evaluateFitness(individual: Individual): Option[Double] = None

}

object FitnessFunction {

  def functions() = Set[String](
    name(classOf[DifferentiationFitnessFunction]),
    name(classOf[CoevolutionWithDifferentiationFitnessFunction])
  )

  private def name(value: Class[_]): String = value.getName

  def apply(experiment: Experiment): FitnessFunction = {
    if (classOf[DifferentiationFitnessFunction].getName == experiment.fitnessFunction) {
      return new DifferentiationFitnessFunction(experiment)
    }

    if(classOf[CoevolutionWithDifferentiationFitnessFunction].getName == experiment.fitnessFunction) {
      return new CoevolutionWithDifferentiationFitnessFunction(experiment)
    }

    throw new IllegalArgumentException("Unknown fitness function: " + experiment.fitnessFunction)
  }

}

