package pl.edu.agh.hubert

import org.scalatest.{Matchers, FunSuite}
import pl.edu.agh.hubert.languages._

class MathIndividualTest extends FunSuite with Matchers {

  test("should differentiate sin(x)") {
    val differentiated = new MathIndividual(sin(variable("x"))).differentiatedBy("x")

    assert(differentiated.isInstanceOf[Cos])
    assert(differentiated.asInstanceOf[Cos].internalWord.isInstanceOf[Variable])
    assert(differentiated.asInstanceOf[Cos].internalWord.asInstanceOf[Variable].id == "x")
  }

  test("should differentiate x + x") {
    val differentiated = new MathIndividual(plus(variable("x"), variable("x"))).differentiatedBy("x")

    assert(differentiated.isInstanceOf[Constant])
    assert(differentiated.asInstanceOf[Constant].value == 2.0)

  }

  test("should differentiate x") {
    val differentiated = new MathIndividual(plus(variable("x"), variable("x"))).differentiatedBy("x")

    assert(differentiated.isInstanceOf[Constant])
    assert(differentiated.asInstanceOf[Constant].value == 1.0)
  }

  private def plus(left: LanguageWord, right: LanguageWord): Plus = {
    new Plus(left, right)
  }

  private def sin(internalWord: LanguageWord): Sin = {
    new Sin(internalWord)
  }

  private def variable(variable: String): Variable = {
    new Variable(variable)
  }
}
