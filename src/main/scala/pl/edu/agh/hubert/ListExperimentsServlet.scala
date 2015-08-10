package pl.edu.agh.hubert

import org.scalatra._
import org.scalatra.scalate.ScalateSupport
import org.slf4j.LoggerFactory

class ListExperimentsServlet extends LoggingServlet {


  
  get("*") {
    logger.info("adding new experiment")
  }
  
}