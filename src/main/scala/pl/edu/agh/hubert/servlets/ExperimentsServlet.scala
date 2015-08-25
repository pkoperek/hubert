package pl.edu.agh.hubert.servlets

import pl.edu.agh.hubert.engine.EvolutionExecutor
import spray.json._
import pl.edu.agh.hubert.experiments.Experiment
import pl.edu.agh.hubert.experiments.ExperimentProtocol._

class ExperimentsServlet(val evolutionExecutor: EvolutionExecutor) extends LoggingServlet {

  get("*") {
    logger.info("listing experiments")
  }

  post("/run") {
    contentType = "text"

    val experiment = request.body.parseJson.convertTo[Experiment]
    logger.info("running new experiment: " + experiment)

    "ok"
  }

  post("/upload") {
    logger.info("uploading new experiment: " + request.body)
  }

  error {
    case t: Throwable => logger.error("Exception while handling request!", t)
  }

}