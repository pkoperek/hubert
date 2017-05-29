package pl.edu.agh.hubert.servlets

import pl.edu.agh.hubert.engine.{ExperimentRepository, Experiment, ExperimentProtocol, EvolutionExecutor}
import pl.edu.agh.hubert.engine.EvolutionTaskProtocol._
import ExperimentProtocol._
import spray.json._

class ExperimentsServlet(
                          val evolutionExecutor: EvolutionExecutor,
                          val experimentRepository: ExperimentRepository
                        ) extends LoggingServlet {

  get("/") {
    contentType = "application/json"

    JsArray(experimentRepository
      .listExperimentExecutions()
      .map(e => e.toJson)
      .toVector
    )
  }

  get("/:experimentId") {

    /**
      * Returns experiment definition
      */

    val experimentId = params("experimentId").toInt
    val maybeExperiment = experimentRepository.experimentById(experimentId)

    if (maybeExperiment.isEmpty) {
      throw new RuntimeException("No such experiment registered! (experiment id: " + experimentId + ")")
    }

    contentType = "application/octet-stream"
    response.setHeader("Content-Disposition", "attachment; filename=experiment-" + experimentId + ".json")

    maybeExperiment.get.toJson
  }

  post("/run") {
    contentType = "application/json"

    val experiment = request.body.parseJson.convertTo[Experiment]

    logger.info("running new experiment: " + experiment)

    val evolutionTask = experimentRepository.recordExperiment(experiment)
    evolutionExecutor.addTask(evolutionTask)

    "{ \"status\": \"started\" }"
  }

  post("/upload") {
    logger.info("uploading new experiment: " + request.body)
  }

  error {
    case t: Throwable => logger.error("Exception while handling request!", t)
  }

}