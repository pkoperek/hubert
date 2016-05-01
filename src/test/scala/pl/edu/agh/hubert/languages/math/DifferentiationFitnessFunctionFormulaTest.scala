package pl.edu.agh.hubert.languages.math

import org.scalatest.FunSuite
import pl.edu.agh.hubert.datasets.CSVLoader
import pl.edu.agh.hubert.testfixtures._

class DifferentiationFitnessFunctionFormulaTest extends FunSuite {

  val loadedDataSet = CSVLoader.load(dataSet("circle_1.csv", Set("t", "x", "y")))
  val fitnessFunction = new DifferentiationFitnessFunctionFormula(3)

  test("x^2 + y^2 - 4 should have very low error") {

    val x = new Variable(0)
    val y = new Variable(1)
    val properSolution = new MathIndividual(new Minus(new Plus(new Mul(x, x), new Mul(y, y)), new Constant(4.0)))

    val fitness = fitnessFunction.evaluateFitnessFormula(properSolution, loadedDataSet)

    assert(fitness.isDefined)
    assert(Math.abs(fitness.get) < 3e-12)
  }

}
