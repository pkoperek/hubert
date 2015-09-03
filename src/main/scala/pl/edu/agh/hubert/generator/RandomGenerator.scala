package pl.edu.agh.hubert.generator

import java.lang.reflect.Constructor

import pl.edu.agh.hubert.Individual
import pl.edu.agh.hubert.languages.{Language, CompositeWord, LanguageWord}

import scala.collection.mutable.ArrayBuffer

trait IndividualGenerator {

  def generateIndividual(): Individual

}

class RandomGenerator(val random: (Int) => Int, val language: Language, maxHeight: Int) extends IndividualGenerator {

  val terminalWords = language.words.filter(c => !classOf[CompositeWord].isAssignableFrom(c)).toArray
  val allWords = language.words.toArray

  private def generateTree(maxHeight: Int): LanguageWord = {

    if (maxHeight == 0)
      return null

    if (maxHeight == 1)
      return terminalWords(random(terminalWords.length)).newInstance().asInstanceOf[LanguageWord]

    val selectedWord: Class[_] = allWords(random(allWords.length))
    if (classOf[CompositeWord].isAssignableFrom(selectedWord)) {
      val selectedCompositeWord: Class[CompositeWord] = selectedWord.asInstanceOf[Class[CompositeWord]]
      val constructor: Constructor[_] = selectedCompositeWord.getConstructors()(0)
      val parametersCount = constructor.getParameterCount

      val buffer = ArrayBuffer[LanguageWord]()
      for (parameterNo <- 1 to parametersCount) {
        buffer += generateTree(maxHeight - 1)
      }

      return constructor.newInstance(buffer: _*).asInstanceOf[LanguageWord]
    }

    selectedWord.newInstance().asInstanceOf[LanguageWord]
  }

  def generateIndividual(): Individual = {
    new Individual(generateTree(maxHeight))
  }

}
