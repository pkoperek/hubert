package pl.edu.agh.hubert.generator

import org.scalatest.FunSuite
import pl.edu.agh.hubert.datasets.DataSet
import pl.edu.agh.hubert.{InputRow, Individual}
import pl.edu.agh.hubert.languages._

class MathRandomIndividualGeneratorTest extends FunSuite {

  val dummyLanguage = Array[Class[_]](classOf[DummyCompositeWord], classOf[DummyTerminalWord], classOf[OtherDummyTerminalWord])
  val constantLanguage = Array[Class[_]](classOf[Constant])
  val variableLanguage = Array[Class[_]](classOf[Variable])

  test("should create Variable with random variable name") {
    val individual = generateIndividualOfHeight(1, language = variableLanguage)

    assert(individual.tree.isInstanceOf[Variable])
    assert(individual.tree.asInstanceOf[Variable].id == "varA")
  }

  test("should create Constant with random value") {
    val individual = generateIndividualOfHeight(1, language = constantLanguage)

    assert(individual.tree.isInstanceOf[Constant])
    assert(individual.tree.asInstanceOf[Constant].value <= 1.0)
    assert(individual.tree.asInstanceOf[Constant].value >= 0.0)
  }

  test("should create individual with tree of height 0") {
    val individual = generateIndividualOfHeight(0)
    assert(individual.tree == null)
  }

  test("should create individual with tree of height 1") {
    val individual = generateIndividualOfHeight(1)
    assert(individual.tree != null)
  }

  test("should create individual with tree of height 1 and with Terminal at top") {
    val individual = generateIndividualOfHeight(1)
    assert(individual.tree != null)
    assert(individual.tree.isInstanceOf[TerminalWord])
  }

  test("should use random number generator to choose word") {
    val individual = generateIndividualOfHeight(1, _ => 1)
    assert(individual.tree != null)
    assert(individual.tree.isInstanceOf[OtherDummyTerminalWord])
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
    assert(individual.tree != null)
    assert(individual.tree.isInstanceOf[DummyCompositeWord])
    val root = individual.tree.asInstanceOf[CompositeWord]
    assert(root.internalWords(0).isInstanceOf[DummyTerminalWord])
    assert(root.internalWords(1).isInstanceOf[OtherDummyTerminalWord])
  }

  test("should create a tree with height 5") {
    val individual = generateIndividualOfHeight(5, _ => 0)
    assert(height(individual.tree) == 5)
  }

  private def generateIndividualOfHeight(
                                          maxHeight: Int = 0,
                                          random: (Int) => Int = _ => 0,
                                          language: Array[Class[_]] = dummyLanguage,
                                          variables: Array[String] = Array[String]("varA")
                                          ): Individual = {
    new MathRandomIndividualGenerator(language, maxHeight, random, variables).generateIndividual()
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

class DummyTerminalWord extends TerminalWord {
  override def evaluateInput(input: InputRow): Double = {
    1.0
  }
}

class OtherDummyTerminalWord extends TerminalWord {
  override def evaluateInput(input: InputRow): Double = {
    1.0
  }
}

class DummyCompositeWord(val left: LanguageWord, val right: LanguageWord) extends CompositeWord(Array(left, right), 2) {
  override def evaluateInput(input: InputRow): Double = {
    internalWords.map(w => w.evaluateInput(input)).sum
  }
}