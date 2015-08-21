package pl.edu.agh.hubert.languages.math

import pl.edu.agh.hubert.languages._

class MathLanguage extends CompositeLanguage(
  Array(
    classOf[Sin],
    classOf[Cos]
  ),
  Array(
    classOf[Constant],
    classOf[Variable]
  )
) {}

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

class Sin(override val internalWords: Array[LanguageWord]) extends CompositeWord(internalWords, 1) {
  override def evaluateInput(input: InputRow): Double = {
    val internalResult = internalWords.head.evaluateInput(input)

    Math.sin(internalResult)
  }
}

class Cos(override val internalWords: Array[LanguageWord]) extends CompositeWord(internalWords, 1) {
  override def evaluateInput(input: InputRow): Double = {
    val internalResult = internalWords.head.evaluateInput(input)

    Math.cos(internalResult)
  }
}

class Plus(override val internalWords: Array[LanguageWord]) extends CompositeWord(internalWords, 2) {
  override def evaluateInput(input: InputRow): Double = {
    val left = internalWords(1).evaluateInput(input)
    val right = internalWords(2).evaluateInput(input)

    left + right
  }
}

class Minus(override val internalWords: Array[LanguageWord]) extends CompositeWord(internalWords, 2) {
  override def evaluateInput(input: InputRow): Double = {
    val left = internalWords(1).evaluateInput(input)
    val right = internalWords(2).evaluateInput(input)

    left - right
  }
}