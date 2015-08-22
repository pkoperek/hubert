package pl.edu.agh.hubert.servlets

class ExperimentsServlet extends LoggingServlet {

  get("*") {
    logger.info("listing experiments")
  }

  post("/add") {
    contentType = "text"

    logger.info("adding new experiment: " + request.body)

    "ok"
  }

  post("/upload") {
    logger.info("uploading new experiment: " + request.body)
  }

}