package pl.edu.agh.hubert.engine

import org.slf4j.LoggerFactory
import pl.edu.agh.hubert.Individual
import pl.edu.agh.hubert.datasets.{CSVLoader, LoadedDataSet}
import pl.edu.agh.hubert.experiments.Experiment
import pl.edu.agh.hubert.generator.IndividualGenerator

trait EvolutionEngine {
  def evolve()

  def experiment: Experiment
}


private class DefaultEvolutionEngine(val experiment: Experiment) extends EvolutionEngine {

  private val logger = LoggerFactory.getLogger(getClass)

  private val individualGenerator = IndividualGenerator(experiment)

  private val loadedDataSet = new LoadedDataSet(CSVLoader.load(experiment.dataSet))

  def generateInitialPopulation(size: Int): Array[Individual] = {
    (1 to size).map(_ => individualGenerator.generateIndividual()).toArray
  }

  private var population = generateInitialPopulation(experiment.populationSize)

  def evolve() = {
    logger.debug("Evolution iteration")

    individualGenerator.generateIndividual()
  }

}

object EvolutionEngine {

  def apply(experiment: Experiment): EvolutionEngine = {
    new DefaultEvolutionEngine(experiment)
  }

}