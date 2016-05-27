package pl.edu.agh.hubert.languages.math

import org.scalatest.FunSuite
import pl.edu.agh.hubert.datasets.CSVLoader
import pl.edu.agh.hubert.testfixtures._

class DifferentiationFitnessFunctionFormulaTest extends FunSuite {

  val circleDataset = CSVLoader.load(dataSet("circle.csv", Set("x", "y")))
  val fitnessFunction = new DifferentiationFitnessFunctionFormula(2)

  test("x^2 + y^2 - 4 should have very low error") {
    val x = new Variable(0)
    val y = new Variable(1)
    val properSolution = new MathIndividual(new Minus(new Plus(new Mul(x, x), new Mul(y, y)), new Constant(4.0)))

    val fitness = fitnessFunction.evaluateFitnessFormula(properSolution, circleDataset)

    assert(Math.abs(fitness) < 3e-12)
  }

  test("y should have high error") {
    val improperSolution = new MathIndividual(new Variable(1))

    val fitness = fitnessFunction.evaluateFitnessFormula(improperSolution, circleDataset)

    assert(Math.abs(fitness) > 3e-12)

  }

  test("x should have high error") {
    val improperSolution = new MathIndividual(new Variable(0))

    val fitness = fitnessFunction.evaluateFitnessFormula(improperSolution, circleDataset)

    assert(Math.abs(fitness) > 3e-12)
  }

  test("cos(x) should not have Infinity") {
    val improperSolution = new MathIndividual(new Cos(new Variable(0)))

    val fitness = fitnessFunction.evaluateFitnessFormula(improperSolution, circleDataset)

    assert(Math.abs(fitness) > 3e-12)
    assert(Math.abs(fitness) < Double.PositiveInfinity)
  }

}
