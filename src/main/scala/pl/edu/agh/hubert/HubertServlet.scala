package pl.edu.agh.hubert

import org.scalatra._
import scalate.ScalateSupport

class HubertServlet extends ScalatraServlet with ScalateSupport {

  get("/") {
    <html>
      <body>
        <h1>Hello, world!</h1>
        Say
        <a href="hello-scalate">hello to Scalate</a>
        .
      </body>
    </html>
  }

}
