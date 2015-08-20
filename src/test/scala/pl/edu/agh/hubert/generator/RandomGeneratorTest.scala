package pl.edu.agh.hubert.generator

import org.scalatest.FunSuite
import pl.edu.agh.hubert.Individual
import pl.edu.agh.hubert.languages._

class RandomGeneratorTest extends FunSuite {

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

  private def generateIndividualOfHeight(maxHeight: Int = 0): Individual = {
    new RandomGenerator(_ => 1).generateIndividual(new DummyLanguage, maxHeight)
  }

}

class DummyLanguage extends CompositeLanguage(
  Array(classOf[DummyCompositeWord]),
  Array(classOf[DummyTerminalWord])
) {}

class DummyTerminalWord extends TerminalWord {
  override def evaluateInput(input: InputRow): Double = {
    1.0
  }
}

class DummyCompositeWord(override val internalWords: List[LanguageWord]) extends CompositeWord(internalWords, 2) {
  override def evaluateInput(input: InputRow): Double = {
    internalWords.map(w => w.evaluateInput(input)).sum
  }
}