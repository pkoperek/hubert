package pl.edu.agh.hubert.languages.math

import pl.edu.agh.hubert.datasets.LoadedDataSet

import scala.util.Random

class FitnessPredictorMutationOperator(
                                        val baseDataSet: LoadedDataSet,
                                        val fitnessPredictorMutationProbability: Double,
                                        val fitnessPredictorSize: Int
                                        ) {

  private val dataSetSize = baseDataSet.differencesSize
  private val random = new Random(System.currentTimeMillis())

  def mutate(parentPredictor: FitnessPredictor): FitnessPredictor = {
    if (random.nextDouble() < fitnessPredictorMutationProbability) {
      val pointToChange = random.nextInt(fitnessPredictorSize)
      val newRow = random.nextInt(dataSetSize)

      parentPredictor.predictorIndices(pointToChange) = newRow

      for (serieIdx <- parentPredictor.data.raw.series.indices) {
        parentPredictor.data.raw.series(serieIdx)(pointToChange) = baseDataSet.raw.series(serieIdx)(newRow)
      }
    }

    parentPredictor
  }

}
