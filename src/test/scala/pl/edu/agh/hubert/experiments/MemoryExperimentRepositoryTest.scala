package pl.edu.agh.hubert.experiments

import org.scalatest.FunSuite
import pl.edu.agh.hubert.languages.Languages

class MemoryExperimentRepositoryTest extends FunSuite {
  val experiment = new Experiment(1, "Test", "some experiment", Languages.mathLanguage())

  test("should add experiment") {
    val repo = new MemoryExperimentRepository

    repo.recordExperiment(experiment)
    val experiments = repo.listExperiments()
    assert(experiments.contains(experiment))
  }
  
  test("should remove experiment") {
    val repo = new MemoryExperimentRepository

    repo.recordExperiment(experiment)
    repo.removeExperiment(experiment.id)    
    
    assert(repo.listExperiments().isEmpty)
  }

  test("should true from removeExperiment if successful") {
    val repo = new MemoryExperimentRepository

    repo.recordExperiment(experiment)
    val removed = repo.removeExperiment(experiment.id)
    
    assert(removed)
  }
}
