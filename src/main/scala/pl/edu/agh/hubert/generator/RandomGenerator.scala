package pl.edu.agh.hubert.generator

import pl.edu.agh.hubert.languages.{InputRow, LanguageWord, Language}

trait IndividualGenerator {
  
  def generateIndividual(language: Language, maxHeight: Int): Individual
  
}

class Individual(val tree: LanguageWord) {
  
  def evaluate(inputRow: InputRow): Double = {
    tree.evaluateInput(inputRow)    
  }
}

class RandomGenerator extends IndividualGenerator {

  def generateIndividual(language: Language, maxHeight: Int): Individual = {
    
    val tree = null

    new Individual(tree)
  }
  
}
