package pl.edu.agh.hubert.engine

import org.slf4j.LoggerFactory
import pl.edu.agh.hubert.crossingover.CrossOverOperator
import pl.edu.agh.hubert.mutation.MutationOperator
import pl.edu.agh.hubert.{EvaluatedIndividual, Individual}
import pl.edu.agh.hubert.datasets.{CSVLoader, LoadedDataSet}
import pl.edu.agh.hubert.experiments.Experiment
import pl.edu.agh.hubert.fitness.FitnessFunction
import pl.edu.agh.hubert.generator.IndividualGenerator

import scala.util.Random

trait EvolutionEngine {
  def evolve()

  def experiment: Experiment
}

private class DeterministicCrowdingEvolutionEngine(val experiment: Experiment) extends EvolutionEngine {

  private val logger = LoggerFactory.getLogger(getClass)

  private val individualGenerator = IndividualGenerator(experiment)
  private val fitnessFunction = FitnessFunction(experiment)
  private val crossOverOperator = CrossOverOperator(experiment)
  private val mutationOperator = MutationOperator(experiment)
  private var population = generateInitialPopulation(experiment.populationSize)

  private def generateInitialPopulation(size: Int): Array[Individual] = {
    logger.info("Generating: " + size + " random individuals")
    (1 to size).map(_ => individualGenerator.generateIndividual()).toArray
  }

  private def evaluateFitness(toEvaluate: Array[Individual]): Array[EvaluatedIndividual] = {
    toEvaluate.par.map(individual =>
      new EvaluatedIndividual(
        individual,
        fitnessFunction.evaluateIndividual(individual)
      )
    ).toArray
  }

  private def mutate(toMutate: Array[(Individual, Individual)]): Array[(Individual, Individual)] = {
    toMutate
  }

  private def groupIntoPairs(toGroup: Array[EvaluatedIndividual]): Array[(EvaluatedIndividual, EvaluatedIndividual)] = {
    Random.shuffle(toGroup.toIterator).grouped(2).map(array => (array(0), array(1))).toArray
  }

  private def crossOverInPairs(parents: Array[(EvaluatedIndividual, EvaluatedIndividual)]): Array[(Individual, Individual)] = {
//    parents
    return null
  }

  def evolve() = {
    logger.debug("Evolution iteration")

    val evaluatedParents = evaluateFitness(population)
    val groupedParents = groupIntoPairs(evaluatedParents)
    val children = crossOverInPairs(groupedParents)
    val mutatedChildren = mutate(children)
  }

}

object EvolutionEngine {

  def apply(experiment: Experiment): EvolutionEngine = {
    new DeterministicCrowdingEvolutionEngine(experiment)
  }

}