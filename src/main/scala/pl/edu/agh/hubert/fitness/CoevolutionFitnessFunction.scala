package pl.edu.agh.hubert.fitness

import pl.edu.agh.hubert.Individual
import pl.edu.agh.hubert.datasets.CSVLoader
import pl.edu.agh.hubert.experiments.Experiment
import pl.edu.agh.hubert.hubert.Input

import scala.collection.mutable.ArrayBuffer
import scala.util.Random

class CoevolutionFitnessFunction(val experiment: Experiment) extends FitnessFunction {

  private val loadedDataSet = CSVLoader.load(experiment.dataSet)
  private var population = Array[Input]()
  private val fitnessPredictorSize = 4
  private val mutationProbability = 0.01
  private val crossOverProbability = 0.75
  private val dataSetSize = loadedDataSet.size

  override def evaluateIndividual(individual: Individual): Option[Double] = {
    None
  }

  private def generateIndividuals(howMuchToGenerate: Int): Array[Input] = {
    (1 to howMuchToGenerate).map(_ => generateIndividual()).toArray
  }

  private def generateIndividual(): Input = {
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

  }

  private class CrossOverOperator {

  }

}

