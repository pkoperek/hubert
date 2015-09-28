package pl.edu.agh.hubert.engine

import pl.edu.agh.hubert.engine.ExperimentProtocol._
import spray.json._

class EvolutionTask(
                     val iterations: Int,
                     val evolutionEngine: EvolutionEngine
                     ) {

  var status = ExperimentStatus.New
  var currentIteration = 0
}

object EvolutionTask {

  def apply(experiment: Experiment): EvolutionTask = {
    new EvolutionTask(experiment.iterations, EvolutionEngine(experiment))
  }

}

object EvolutionTaskProtocol extends DefaultJsonProtocol {

  implicit object EvolutionTaskJsonFormat extends RootJsonFormat[EvolutionTask] {
    def write(et: EvolutionTask) =
      JsObject(
        "experiment" -> et.evolutionEngine.experiment.toJson,
        "currentIteration" -> JsNumber(et.currentIteration),
        "status" -> JsString(et.status.toString)
      )

    def read(value: JsValue) = value match {
      case _ => deserializationError("EvolutionTask can't be deserialized!")
    }
  }

}