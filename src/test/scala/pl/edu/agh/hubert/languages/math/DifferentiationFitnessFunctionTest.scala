package pl.edu.agh.hubert.languages.math

import org.scalatest.FunSuite
import pl.edu.agh.hubert.engine.{Languages, Experiment}
import pl.edu.agh.hubert.testfixtures._

class DifferentiationFitnessFunctionTest extends FunSuite {

  val fitnessFunction = new DifferentiationFitnessFunction(circleExperiment)

  test("x^2 + y^2 - 4 should have very low error") {

    val x = new Variable(0)
    val y = new Variable(1)
    val properSolution = new MathIndividual(new Minus(new Plus(new Mul(x, x), new Mul(y, y)), new Constant(4.0)))

    val evaluatedPopulation = fitnessFunction.evaluatePopulation(Array(properSolution))

    val fitness = evaluatedPopulation(0).fitness

    assert(fitness.isDefined)
    assert(Math.abs(fitness.get) < 3e-12)
  }

}
