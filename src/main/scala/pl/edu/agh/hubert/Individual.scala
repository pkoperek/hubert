package pl.edu.agh.hubert

import pl.edu.agh.hubert.languages._

import scala.collection.mutable

class Individual(val tree: LanguageWord) {

  def evaluate(inputRow: InputRow): Double = {
    tree.evaluateInput(inputRow)
  }
}

class MathIndividual(override val tree: LanguageWord) extends Individual(tree) {

  private val differentiatedCache = mutable.Map[String, LanguageWord]()

  def differentiatedBy(variable: String) = {

    if (!differentiatedCache.contains(variable)) {
      differentiatedCache += (variable -> simplify(differentiateBy(variable, tree)))
    }

    differentiatedCache.get(variable)
  }

  private def simplify(word: LanguageWord): LanguageWord = {
    word
  }

  private def differentiateBy(variable: String, tree: LanguageWord): LanguageWord = {
    tree.getClass.getName.toLowerCase match {
      case "sin" =>
        val sin = tree.asInstanceOf[Sin]
        return new Mul(
          new Cos(sin.internalWord),
          differentiateBy(variable, sin.internalWord)
        )

      case "cos" =>
        val cos = tree.asInstanceOf[Cos]
        return new Mul(
          new Mul(
            new Constant(-1.0),
            new Sin(cos.internalWord)
          ),
          differentiateBy(variable, cos.internalWord)
        )

      case "plus" =>
        val plus = tree.asInstanceOf[Plus]
        return new Plus(
          differentiateBy(variable, plus.leftWord),
          differentiateBy(variable, plus.rightWord)
        )

      case "minus" =>
        val minus = tree.asInstanceOf[Minus]
        return new Minus(
          differentiateBy(variable, minus.leftWord),
          differentiateBy(variable, minus.rightWord)
        )

      case "mul" =>
        val mul = tree.asInstanceOf[Mul]
        return new Plus(
          new Mul(
            differentiateBy(variable, mul.leftWord),
            mul.rightWord
          ),
          new Mul(
            mul.leftWord,
            differentiateBy(variable, mul.rightWord)
          )
        )

      case "variable" =>
        val variableTree = tree.asInstanceOf[Variable]
        if (variableTree.id == variable) {
          return new Constant(1.0)
        } else {
          return new Constant(0.0)
        }

      case "constant" => return new Constant(0.0)
    }

    throw new RuntimeException("Unknown function: " + tree)
  }

}