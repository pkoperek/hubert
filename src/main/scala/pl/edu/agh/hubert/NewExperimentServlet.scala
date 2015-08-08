package pl.edu.agh.hubert

import org.scalatra._
import org.slf4j.LoggerFactory

class NewExperimentServlet extends ScalatraServlet {

  val logger = LoggerFactory.getLogger(getClass)

  post("*") {
    contentType = "text"

    logger.info("adding new experiment: " + request.body)

    "ok"
  }

}
