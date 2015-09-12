package pl.edu.agh.hubert.generator

import org.scalatest.FunSuite
import pl.edu.agh.hubert._
import pl.edu.agh.hubert.languages._

class MathRandomIndividualGeneratorTest extends FunSuite {

  val dummyLanguage = DummyLanguage.dummyLanguage()
  val constantLanguage = Array[Class[_]](classOf[Constant])
  val variableLanguage = Array[Class[_]](classOf[Variable])

  test("should create Variable with random variable name") {
    val individual = generateIndividualOfHeight(1, language = variableLanguage)

    assert(individual.rawTree.isInstanceOf[Variable])
    assert(individual.rawTree.asInstanceOf[Variable].id == 0)
  }

  test("should create Constant with random value") {
    val individual = generateIndividualOfHeight(1, language = constantLanguage)

    assert(individual.rawTree.isInstanceOf[Constant])
    assert(individual.rawTree.asInstanceOf[Constant].value <= 1.0)
    assert(individual.rawTree.asInstanceOf[Constant].value >= 0.0)
  }

  test("should create individual with tree of height 0") {
    val individual = generateIndividualOfHeight(0)
    assert(individual.rawTree == null)
  }

  test("should create individual with tree of height 1") {
    val individual = generateIndividualOfHeight(1)
    assert(individual.rawTree != null)
  }

  test("should create individual with tree of height 1 and with Terminal at top") {
    val individual = generateIndividualOfHeight(1)
    assert(individual.rawTree != null)
    assert(individual.rawTree.isInstanceOf[TerminalWord])
  }

  test("should use random number generator to choose word") {
    val individual = generateIndividualOfHeight(1, _ => 1)
    assert(individual.rawTree != null)
    assert(individual.rawTree.isInstanceOf[OtherDummyTerminalWord])
  }

  test("should create two level tree") {
    var execution = 0
    val random: (Int) => Int = max => {
      var retVal = 1
      if (execution == 0) {
        retVal = 0
      } else if (execution == 1) {
        retVal = 0
      }

      execution += 1
      retVal
    }

    val individual = generateIndividualOfHeight(2, random)
    assert(individual.rawTree != null)
    assert(individual.rawTree.isInstanceOf[DummyCompositeWord])
    val root = individual.rawTree.asInstanceOf[CompositeWord]
    assert(root.internalWords(0).isInstanceOf[DummyTerminalWord])
    assert(root.internalWords(1).isInstanceOf[OtherDummyTerminalWord])
  }

  test("should create a tree with height 5") {
    val individual = generateIndividualOfHeight(5, _ => 0)
    assert(height(individual.rawTree) == 5)
  }

  private def generateIndividualOfHeight(
                                          maxHeight: Int = 0,
                                          random: (Int) => Int = _ => 0,
                                          language: Array[Class[_]] = dummyLanguage.words.toArray,
                                          variables: Array[String] = Array[String]("varA")
                                          ): Individual = {
    new MathRandomIndividualGenerator(language, maxHeight, random, variables.size).generateIndividual()
  }

  private def height(word: LanguageWord): Int = {
    if (word.isInstanceOf[CompositeWord]) {
      val compositeWord = word.asInstanceOf[CompositeWord]

      var max = 0
      for (internalWord <- compositeWord.internalWords) {
        val childHeight = height(internalWord)
        if (childHeight > max) {
          max = childHeight
        }
      }

      return max + 1
    }

    1
  }
}

