package pl.edu.agh.hubert.languages.math

import pl.edu.agh.hubert.engine.{CompositeWord, LanguageWord, TerminalWord}
import pl.edu.agh.hubert.engine.Input

class Constant(val value: Double) extends TerminalWord() {
  override def evaluateInput(input: Input): Array[Double] = {
    Array.fill[Double](input(0).length)(value)
  }

  override def toString: String = value.toString
}

class Variable(val id: Int) extends TerminalWord() {
  override def evaluateInput(input: Input): Array[Double] = {
    input(id)
  }

  override def toString: String = "var_" + id.toString
}

class Sin(val internalWord: LanguageWord) extends CompositeWord(Array(internalWord), 1) {
  override def evaluateInput(input: Input): Array[Double] = {
    val internalResult = internalWord.evaluateInput(input)

    internalResult.map(value => Math.sin(value))
  }

  override def toString: String = "sin(" + internalWord + ")"
}

class Cos(val internalWord: LanguageWord) extends CompositeWord(Array(internalWord), 1) {
  override def evaluateInput(input: Input): Array[Double] = {
    val internalResult = internalWord.evaluateInput(input)

    internalResult.map(value => Math.cos(value))
  }

  override def toString: String = "cos(" + internalWord + ")"
}

class Plus(val leftWord: LanguageWord, val rightWord: LanguageWord) extends CompositeWord(Array(leftWord, rightWord), 2) {
  override def evaluateInput(input: Input): Array[Double] = {
    val left = leftWord.evaluateInput(input)
    val right = rightWord.evaluateInput(input)

    left.zip(right).map(pair => pair._1 + pair._2)
  }

  override def toString: String = "(" + leftWord + " + " + rightWord + ")"
}

class Minus(val leftWord: LanguageWord, val rightWord: LanguageWord) extends CompositeWord(Array(leftWord, rightWord), 2) {
  override def evaluateInput(input: Input): Array[Double] = {
    val left = leftWord.evaluateInput(input)
    val right = rightWord.evaluateInput(input)

    left.zip(right).map(pair => pair._1 - pair._2)
  }

  override def toString: String = "(" + leftWord + " - " + rightWord + ")"
}

class Mul(val leftWord: LanguageWord, val rightWord: LanguageWord) extends CompositeWord(Array(leftWord, rightWord), 2) {
  override def evaluateInput(input: Input): Array[Double] = {
    val left = leftWord.evaluateInput(input)
    val right = rightWord.evaluateInput(input)

    left.zip(right).map(pair => pair._1 * pair._2)
  }

  override def toString: String = "(" + leftWord + " * " + rightWord + ")"
}