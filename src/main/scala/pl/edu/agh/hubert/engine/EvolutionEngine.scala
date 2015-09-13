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

  private def generateInitialPopulation(size: Int): Array[EvaluatedIndividual] = {
    logger.info("Generating: " + size + " random individuals")
    evaluateFitness((1 to size).map(_ => individualGenerator.generateIndividual()).toArray)
  }

  private def evaluateFitness(toEvaluate: Array[Individual]): Array[EvaluatedIndividual] = {
    toEvaluate.par.map(individual => evaluateFitness(individual)).toArray
  }

  private def evaluateFitness(individual: Individual): EvaluatedIndividual =
    new EvaluatedIndividual(
      individual,
      fitnessFunction.evaluateIndividual(individual)
    )

  private def mutate(toMutate: Array[(Individual, Individual)]): Array[(Individual, Individual)] = {
    toMutate.map(pair => (mutationOperator.mutate(pair._1), mutationOperator.mutate(pair._2)))
  }

  private def groupIntoPairs(toGroup: Array[EvaluatedIndividual]): Array[(EvaluatedIndividual, EvaluatedIndividual)] = {
    Random.shuffle(toGroup.toIterator).grouped(2).map(array => (array.head, array(1))).toArray
  }

  private def crossOverInPairs(parentsPairs: Array[(EvaluatedIndividual, EvaluatedIndividual)]): Array[(Individual, Individual)] = {
    parentsPairs.map(parents => crossOverOperator.crossOver(parents._1.individual, parents._2.individual))
  }

  def tournament(
                  groupedParents: Array[(EvaluatedIndividual, EvaluatedIndividual)],
                  mutatedChildren: Array[(EvaluatedIndividual, EvaluatedIndividual)]): Array[EvaluatedIndividual] = {
    groupedParents.zip(mutatedChildren).flatMap(p => {
      Array(p._1._1, p._1._2, p._2._1, p._2._2).sortBy(_.fitness).slice(0, 2)
    })
  }

  def evaluateFitness(toEvaluate: Array[(Individual, Individual)]): Array[(EvaluatedIndividual, EvaluatedIndividual)] = {
    toEvaluate.map(siblings => (evaluateFitness(siblings._1), evaluateFitness(siblings._2)))
  }

  def evolve() = {
    logger.debug("Evolution iteration")

    val groupedParents = groupIntoPairs(population)
    val children = crossOverInPairs(groupedParents)
    val mutatedChildren = mutate(children)
    val evaluatedChildren = evaluateFitness(mutatedChildren)

    population = tournament(groupedParents, evaluatedChildren)

    population.foreach( i => logger.debug("Individual: " + i.fitness + " > " + i ))
  }

}

object EvolutionEngine {

  def apply(experiment: Experiment): EvolutionEngine = {
    new DeterministicCrowdingEvolutionEngine(experiment)
  }

}