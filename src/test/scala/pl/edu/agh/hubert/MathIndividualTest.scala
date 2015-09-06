package pl.edu.agh.hubert

import org.scalatest.{Matchers, FunSuite}
import pl.edu.agh.hubert.languages._

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

  private def plus(left: LanguageWord, right: LanguageWord): Plus = {
    new Plus(left, right)
  }

  private def sin(internalWord: LanguageWord): Sin = {
    new Sin(internalWord)
  }

  private def variable(variable: Int): Variable = {
    new Variable(variable)
  }
}
