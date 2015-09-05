package pl.edu.agh.hubert.engine

import org.slf4j.LoggerFactory
import pl.edu.agh.hubert.Individual
import pl.edu.agh.hubert.datasets.{CSVLoader, LoadedDataSet}
import pl.edu.agh.hubert.experiments.Experiment
import pl.edu.agh.hubert.generator.IndividualGenerator

import scala.util.Random

trait EvolutionEngine {
  def evolve()

  def experiment: Experiment
}


private class DeterministicCrowdingEvolutionEngine(val experiment: Experiment) extends EvolutionEngine {

  private val logger = LoggerFactory.getLogger(getClass)

  private val individualGenerator = IndividualGenerator(experiment)

  private val loadedDataSet = new LoadedDataSet(CSVLoader.load(experiment.dataSet))

  def generateInitialPopulation(size: Int): Array[Individual] = {
    logger.info("Generating: " + size + " random individuals")
    (1 to size).map(_ => individualGenerator.generateIndividual()).toArray
  }

  private var population = generateInitialPopulation(experiment.populationSize)

  def evaluateIndividual(individual: Individual): Double = {
    1.0
    
  }
  
  def evaluateFitness(toEvaluate: Array[(Individual, Individual)]) {
    toEvaluate.map( pair => (evaluateIndividual(pair._1), evaluateIndividual(pair._2)) )
  }

  def mutate(toMutate: Array[(Individual, Individual)]): Array[(Individual, Individual)] = {
    toMutate
  }

  def groupIntoPairs(): Array[(Individual, Individual)] = {
    Random.shuffle(population).grouped(2).map(array => (array(0), array(1))).toArray
  }

  def crossOverInPairs(parents: Array[(Individual, Individual)]): Array[(Individual, Individual)] = {
    parents
  }

  def evolve() = {
    logger.debug("Evolution iteration")

    evaluateFitness()
    val parents = groupIntoPairs()
    val children = crossOverInPairs(parents)
    val mutatedChildren = mutate(children)
  }

}

object EvolutionEngine {

  def apply(experiment: Experiment): EvolutionEngine = {
    new DeterministicCrowdingEvolutionEngine(experiment)
  }

}