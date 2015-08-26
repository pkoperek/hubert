package pl.edu.agh.hubert.experiments

import scala.collection.concurrent.TrieMap

trait ExperimentRepository {

  def recordExperiment(experiment: Experiment)
  def removeExperiment(id: Int): Boolean
  def listExperiments(offset: Int = 0, limit: Int = Int.MaxValue): Array[Experiment]
  
}

class MemoryExperimentRepository extends ExperimentRepository {
  private val storage = new TrieMap[Int, Experiment]()
  
  override def recordExperiment(experiment: Experiment): Unit = {
    storage += (experiment.id -> experiment)
  }

  override def removeExperiment(id: Int): Boolean = {
    storage.remove(id).isDefined
  }

  override def listExperiments(offset: Int, limit: Int): Array[Experiment] = {
    storage.values.slice(offset, offset + limit).toArray
  }
}