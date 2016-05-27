package pl.edu.agh.hubert.languages.math

import org.scalatest.{FunSuite, Matchers}
import pl.edu.agh.hubert.engine.LanguageWord

class MathIndividualTest extends FunSuite with Matchers {

  test("should differentiate sin(x)") {
    val differentiated = new MathIndividual(sin(variable(0))).differentiatedBy(0)

    assert(differentiated.isInstanceOf[Cos])
    assert(differentiated.asInstanceOf[Cos].internalWord.isInstanceOf[Variable])
    assert(differentiated.asInstanceOf[Cos].internalWord.asInstanceOf[Variable].id == 0)
  }

  test("should differentiate x + x") {
    val differentiated = new MathIndividual(plus(variable(0), variable(0))).differentiatedBy(0)

    assert(differentiated.isInstanceOf[Constant])
    assert(differentiated.asInstanceOf[Constant].value == 2.0)

  }

  test("should differentiate x") {
    val differentiated = new MathIndividual(variable(0)).differentiatedBy(0)

    assert(differentiated.isInstanceOf[Constant])
    assert(differentiated.asInstanceOf[Constant].value == 1.0)
  }

  test("should differentiate y when interdependent with x") {
    val differentiated = new MathIndividual(variable(1)).differentiatedBy(0, 1)

    assert(differentiated.isInstanceOf[Variable])
    assert(differentiated.asInstanceOf[Variable].id == 1)
    assert(differentiated.asInstanceOf[Variable].isDependentOf == 0)
  }

  test("should differentiate 2*y when y interdependent with x") {
    val differentiated = new MathIndividual(mul(const(2.0), variable(1))).differentiatedBy(0, 1)

    assert(differentiated.isInstanceOf[Mul])

    val left = differentiated.asInstanceOf[Mul].leftWord
    val right = differentiated.asInstanceOf[Mul].rightWord

    assert(left.isInstanceOf[Constant])
    assert(left.asInstanceOf[Constant].value == 2.0)
    assert(right.isInstanceOf[Variable])
    assert(right.asInstanceOf[Variable].id == 1)
    assert(right.asInstanceOf[Variable].isDependentOf == 0)
  }

  test("should differentiate x*y with y interdependent with x") {
    val differentiated = new MathIndividual(mul(variable(0), variable(1))).differentiatedBy(0, 1)

    assert(differentiated.isInstanceOf[Plus])

    val left = differentiated.asInstanceOf[Plus].leftWord
    val right = differentiated.asInstanceOf[Plus].rightWord

    assert(left.asInstanceOf[Mul].leftWord.asInstanceOf[Constant].value == 1.0)
    assert(left.asInstanceOf[Mul].rightWord.asInstanceOf[Variable].id == 1)

    assert(right.asInstanceOf[Mul].leftWord.asInstanceOf[Variable].id == 0)
    assert(right.asInstanceOf[Mul].rightWord.asInstanceOf[Variable].id == 1)
    assert(right.asInstanceOf[Mul].rightWord.asInstanceOf[Variable].isDependentOf == 0)
  }

  private def mul(left: LanguageWord, right: LanguageWord): Mul = {
    new Mul(left, right)
  }

  private def plus(left: LanguageWord, right: LanguageWord): Plus = {
    new Plus(left, right)
  }

  private def sin(internalWord: LanguageWord): Sin = {
    new Sin(internalWord)
  }

  private def variable(variable: Int): Variable = {
    new Variable(variable)
  }

  private def const(number: Double): Constant = {
    new Constant(number)
  }

}
