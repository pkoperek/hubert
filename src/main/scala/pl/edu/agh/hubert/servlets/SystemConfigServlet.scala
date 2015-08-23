package pl.edu.agh.hubert.servlets

import spray.json._
import pl.edu.agh.hubert.configuration.Configuration
import pl.edu.agh.hubert.configuration.ConfigurationProtocol._

class SystemConfigServlet extends LoggingServlet {

  get("*") {
    contentType = "application/json"

    val json = Configuration.export().toJson
    
    logger.info("Returning configuration: " + json)
    
    json
  }

}

