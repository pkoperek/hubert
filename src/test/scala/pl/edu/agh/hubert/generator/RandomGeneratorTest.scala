package pl.edu.agh.hubert.generator

import org.scalatest.FunSuite
import pl.edu.agh.hubert.languages._

class RandomGeneratorTest extends FunSuite {
  
  test("should create individual with tree of height 0") {
    val individual = new RandomGenerator().generateIndividual(new DummyLanguage, 0)
    assert(individual.tree == null)
  }
  
}

class DummyLanguage extends Language(List(
  classOf[DummyTerminalWord],
  classOf[DummyCompositeWord]
)) {}

class DummyTerminalWord extends TerminalWord {
  override def evaluateInput(input: InputRow): Double = { 1.0 }
}

class DummyCompositeWord(override val internalWords: List[LanguageWord]) extends CompositeWord(internalWords, 2) {
  override def evaluateInput(input: InputRow): Double = {
    internalWords.map(w => w.evaluateInput(input)).sum
  }
}