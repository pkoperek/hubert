package pl.edu.agh.hubert.engine

import org.slf4j.LoggerFactory

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
  private var population = Array[EvaluatedIndividual]()

  def evolve() = {
    logger.debug("Evolution iteration")

    population ++= missingIndividuals

    val groupedParents = groupedIntoPairs(population)
    val children = crossOverInPairs(groupedParents)
    val mutatedChildren = mutate(children)
    val evaluatedChildren = fitnessFunction.evaluatePopulation(mutatedChildren)

    population = tournament(groupedParents, evaluatedChildren)

    val result = if (population.nonEmpty) population.minBy(_.fitnessValue) else "None!"
    logger.debug("Final fitness: " + result)
  }

  private def missingIndividuals: Array[EvaluatedIndividual] = {
    val missingIndividualsNo = experiment.populationSize - population.length
    if(missingIndividualsNo > 0) {
      return generateRandomIndividuals(missingIndividualsNo)
    }

    Array.empty[EvaluatedIndividual]
  }

  private def generateRandomIndividuals(targetSize: Int): Array[EvaluatedIndividual] = {
    logger.info("Generating: " + targetSize + " random individuals")
    fitnessFunction.evaluatePopulation((1 to targetSize).map(_ => individualGenerator.generateIndividual()).toArray)
  }

  private def mutate(toMutate: Array[(Individual, Individual)]): Array[(Individual, Individual)] = {
    toMutate.map(pair => (mutationOperator.mutate(pair._1), mutationOperator.mutate(pair._2)))
  }

  private def groupedIntoPairs(toGroup: Array[EvaluatedIndividual]): Array[(EvaluatedIndividual, EvaluatedIndividual)] = {
    Random.shuffle(toGroup.toIterator).grouped(2).map(array => (array.head, array(1))).toArray
  }

  private def crossOverInPairs(parentsPairs: Array[(EvaluatedIndividual, EvaluatedIndividual)]): Array[(Individual, Individual)] = {
    parentsPairs.map(parents => crossOverOperator.crossOver(parents._1.individual, parents._2.individual))
  }

  private def tournament(
                          groupedParents: Array[(EvaluatedIndividual, EvaluatedIndividual)],
                          mutatedChildren: Array[(EvaluatedIndividual, EvaluatedIndividual)]): Array[EvaluatedIndividual] = {
    groupedParents.zip(mutatedChildren).flatMap(p => {
      Array(p._1._1, p._1._2, p._2._1, p._2._2).filter(i => i.isValid).sortBy(-_.fitnessValue).slice(0, 2)
    })
  }

}

object EvolutionEngine {

  def apply(experiment: Experiment): EvolutionEngine = {
    new DeterministicCrowdingEvolutionEngine(experiment)
  }

}