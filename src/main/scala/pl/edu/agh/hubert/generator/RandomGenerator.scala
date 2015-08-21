package pl.edu.agh.hubert.generator

import java.lang.reflect.Constructor

import pl.edu.agh.hubert.Individual
import pl.edu.agh.hubert.languages.{CompositeWord, CompositeLanguage, LanguageWord}

import scala.collection.mutable.ArrayBuffer

trait IndividualGenerator {

  def generateIndividual(language: CompositeLanguage, maxHeight: Int): Individual

}

class RandomGenerator(val random: (Int) => Int) extends IndividualGenerator {

  private def generateTree(language: CompositeLanguage, maxHeight: Int): LanguageWord = {

    if (maxHeight == 0)
      return null

    if (maxHeight == 1)
      return language.terminalWords(random(language.terminalWords.length)).newInstance().asInstanceOf[LanguageWord]

    val selectedWord: Class[_] = language.words(random(language.words.length))
    if (classOf[CompositeWord].isAssignableFrom(selectedWord)) {
      val selectedCompositeWord: Class[CompositeWord] = selectedWord.asInstanceOf[Class[CompositeWord]]
      val constructor: Constructor[_] = selectedCompositeWord.getConstructors()(0)
      val parametersCount = constructor.getParameterCount

      val buffer = ArrayBuffer[LanguageWord]()
      for (parameterNo <- 1 to parametersCount) {
        buffer += generateTree(language, maxHeight - 1)
      }
      
      return constructor.newInstance(buffer:_*).asInstanceOf[LanguageWord]
    }

    selectedWord.newInstance().asInstanceOf[LanguageWord]
  }

  def generateIndividual(language: CompositeLanguage, maxHeight: Int): Individual = {
    new Individual(generateTree(language, maxHeight))
  }

}
