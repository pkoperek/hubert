package pl.edu.agh.hubert.engine

import pl.edu.agh.hubert.languages.math.MathMutationOperator

trait MutationOperator {

  def mutate(individual: Individual): Individual

}

object MutationOperator {

  def apply(experiment: Experiment): MutationOperator = {
    experiment.language.name match {
      case "math" => return new MathMutationOperator(
        experiment.mutationProbability,
        experiment.maxHeight,
        IndividualGenerator(experiment))

    }
    throw new IllegalArgumentException("Unknown language!")
  }

}

