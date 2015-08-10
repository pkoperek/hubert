package pl.edu.agh.hubert.servlets

class UploadExperimentServlet extends LoggingServlet {

  post("*") {
    logger.info("uploading new experiment: " + request.body)
  }

}
