package pl.edu.agh.hubert.languages.math

import org.slf4j.LoggerFactory
import pl.edu.agh.hubert.datasets.{CSVLoader, LoadedDataSet}
import pl.edu.agh.hubert.engine._
import pl.edu.agh.hubert.flat

class DifferentiationWithFitnessPredictionFitnessFunction(val experiment: Experiment) extends FitnessFunction {

  private val loadedDataSet = CSVLoader.load(experiment.dataSet)
  private val diffFF = new DifferentiationFitnessFunctionFormula(experiment.dataSet.variables.size)

  private val fitnessPredictorSize = 128
  private val fitnessPredictorMutationProbability = 0.10
  private val fitnessPredictorCrossOverProbability = 0.50

  private val fitnessPredictorCrossOverOperator = new FitnessPredictorCrossOverOperator(
    loadedDataSet,
    fitnessPredictorCrossOverProbability,
    fitnessPredictorSize
  )

  private val fitnessPredictorMutationOperator = new FitnessPredictorMutationOperator(
    loadedDataSet,
    fitnessPredictorMutationProbability,
    fitnessPredictorSize
  )

  private val fitnessPredictorEngine = new FitnessPredictorThread(
    loadedDataSet,
    fitnessPredictorCrossOverOperator,
    fitnessPredictorMutationOperator,
    fitnessPredictorSize,
    diffFF
  )

  private def storeMostRecentSolutionPopulation(mathPopulation: Array[MathIndividual]): Unit = {
    fitnessPredictorEngine.mostRecentSolutionPopulation(mathPopulation)
  }

  override def evaluatePopulation(toEvaluate: Array[Individual]): Array[EvaluatedIndividual] = {
    val mathPopulation = toEvaluate.map(asMathIndividual)
    storeMostRecentSolutionPopulation(mathPopulation)
    val fitnessPredictor = fitnessPredictorEngine.bestFitnessPredictor()
    val evaluatedSolutions = mathPopulation.map(asEvaluatedIndividual(fitnessPredictor))
    evaluatedSolutions
  }

  private def asMathIndividual(individual: Individual): MathIndividual = {
    individual.asInstanceOf[MathIndividual]
  }

  private def asEvaluatedIndividual(fitnessPredictor: FitnessPredictor)(individual: MathIndividual): EvaluatedIndividual = {
    new EvaluatedIndividual(
      individual,
      diffFF.evaluateFitnessFormula(individual, fitnessPredictor.data)
    )
  }

}