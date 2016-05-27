package pl.edu.agh.hubert.languages.math

import org.scalatest.{FunSuite, Matchers}
import pl.edu.agh.hubert.engine.{Experiment, Languages}
import pl.edu.agh.hubert.testfixtures.circleExperiment

class DifferentiationWithFitnessPredictionFitnessFunctionTest extends FunSuite with Matchers {

  test("should have very low error") {
    val experiment = circleExperiment
    val x = new Variable(0)
    val y = new Variable(1)
    val individualTree = new Minus(new Plus(new Mul(x, x), new Mul(y, y)), new Constant(4.0))
    val properSolution = new MathIndividual(individualTree)

    val fitnessFunction = new DifferentiationWithFitnessPredictionFitnessFunction(experiment)
    val evaluatedPopulation = fitnessFunction.evaluatePopulation(Array(properSolution))

    val fitness = evaluatedPopulation(0).fitness
    assert(Math.abs(fitness) < 3e-12)
  }

}
