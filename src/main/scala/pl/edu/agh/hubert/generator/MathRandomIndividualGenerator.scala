package pl.edu.agh.hubert.generator

import java.lang.reflect.Constructor

import org.slf4j.LoggerFactory
import pl.edu.agh.hubert.experiments.Experiment
import pl.edu.agh.hubert.languages._
import pl.edu.agh.hubert.{Individual, MathIndividual}

import scala.collection.mutable.ArrayBuffer

trait IndividualGenerator {

  def generateGenome(maxHeight: Int): LanguageWord
  def generateIndividual(): Individual

}

object IndividualGenerator {

  private def randomWithMax(x: Int): Int = (Math.random() * (x - 1)).toInt

  def apply(experiment: Experiment): IndividualGenerator = {
    experiment.language.name match {
      case "math" => return new MathRandomIndividualGenerator(
        experiment.language.words.toArray,
        experiment.maxHeight, 
        randomWithMax, 
        experiment.dataSet.variables.size
      )
    }

    throw new RuntimeException("No individual generator specified!")
  }

}

class MathRandomIndividualGenerator(
                           val words: Array[Class[_]],
                           val maxHeight: Int,
                           val random: (Int) => Int, 
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
      return firstConstructor(word).newInstance(Math.random().asInstanceOf[Object]).asInstanceOf[LanguageWord]
    }

    if (classOf[Variable].isAssignableFrom(word)) {
      return firstConstructor(word).newInstance(randomElement(variablesIds).asInstanceOf[Object]).asInstanceOf[LanguageWord]
    }

    word.newInstance().asInstanceOf[LanguageWord]
  }

  private def randomElement[ElementType](array: Array[ElementType]): ElementType = array(random(array.length))

  private def firstConstructor(word: Class[_]): Constructor[_] = {
    word.getConstructors()(0)
  }

  def generateIndividual(): Individual = {
    new MathIndividual(generateGenome(maxHeight))
  }

}
