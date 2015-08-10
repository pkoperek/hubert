package pl.edu.agh.hubert

class NewExperimentServlet extends LoggingServlet {

  post("*") {
    contentType = "text"

    logger.info("adding new experiment: " + request.body)

    "ok"
  }

}
