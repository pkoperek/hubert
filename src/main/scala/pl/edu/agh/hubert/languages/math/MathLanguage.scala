package pl.edu.agh.hubert.languages.math

import pl.edu.agh.hubert.languages._

class MathLanguage extends Language(List(
  classOf[Constant],
  classOf[Variable],
  classOf[Sin],
  classOf[Cos]
)) {}

class Constant(val value: Double) extends TerminalWord() {
  override def evaluateInput(input: InputRow): Double = {
    value
  }
}

class Variable(val id: String) extends TerminalWord() {
  override def evaluateInput(input: InputRow): Double = {
    input.valueForId(id)
  }
}

class Sin(override val internalWords: List[LanguageWord]) extends CompositeWord(internalWords) {
  override def evaluateInput(input: InputRow): Double = {
    val internalResult = internalWords.head.evaluateInput(input)

    Math.sin(internalResult)
  }
}

class Cos(override val internalWords: List[LanguageWord]) extends CompositeWord(internalWords) {
  override def evaluateInput(input: InputRow): Double = {
    val internalResult = internalWords.head.evaluateInput(input)

    Math.cos(internalResult)
  }
}