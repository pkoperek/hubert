package pl.edu.agh.hubert.languages

import pl.edu.agh.hubert.InputRow

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

class Sin(val internalWord: LanguageWord) extends CompositeWord(Array(internalWord), 1) {
  override def evaluateInput(input: InputRow): Double = {
    val internalResult = internalWord.evaluateInput(input)

    Math.sin(internalResult)
  }
}

class Cos(val internalWord: LanguageWord) extends CompositeWord(Array(internalWord), 1) {
  override def evaluateInput(input: InputRow): Double = {
    val internalResult = internalWord.evaluateInput(input)

    Math.cos(internalResult)
  }
}

class Plus(val leftWord: LanguageWord, val rightWord: LanguageWord) extends CompositeWord(Array(leftWord, rightWord), 2) {
  override def evaluateInput(input: InputRow): Double = {
    val left = leftWord.evaluateInput(input)
    val right = rightWord.evaluateInput(input)

    left + right
  }
}

class Minus(val leftWord: LanguageWord, val rightWord: LanguageWord) extends CompositeWord(Array(leftWord, rightWord), 2) {
  override def evaluateInput(input: InputRow): Double = {
    val left = leftWord.evaluateInput(input)
    val right = rightWord.evaluateInput(input)

    left - right
  }
}

class Mul(val leftWord: LanguageWord, val rightWord: LanguageWord) extends CompositeWord(Array(leftWord, rightWord), 2) {
  override def evaluateInput(input: InputRow): Double = {
    val left = leftWord.evaluateInput(input)
    val right = rightWord.evaluateInput(input)

    left * right
  }
}