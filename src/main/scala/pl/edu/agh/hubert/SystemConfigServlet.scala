package pl.edu.agh.hubert

class SystemConfigServlet extends LoggingServlet {

  get("*") {
    contentType = "application/json"

    "ok"
  }

}

