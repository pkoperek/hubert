package pl.edu.agh.hubert.languages.math

import java.lang.reflect.Constructor

import org.slf4j.LoggerFactory
import pl.edu.agh.hubert.engine.{CompositeWord, Individual, IndividualGenerator, LanguageWord}

import scala.collection.mutable.ArrayBuffer
import scala.util.Random

class MathRandomIndividualGenerator(
                                     val words: Array[Class[_]],
                                     val maxHeight: Int,
                                     val random: Random,
                                     val variablesIdsNo: Int
                                     ) extends IndividualGenerator {

  private val logger = LoggerFactory.getLogger(getClass)

  private val terminalWords = words.filter(c => !isCompositeWord(c))
  private val allWords = words
  private val variablesIds = (0 to variablesIdsNo-1).toArray

  private def isCompositeWord(c: Class[_]): Boolean = {
    classOf[CompositeWord].isAssignableFrom(c)
  }

  def generateGenome(maxHeight: Int): LanguageWord = {
    if (maxHeight == 0)
      return null

    if (maxHeight == 1) {
      return instantiateTerminalWord(randomElement(terminalWords))
    }

    val selectedWord: Class[_] = randomElement(allWords)
    if (isCompositeWord(selectedWord)) {
      return instantiateCompositeWord(maxHeight, selectedWord)
    }

    instantiateTerminalWord(selectedWord)
  }

  private def instantiateCompositeWord(maxHeight: Int, word: Class[_]): LanguageWord = {
    if(logger.isDebugEnabled) {
      logger.debug("Composite, selected: " + word)
    }

    val selectedCompositeWord: Class[CompositeWord] = word.asInstanceOf[Class[CompositeWord]]
    val constructor: Constructor[_] = firstConstructor(selectedCompositeWord)
    val parametersCount = constructor.getParameterCount

    val buffer = ArrayBuffer[LanguageWord]()
    for (parameterNo <- 1 to parametersCount) {
      buffer += generateGenome(maxHeight - 1)
    }

    constructor.newInstance(buffer: _*).asInstanceOf[LanguageWord]
  }

  private def instantiateTerminalWord(word: Class[_]): LanguageWord = {
    logger.debug("Terminal, selected: " + word)
    if (classOf[Constant].isAssignableFrom(word)) {
      return new Constant(Math.random())
    }

    if (classOf[Variable].isAssignableFrom(word)) {
      return new Variable(randomElement(variablesIds))
    }

    word.newInstance().asInstanceOf[LanguageWord]
  }

  private def randomElement[ElementType](array: Array[ElementType]): ElementType = array(random.nextInt(array.length))

  private def firstConstructor(word: Class[_]): Constructor[_] = {
    word.getConstructors()(0)
  }

  def generateIndividual(): Individual = {
    new MathIndividual(generateGenome(maxHeight))
  }

}
