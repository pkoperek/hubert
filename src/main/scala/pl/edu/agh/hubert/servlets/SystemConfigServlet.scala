package pl.edu.agh.hubert.servlets

class SystemConfigServlet extends LoggingServlet {

  get("*") {
    contentType = "application/json"

    "ok"
  }

}

