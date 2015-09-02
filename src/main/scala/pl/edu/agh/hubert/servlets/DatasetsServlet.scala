package pl.edu.agh.hubert.servlets

import pl.edu.agh.hubert.datasets.DataSets

import spray.json._
import pl.edu.agh.hubert.datasets.DataSetProtocol._

class DatasetsServlet extends LoggingServlet {

  get("*") {
    contentType = "application/json"

    val json = DataSets.dataSets.toJson
    logger.info("Datasets: " + json.toString())

    json
  }

}
