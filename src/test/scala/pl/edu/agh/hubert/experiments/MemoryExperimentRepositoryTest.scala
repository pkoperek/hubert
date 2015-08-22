package pl.edu.agh.hubert.experiments

import org.scalatest.FunSuite

class MemoryExperimentRepositoryTest extends FunSuite {
  val experiment = new Experiment(1, "Test")

  test("should add experiment") {
    val repo = new MemoryExperimentRepository

    repo.addExperiment(experiment)
    val experiments = repo.listExperiments()
    assert(experiments.contains(experiment))
  }
  
  test("should remove experiment") {
    val repo = new MemoryExperimentRepository

    repo.addExperiment(experiment)
    repo.removeExperiment(experiment.id)    
    
    assert(repo.listExperiments().isEmpty)
  }

  test("should true from removeExperiment if successful") {
    val repo = new MemoryExperimentRepository

    repo.addExperiment(experiment)
    val removed = repo.removeExperiment(experiment.id)
    
    assert(removed)
  }
}
