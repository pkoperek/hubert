package pl.edu.agh.hubert.engine

import pl.edu.agh.hubert.languages.math.MathCrossOverOperator

trait CrossOverOperator {

  def crossOver(left: Individual, right: Individual): (Individual, Individual)

}

object CrossOverOperator {

  def apply(experiment: Experiment): CrossOverOperator = {
    experiment.language.name match {
      case "math" => return new MathCrossOverOperator(experiment.crossOverProbability)
    }

    throw new IllegalArgumentException("Unknown language: " + experiment.language.name)
  }

}