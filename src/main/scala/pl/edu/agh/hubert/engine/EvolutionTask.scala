package pl.edu.agh.hubert.engine

import pl.edu.agh.hubert.experiments.Experiment

class EvolutionTask(val iterations: Int, val evolutionEngine: EvolutionEngine) {}

object EvolutionTask {

  def apply(experiment: Experiment): EvolutionTask = {
    new EvolutionTask(experiment.iterations, EvolutionEngine())
  }

}
