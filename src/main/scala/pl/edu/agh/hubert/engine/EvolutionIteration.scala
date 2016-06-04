package pl.edu.agh.hubert.engine

import org.slf4j.LoggerFactory
import pl.edu.agh.hubert.{flat, pairs, randomPairs}

trait EvolutionIteration {
  def evolve(): EvolutionIterationResult

  def experiment: Experiment
}

case class EvolutionIterationResult(
                                     // Returns true if the solution hasn't been found
                                     val shouldContinue: Boolean,
                                     val iterationStatistics: EvolutionIterationStatistics
                                   )

private class DeterministicCrowdingEvolutionIteration(val experiment: Experiment) extends EvolutionIteration {

  private val logger = LoggerFactory.getLogger(getClass)

  private val individualGenerator = IndividualGenerator(experiment)
  private val fitnessFunction = FitnessFunction(experiment)
  private val crossOverOperator = CrossOverOperator(experiment)
  private val mutationOperator = MutationOperator(experiment)
  private var population = Array[EvaluatedIndividual]()
  private val targetFitness = experiment.targetFitness

  def evolve(): EvolutionIterationResult = {
    logger.debug("Deterministic Crowding Evolution iteration: start")

    population ++= missingIndividuals

    val groupedParents = randomPairs(population)
    val children = crossOverInPairs(groupedParents)
    val mutatedChildren = mutate(children)
    val evaluatedChildren = fitnessFunction.evaluatePopulation(mutatedChildren)

    population = tournament(groupedParents, pairs(evaluatedChildren))

    gatherResults()
  }

  private def gatherResults(): EvolutionIterationResult = {
    val avgFitness = population.foldLeft(0.0)(_ + _.fitness) / population.length
    val withMinFitness = population.minBy(_.fitness)
    val withMaxFitness = population.maxBy(_.fitness)

    if (logger.isDebugEnabled()) {
      logger.debug("Evolution iteration: end max (best): -> " + withMaxFitness.fitness)
      logger.debug("Evolution iteration: end min: -> " + withMinFitness.fitness)
      logger.debug("Evolution iteration: end avg: -> " + avgFitness)
    }

    new EvolutionIterationResult(
      shouldContinue(),
      new EvolutionIterationStatistics(avgFitness, withMinFitness.fitness, withMaxFitness.fitness)
    )
  }

  private def shouldContinue(): Boolean = {
    !fitnessFunction.targetFitnessAchieved(targetFitness, bestFitness())
  }

  private def bestFitness(): Double = {
    // assume that the higher fitness, the better
    if (population.nonEmpty) population.maxBy(_.fitness).fitness else Double.MinValue
  }

  private def missingIndividuals: Array[EvaluatedIndividual] = {
    val missingIndividualsNo = experiment.populationSize - population.length
    if (missingIndividualsNo > 0) {
      return generateRandomIndividuals(missingIndividualsNo)
    }

    Array.empty[EvaluatedIndividual]
  }

  private def generateRandomIndividuals(targetSize: Int): Array[EvaluatedIndividual] = {
    logger.info("Generating: " + targetSize + " random individuals")
    fitnessFunction.evaluatePopulation((1 to targetSize).map(_ => individualGenerator.generateIndividual()).toArray)
  }

  private def mutate(toMutate: Array[(Individual, Individual)]): Array[Individual] = {
    flat(toMutate.map(pair => (mutationOperator.mutate(pair._1), mutationOperator.mutate(pair._2))))
  }

  private def crossOverInPairs(parentsPairs: Array[(EvaluatedIndividual, EvaluatedIndividual)]): Array[(Individual, Individual)] = {
    parentsPairs.map(parents => crossOverOperator.crossOver(parents._1.individual, parents._2.individual))
  }

  private def tournament(
                          groupedParents: Array[(EvaluatedIndividual, EvaluatedIndividual)],
                          mutatedChildren: Array[(EvaluatedIndividual, EvaluatedIndividual)]): Array[EvaluatedIndividual] = {
    groupedParents.zip(mutatedChildren).flatMap(p => {
      Array(p._1._1, p._1._2, p._2._1, p._2._2).sortBy(-_.fitness).slice(0, 2)
    })
  }

}

object EvolutionIteration {

  def apply(experiment: Experiment): EvolutionIteration = {
    new DeterministicCrowdingEvolutionIteration(experiment)
  }

}