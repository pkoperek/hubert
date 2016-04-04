package pl.edu.agh.hubert.languages.math

import org.slf4j.LoggerFactory
import pl.edu.agh.hubert.datasets.{CSVLoader, LoadedDataSet}
import pl.edu.agh.hubert.engine._
import pl.edu.agh.hubert.flat

class DifferentiationWithFitnessPredictionFitnessFunction(val experiment: Experiment) extends FitnessFunction {

  private val logger = LoggerFactory.getLogger(getClass)

  private val loadedDataSet = CSVLoader.load(experiment.dataSet)
  private val pairings = loadedDataSet.raw.indices.combinations(2).toArray.map(c => (c.seq(0), c.seq(1)))

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
    fitnessPredictorSize
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
      evaluateSolutionIndividual(individual, fitnessPredictor.data)
    )
  }

  def assumeDataSetHasTimeVariable(loadedDataSet: LoadedDataSet) = {
    if (!loadedDataSet.hasTime()) {
      throw new UnsupportedOperationException("Timeless ")
    }
  }

  private def evaluateSolutionIndividual(solutionIndividual: MathIndividual, input: LoadedDataSet): Option[Double] = {

    assumeDataSetHasTimeVariable(loadedDataSet)

    val N = input.rawSize

    if (pairings.length == 1) {

      val t = loadedDataSet.timeVariableName()
      val f_t = (loadedDataSet.variables() - loadedDataSet.timeVariableName()).head

      val t_idx = loadedDataSet.indexOf(t).get
      val f_t_idx = loadedDataSet.indexOf(f_t).get

      val df_t_dt = solutionIndividual.differentiatedBy(t_idx)
      val df_t_dt_sym = df_t_dt.evaluateInput(input.raw)

      val dt_num = input.seriesOfDifferences(t_idx)
      val df_t_num = input.seriesOfDifferences(f_t_idx)

      val filtered = df_t_num.zip(dt_num).zip(df_t_dt_sym).filter(row => row._1._2 > 0)

      if (filtered.length == 0) {
        return None
      } else {
        val error = filtered
          .map(r => Math.abs(r._1._1 / r._1._2) - Math.abs(r._2))
          .map(x => Math.log(1 + Math.abs(x)))
          .sum / -N

        return Some(error)
      }

    } else {
      val pairingErrors = pairings.par.map(pairing => {
        val x = pairing._1
        val y = pairing._2

        val dx: LanguageWord = solutionIndividual.differentiatedBy(x)
        val dy: LanguageWord = solutionIndividual.differentiatedBy(y)

        val dx_sym = dx.evaluateInput(input.raw)
        val dy_sym = dy.evaluateInput(input.raw)

        val dx_num = input.seriesOfDifferences(x)
        val dy_num = input.seriesOfDifferences(y)

        val filtered = dy_sym
          .zip(dx_sym)
          .zip(dx_num)
          .zip(dy_num)
          .filter(r => r._1._1._2 > 0 && r._2 > 0)

        if (filtered.length == 0) {
          None
        } else {
          // -N - the same as multiplying by -1 at the beginning
          val pairingError = filtered
            //          .par
            .map(r => Math.abs(r._1._1._1 / r._1._1._2) - Math.abs(r._1._2 / r._2))
            .map(x => Math.log(1 + (if (x > 0) x else -x)))
            .sum / -N

          Some(pairingError)
        }
      })
        .seq
        .filter(o => o.isDefined)
        .map(o => o.get)

      if (pairingErrors.nonEmpty) {
        Some(pairingErrors.min)
      } else {
        None
      }
    }
  }

}