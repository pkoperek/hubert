package pl.edu.agh.hubert.fitness

import pl.edu.agh.hubert.Individual
import pl.edu.agh.hubert.datasets.CSVLoader
import pl.edu.agh.hubert.engine.PopulationRepository
import pl.edu.agh.hubert.experiments.Experiment
import pl.edu.agh.hubert.hubert.Input

import scala.collection.mutable.ArrayBuffer
import scala.util.Random

class CoevolutionFitnessFunction(val experiment: Experiment, val populationProvider: PopulationRepository) extends FitnessFunction {

  type FitnessPredictor = Input

  private val loadedDataSet = CSVLoader.load(experiment.dataSet)
  private var population = Array[FitnessPredictor]()
  private val fitnessPredictorSize = 4
  private val mutationProbability = 0.01
  private val crossOverProbability = 0.75
  private val dataSetSize = loadedDataSet.size

  override def evaluateIndividual(individual: Individual): Option[Double] = {
    None
  }

  private def generateFitnessPredictors(howMuchToGenerate: Int): Array[Input] = {
    (1 to howMuchToGenerate).map(_ => generateFitnessPredictor()).toArray
  }

  private def generateFitnessPredictor(): Input = {
    val predictorRows: Array[Int] = (1 to fitnessPredictorSize).map( _ => Random.nextInt(dataSetSize)).toArray

    val buffer = ArrayBuffer[Array[Double]]()
    for(serie <- loadedDataSet.raw) {
      val serieBuffer = ArrayBuffer[Double]()

      for(selectedRow <- predictorRows) {
        serieBuffer += serie(selectedRow)
      }

      buffer += serieBuffer.toArray
    }

    buffer.toArray
  }

  private class MutationOperator {
    def mutate(fitnessPredictor: FitnessPredictor): FitnessPredictor = {
      if(Random.nextDouble() < mutationProbability) {
        val pointToChange = Random.nextInt(fitnessPredictorSize)
        val newRow = Random.nextInt(dataSetSize)

        for(serieIdx <- fitnessPredictor.indices) {
          fitnessPredictor(serieIdx)(pointToChange) = loadedDataSet.raw(serieIdx)(newRow)
        }
      }

      fitnessPredictor
    }
  }

  private class CrossOverOperator {

    def crossOver(left: FitnessPredictor, right: FitnessPredictor): (FitnessPredictor, FitnessPredictor) = {
      if(Random.nextDouble() < crossOverProbability) {
        val crossOverPoint = Random.nextInt(fitnessPredictorSize)

        val leftChild = ArrayBuffer[Array[Double]]()
        val rightChild = ArrayBuffer[Array[Double]]()

        for(serieIdx <- left.indices) {
          val leftSerie = left(serieIdx)
          val rightSerie = right(serieIdx)

          val (leftChildHead, leftChildTail) = leftSerie.splitAt(crossOverPoint)
          val (rightChildHead, rightChildTail) = leftSerie.splitAt(crossOverPoint)

          leftChild += leftChildHead ++ rightChildTail
          rightChild += rightChildHead ++ leftChildTail
        }

        return (leftChild.toArray, rightChild.toArray)
      }

      (left, right)
    }

  }

}

