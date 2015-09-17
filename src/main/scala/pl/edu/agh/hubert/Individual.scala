package pl.edu.agh.hubert

import pl.edu.agh.hubert.hubert.Input
import pl.edu.agh.hubert.languages._

class Individual(val rawTree: LanguageWord) {

  def evaluate(input: Input): Array[Double] = {
    rawTree.evaluateInput(input)
  }
}

case class EvaluatedIndividual(individual: Individual, fitness: Option[Double]) {
  def rawTree = individual.rawTree
}

package object hubert {
  type Input = Array[Array[Double]]
}
