package pl.edu.agh.hubert.engine

import java.io.File

import org.scalatest.{BeforeAndAfter, FunSuite}
import pl.edu.agh.hubert.datasets.DataSet
import pl.edu.agh.hubert.languages.math.DifferentiationWithFitnessPredictionFitnessFunction
import scalax.io._

class MemoryExperimentRepositoryTest extends FunSuite with BeforeAndAfter {
  val temporaryFile = File.createTempFile("temp", ".csv")

  Resource.fromFile(temporaryFile).writeStrings(List("#t\n", "1.0\n", "2.0"))

  val experiment = new Experiment(
    1,
    "Test",
    "some experiment",
    1,
    Languages.mathLanguage(),
    new DataSet(temporaryFile.getAbsolutePath, Set("t")),
    fitnessFunction = classOf[DifferentiationWithFitnessPredictionFitnessFunction].getName
  )

  before {
    temporaryFile.deleteOnExit()
  }

  test("should add experiment") {
    val repo = new MemoryExperimentRepository

    val evolutionTask = repo.recordExperiment(experiment)
    val experiments = repo.listExperimentExecutions()
    assert(experiments.contains(evolutionTask))
  }

  test("should return EvolutionTask") {
    val repo = new MemoryExperimentRepository

    assert(repo.recordExperiment(experiment) != null)
  }
}
