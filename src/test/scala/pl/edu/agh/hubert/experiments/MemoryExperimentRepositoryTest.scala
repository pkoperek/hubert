package pl.edu.agh.hubert.experiments

import java.io.File

import org.scalatest.{BeforeAndAfter, FunSuite}
import pl.edu.agh.hubert.datasets.DataSet
import pl.edu.agh.hubert.fitness.DifferentiationFitnessFunction
import pl.edu.agh.hubert.languages.Languages

class MemoryExperimentRepositoryTest extends FunSuite with BeforeAndAfter{
  val temporaryFile = File.createTempFile("temp", ".csv")
  val experiment = new Experiment(
    1, 
    "Test", 
    "some experiment", 
    1, 
    Languages.mathLanguage(), 
    new DataSet(temporaryFile.getAbsolutePath, Set("varA")),
    fitnessFunction = classOf[DifferentiationFitnessFunction].getName
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
