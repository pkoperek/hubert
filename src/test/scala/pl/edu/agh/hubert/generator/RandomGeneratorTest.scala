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

  test("should use random number generator to choose word") {
    val individual = generateIndividualOfHeight(1, _ => 1)
    assert(individual.tree != null)
    assert(individual.tree.isInstanceOf[OtherDummyTerminalWord])
  }

  private def generateIndividualOfHeight(maxHeight: Int = 0, random: (Int) => Int = _ => 0): Individual = {
    new RandomGenerator(random).generateIndividual(new DummyLanguage, maxHeight)
  }

}

class DummyLanguage extends CompositeLanguage(
  Array(classOf[DummyCompositeWord]),
  Array(classOf[DummyTerminalWord], classOf[OtherDummyTerminalWord])
) {}

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

class DummyCompositeWord(override val internalWords: List[LanguageWord]) extends CompositeWord(internalWords, 2) {
  override def evaluateInput(input: InputRow): Double = {
    internalWords.map(w => w.evaluateInput(input)).sum
  }
}