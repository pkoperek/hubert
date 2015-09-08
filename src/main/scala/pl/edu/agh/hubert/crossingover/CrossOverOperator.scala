package pl.edu.agh.hubert.crossingover

import pl.edu.agh.hubert.Individual
import pl.edu.agh.hubert.experiments.Experiment

trait CrossOverOperator {
  
  def crossOver(left: Individual, right: Individual)
  
}

object CrossOverOperator {
  
  def apply(experiment: Experiment): CrossOverOperator = {
    return null
  }
  
}
