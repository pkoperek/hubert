package pl.edu.agh.hubert

import pl.edu.agh.hubert.languages._

import scala.collection.mutable

class MathIndividual(override val rawTree: LanguageWord) extends Individual(rawTree) {

  private val differentiatedCache = mutable.Map[Int, LanguageWord]()

  lazy val size = countElements(tree)

  lazy val tree = simplify(rawTree)

  def differentiatedBy(variable: Int) = differentiatedCache.getOrElseUpdate(variable, simplify(differentiateBy(variable, tree)))

  private def countElements(tree: LanguageWord): Int = {
    1 + (tree match {
      case compositeWord: CompositeWord => compositeWord.internalWords.map( word => countElements(word) ).sum
      case _ => 0
    })
  }

  private def simplify(word: LanguageWord): LanguageWord = {
    if (word.isInstanceOf[TerminalWord]) {
      return word
    }

    word match {
      case sin: Sin =>
        simplify(sin.internalWord) match {
          case constant: Constant => return new Constant(Math.sin(constant.value))
          case word: LanguageWord => return sin
        }

      case cos: Cos =>
        simplify(cos.internalWord) match {
          case constant: Constant => return new Constant(Math.cos(constant.value))
          case word: LanguageWord => return cos
        }

      case plus: Plus =>
        if (isConstant(plus.leftWord) && isConstant(plus.rightWord)) {
          return new Constant(constant(plus.leftWord) + constant(plus.rightWord))
        } else if (isZero(plus.leftWord)) {
          return plus.rightWord
        } else if (isZero(plus.rightWord)) {
          return plus.leftWord
        }

      case minus: Minus =>
        if (isConstant(minus.leftWord) && isConstant(minus.rightWord)) {
          return new Constant(constant(minus.leftWord) - constant(minus.rightWord))
        } else if (isZero(minus.rightWord)) {
          return minus.leftWord
        }

      case mul: Mul =>
        if (isConstant(mul.leftWord) && isConstant(mul.rightWord)) {
          return new Constant(constant(mul.leftWord) * constant(mul.rightWord))
        } else if (isOne(mul.leftWord)) {
          return mul.rightWord
        } else if (isOne(mul.rightWord)) {
          return mul.leftWord
        }
    }

    word
  }

  private def isOne(word: LanguageWord): Boolean = {
    isConstant(word) && constant(word) == 1.0
  }

  private def isZero(word: LanguageWord): Boolean = {
    isConstant(word) && constant(word) == 0.0
  }

  private def constant(word: LanguageWord): Double = word.asInstanceOf[Constant].value

  private def isConstant(word: LanguageWord): Boolean = {
    word.isInstanceOf[Constant]
  }

  private def differentiateBy(variable: Int, tree: LanguageWord): LanguageWord = {
    tree match {
      case sin: Sin =>
        return new Mul(
          new Cos(sin.internalWord),
          differentiateBy(variable, sin.internalWord)
        )

      case cos: Cos =>
        return new Mul(
          new Mul(
            new Constant(-1.0),
            new Sin(cos.internalWord)
          ),
          differentiateBy(variable, cos.internalWord)
        )

      case plus: Plus =>
        return new Plus(
          differentiateBy(variable, plus.leftWord),
          differentiateBy(variable, plus.rightWord)
        )

      case minus: Minus =>
        return new Minus(
          differentiateBy(variable, minus.leftWord),
          differentiateBy(variable, minus.rightWord)
        )

      case mul: Mul =>
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

      case variableTree: Variable =>
        if (variableTree.id == variable) {
          return new Constant(1.0)
        } else {
          return new Constant(0.0)
        }

      case constant: Constant => return new Constant(0.0)
    }

    throw new RuntimeException("Unknown function: " + tree)
  }

  override def toString: String = {
    "Individual: " + tree
  }

}