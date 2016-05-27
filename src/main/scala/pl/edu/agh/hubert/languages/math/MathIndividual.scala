package pl.edu.agh.hubert.languages.math

import pl.edu.agh.hubert.engine.{Individual, CompositeWord, LanguageWord, TerminalWord}

import scala.collection.mutable

class MathIndividual(override val rawTree: LanguageWord) extends Individual(rawTree) {

  private val differentiatedCache = mutable.Map[Int, LanguageWord]()

  lazy val size = countElements(tree)

  lazy val tree = simplify(rawTree)

  def differentiatedBy(variable: Int, dependent: Int = Int.MinValue) = differentiatedCache.getOrElseUpdate(
    variable,
    simplify(differentiateBy(variable, dependent, tree))
  )

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

      case plus: Plus => {
        val left = simplify(plus.leftWord)
        val right = simplify(plus.rightWord)

        if (isConstant(left) && isConstant(right)) {
          return new Constant(constant(left) + constant(right))
        } else if (isZero(left)) {
          return right
        } else if (isZero(right)) {
          return left
        }
      }

      case minus: Minus => {
        val left = simplify(minus.leftWord)
        val right = simplify(minus.rightWord)

        if (isConstant(left) && isConstant(right)) {
          return new Constant(constant(left) - constant(right))
        } else if (isZero(right)) {
          return left
        }
      }

      case mul: Mul => {
        val left = simplify(mul.leftWord)
        val right = simplify(mul.rightWord)

        if (isConstant(left) && isConstant(right)) {
          return new Constant(constant(left) * constant(right))
        } else if (isOne(left)) {
          return right
        } else if (isOne(right)) {
          return left
        } else if (isZero(right)) {
          return right
        } else if (isZero(left)) {
          return left
        }
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

  private def differentiateBy(variable: Int, dependent: Int, tree: LanguageWord): LanguageWord = {
    tree match {
      case sin: Sin =>
        return new Mul(
          new Cos(sin.internalWord),
          differentiateBy(variable, dependent, sin.internalWord)
        )

      case cos: Cos =>
        return new Mul(
          new Mul(
            new Constant(-1.0),
            new Sin(cos.internalWord)
          ),
          differentiateBy(variable, dependent, cos.internalWord)
        )

      case plus: Plus =>
        return new Plus(
          differentiateBy(variable, dependent, plus.leftWord),
          differentiateBy(variable, dependent, plus.rightWord)
        )

      case minus: Minus =>
        return new Minus(
          differentiateBy(variable, dependent, minus.leftWord),
          differentiateBy(variable, dependent, minus.rightWord)
        )

      case mul: Mul =>
        return new Plus(
          new Mul(
            differentiateBy(variable, dependent, mul.leftWord),
            mul.rightWord
          ),
          new Mul(
            mul.leftWord,
            differentiateBy(variable, dependent, mul.rightWord)
          )
        )

      case variableTree: Variable =>
        if (variableTree.id == variable) {
          return new Constant(1.0)
        } else if(variableTree.id == dependent) {
          // in reality when the variable y is interdependent with x
          // we compute the derivative as y' = dy/dx
          return new Variable(dependent, variable)
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