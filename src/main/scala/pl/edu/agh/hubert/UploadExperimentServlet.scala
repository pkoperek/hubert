package pl.edu.agh.hubert

import org.scalatra._
import org.scalatra.scalate.ScalateSupport
import org.slf4j.LoggerFactory

class UploadExperimentServlet extends ScalatraServlet {

  val logger = LoggerFactory.getLogger(getClass)

  post("*") {
    logger.info("uploading new experiment: " + request.body)
  }

}
