package pl.edu.agh.hubert

import pl.edu.agh.hubert.hubert.Input
import pl.edu.agh.hubert.languages._

class Individual(val tree: LanguageWord) {

  def evaluate(input: Input): Array[Double] = {
    tree.evaluateInput(input)
  }
}

case class EvaluatedIndividual(individual: Individual, fitness: Double) {
  def tree = individual.tree

}

package object hubert {
  type Input = Array[Array[Double]]
}
