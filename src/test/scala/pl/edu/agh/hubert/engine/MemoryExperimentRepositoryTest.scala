package pl.edu.agh.hubert.engine

import org.scalatest.{BeforeAndAfter, FunSuite}

import pl.edu.agh.hubert.testfixtures.staticExperiment

class MemoryExperimentRepositoryTest extends FunSuite with BeforeAndAfter {

  test("should add experiment") {
    val repo = new MemoryExperimentRepository

    val evolutionTask = repo.recordExperiment(staticExperiment)
    val experiments = repo.listExperimentExecutions()
    assert(experiments.contains(evolutionTask))
  }

  test("should return EvolutionTask") {
    val repo = new MemoryExperimentRepository

    assert(repo.recordExperiment(staticExperiment) != null)
  }
}
