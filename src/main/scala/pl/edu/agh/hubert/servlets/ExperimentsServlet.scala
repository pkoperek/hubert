package pl.edu.agh.hubert.servlets

import pl.edu.agh.hubert.engine.{EvolutionEngine, EvolutionExecutor, EvolutionTask}
import spray.json._
import pl.edu.agh.hubert.experiments.{ExperimentRepository, Experiment}
import pl.edu.agh.hubert.experiments.ExperimentProtocol._

class ExperimentsServlet(
                          val evolutionExecutor: EvolutionExecutor,
                          val experimentRepository: ExperimentRepository
                          ) extends LoggingServlet {

  get("*") {
    contentType = "application/json"
    JsArray(experimentRepository.listExperiments().map(e => e.toJson).toVector)
  }

  post("/run") {
    contentType = "application/json"

    val experiment = request.body.parseJson.convertTo[Experiment]

    logger.info("running new experiment: " + experiment)

    experimentRepository.recordExperiment(experiment)
    evolutionExecutor.addTask(EvolutionTask(experiment))

    "{ \"status\": \"started\" }"
  }

  post("/upload") {
    logger.info("uploading new experiment: " + request.body)
  }

  error {
    case t: Throwable => logger.error("Exception while handling request!", t)
  }

}