package pl.edu.agh.hubert.engine

class Individual(val rawTree: LanguageWord) {

  def evaluate(input: Input): Array[Double] = {
    rawTree.evaluateInput(input)
  }
}

case class EvaluatedIndividual(individual: Individual, fitness: Double) {
  def rawTree = individual.rawTree
}