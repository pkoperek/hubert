package pl.edu.agh.hubert.generator

import pl.edu.agh.hubert.Individual
import pl.edu.agh.hubert.languages.{CompositeLanguage, LanguageWord}

trait IndividualGenerator {

  def generateIndividual(language: CompositeLanguage, maxHeight: Int): Individual

}

class RandomGenerator(val random: (Int) => Int) extends IndividualGenerator {

  private def generateTree(language: CompositeLanguage, maxHeight: Int): LanguageWord = {

    if (maxHeight == 0)
      return null
    //    random(language.words.length)

//    if (maxHeight == 1)
//      return language.terminalWords(random(language.terminalWords.length)).newInstance().asInstanceOf[LanguageWord]
//  
    // language.words(random(language.words.length)).newInstance().asInstanceOf[LanguageWord]
    
    language.terminalWords(0).newInstance().asInstanceOf[LanguageWord]
  }

  def generateIndividual(language: CompositeLanguage, maxHeight: Int): Individual = {
    new Individual(generateTree(language, maxHeight))
  }

}
