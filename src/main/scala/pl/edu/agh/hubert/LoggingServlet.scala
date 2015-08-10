package pl.edu.agh.hubert

import org.scalatra.ScalatraServlet
import org.slf4j.LoggerFactory

class LoggingServlet extends ScalatraServlet {

  protected val logger =  LoggerFactory.getLogger(getClass)
  
}
