package pl.edu.agh.hubert.servlets

import spray.json._
import pl.edu.agh.hubert.configuration.InternalConfiguration
import pl.edu.agh.hubert.configuration.ConfigurationProtocol._

class SystemConfigServlet extends LoggingServlet {

  get("*") {
    contentType = "application/json"

    val json = (new InternalConfiguration).toJson
    
    logger.info("Returning configuration: " + json)
    
    json
  }

}

