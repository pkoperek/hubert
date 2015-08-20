package pl.edu.agh.hubert

import pl.edu.agh.hubert.languages.{InputRow, LanguageWord}

class Individual(val tree: LanguageWord) {

  def evaluate(inputRow: InputRow): Double = {
    tree.evaluateInput(inputRow)
  }
}