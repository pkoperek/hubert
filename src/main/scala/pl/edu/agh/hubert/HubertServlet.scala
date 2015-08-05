package pl.edu.agh.hubert

import org.scalatra._
import scalate.ScalateSupport

class HubertServlet extends ScalatraServlet with ScalateSupport {

  get("/") {
    contentType="text/html"
    
    layoutTemplate("/WEB-INF/views/index.ssp")
  }
  
  // use polymer + http://handsontable.com/
}
