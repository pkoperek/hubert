package pl.edu.agh.hubert.engine

import scala.collection.concurrent.TrieMap

trait ExperimentRepository {

  def recordExperiment(experiment: Experiment): EvolutionTask

  def listExperimentExecutions(offset: Int = 0, limit: Int = Int.MaxValue): Array[EvolutionTask]

}

class MemoryExperimentRepository extends ExperimentRepository {
  private val storage = new TrieMap[Int, EvolutionTask]()
  private var currentId: Int = 1

  override def recordExperiment(experiment: Experiment): EvolutionTask = {
    val evolutionTask = EvolutionTask(assignId(experiment))
    storage += (evolutionTask.evolutionEngine.experiment.id -> evolutionTask)

    evolutionTask
  }

  private def assignId(experiment: Experiment): Experiment = experiment.copyWithId(nextId())

  private def nextId(): Int = {
    val id = currentId
    currentId += 1
    id
  }

  override def listExperimentExecutions(offset: Int, limit: Int): Array[EvolutionTask] = {
    storage.values.slice(offset, offset + limit).toArray
  }
}