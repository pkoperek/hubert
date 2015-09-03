package pl.edu.agh.hubert.engine

import org.slf4j.LoggerFactory
import pl.edu.agh.hubert.experiments.Experiment
import pl.edu.agh.hubert.generator.RandomGenerator

trait EvolutionEngine {
  def evolve()

  def experiment: Experiment
}


private class DefaultEvolutionEngine(val experiment: Experiment) extends EvolutionEngine {

  private val logger = LoggerFactory.getLogger(getClass)

  private def randomWithMax(x: Int): Int = (Math.random() * (x - 1)).toInt

  private val individualGenerator = new RandomGenerator(randomWithMax, experiment.language, experiment.maxHeight)

  def evolve() = {
    logger.info("Evolution iteration")

    individualGenerator.generateIndividual()
  }

}

object EvolutionEngine {

  def apply(experiment: Experiment): EvolutionEngine = {
    new DefaultEvolutionEngine(experiment)
  }

}