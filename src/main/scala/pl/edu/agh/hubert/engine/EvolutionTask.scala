package pl.edu.agh.hubert.engine

import pl.edu.agh.hubert.engine.ExperimentProtocol._
import pl.edu.agh.hubert.engine.EvolutionIterationStatisticsProtocol._
import spray.json._

class EvolutionTask(
                     val iterations: Int,
                     val evolutionEngine: EvolutionIteration
                   ) {

  var status = ExperimentStatus.New
  var currentIteration = 0
  var statistics: EvolutionIterationStatistics = EvolutionIterationStatistics(Double.MaxValue, Double.MinValue, Double.MinValue)
}

object EvolutionTask {

  def apply(experiment: Experiment): EvolutionTask = {
    new EvolutionTask(experiment.iterations, EvolutionIteration(experiment))
  }

}

object EvolutionTaskProtocol extends DefaultJsonProtocol {

  implicit object EvolutionTaskJsonFormat extends RootJsonFormat[EvolutionTask] {
    def write(et: EvolutionTask) =
      JsObject(
        "experiment" -> et.evolutionEngine.experiment.toJson,
        "currentIteration" -> JsNumber(et.currentIteration),
        "status" -> JsString(et.status.toString),
        "statistics" -> et.statistics.toJson
      )

    def read(value: JsValue) = value match {
      case _ => deserializationError("EvolutionTask can't be deserialized!")
    }
  }

}