package pl.edu.agh.hubert.languages.math

import pl.edu.agh.hubert.datasets.LoadedDataSet

import scala.reflect.ClassTag
import scala.util.Random


class FitnessPredictorCrossOverOperator(
                                         val baseDataSet: LoadedDataSet,
                                         val fitnessPredictorCrossOverProbability: Double,
                                         val fitnessPredictorSize: Int
                                         ) {

  private val random = new Random(System.currentTimeMillis())

  def crossOver(left: FitnessPredictor, right: FitnessPredictor): (FitnessPredictor, FitnessPredictor) = {
    if (random.nextDouble() < fitnessPredictorCrossOverProbability) {
      val crossOverPoint = random.nextInt(fitnessPredictorSize)

      val (leftIndices, rightIndices) = crossOver(crossOverPoint, left.predictorIndices, right.predictorIndices)

      return (
        new FitnessPredictor(leftIndices, baseDataSet.subset(leftIndices)),
        new FitnessPredictor(rightIndices, baseDataSet.subset(rightIndices))
        )
    }

    (left, right)
  }

  private def crossOver[T: ClassTag](
                                      crossOverPoint: Int,
                                      leftSerie: Array[T],
                                      rightSerie: Array[T]
                                      ): (Array[T], Array[T]) = {
    val (leftChildHead, leftChildTail) = leftSerie.splitAt(crossOverPoint)
    val (rightChildHead, rightChildTail) = rightSerie.splitAt(crossOverPoint)

    val leftChildArray = leftChildHead ++ rightChildTail
    val rightChildArray = rightChildHead ++ leftChildTail
    (leftChildArray, rightChildArray)
  }
}
