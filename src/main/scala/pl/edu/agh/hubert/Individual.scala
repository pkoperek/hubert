package pl.edu.agh.hubert

import pl.edu.agh.hubert.languages._

import scala.collection.mutable

class Individual(val tree: LanguageWord) {

  def evaluate(inputRow: InputRow): Double = {
    tree.evaluateInput(inputRow)
  }
}

