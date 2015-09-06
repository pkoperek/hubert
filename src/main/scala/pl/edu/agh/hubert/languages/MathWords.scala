package pl.edu.agh.hubert.languages

import pl.edu.agh.hubert.InputRow

class Constant(val value: Double) extends TerminalWord() {
  override def evaluateInput(input: InputRow): Double = {
    value
  }

  override def toString: String = value.toString
}

class Variable(val id: String) extends TerminalWord() {
  override def evaluateInput(input: InputRow): Double = {
    input.valueForId(id)
  }

  override def toString: String = id
}

class Sin(val internalWord: LanguageWord) extends CompositeWord(Array(internalWord), 1) {
  override def evaluateInput(input: InputRow): Double = {
    val internalResult = internalWord.evaluateInput(input)

    Math.sin(internalResult)
  }

  override def toString: String = "sin(" + internalWord + ")"
}

class Cos(val internalWord: LanguageWord) extends CompositeWord(Array(internalWord), 1) {
  override def evaluateInput(input: InputRow): Double = {
    val internalResult = internalWord.evaluateInput(input)

    Math.cos(internalResult)
  }

  override def toString: String = "cos(" + internalWord + ")"
}

class Plus(val leftWord: LanguageWord, val rightWord: LanguageWord) extends CompositeWord(Array(leftWord, rightWord), 2) {
  override def evaluateInput(input: InputRow): Double = {
    val left = leftWord.evaluateInput(input)
    val right = rightWord.evaluateInput(input)

    left + right
  }

  override def toString: String = "(" + leftWord + " + " + rightWord + ")"
}

class Minus(val leftWord: LanguageWord, val rightWord: LanguageWord) extends CompositeWord(Array(leftWord, rightWord), 2) {
  override def evaluateInput(input: InputRow): Double = {
    val left = leftWord.evaluateInput(input)
    val right = rightWord.evaluateInput(input)

    left - right
  }

  override def toString: String = "(" + leftWord + " - " + rightWord + ")"
}

class Mul(val leftWord: LanguageWord, val rightWord: LanguageWord) extends CompositeWord(Array(leftWord, rightWord), 2) {
  override def evaluateInput(input: InputRow): Double = {
    val left = leftWord.evaluateInput(input)
    val right = rightWord.evaluateInput(input)

    left * right
  }

  override def toString: String = "(" + leftWord + " * " + rightWord + ")"
}