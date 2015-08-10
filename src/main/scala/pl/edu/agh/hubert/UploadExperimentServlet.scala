package pl.edu.agh.hubert

import org.slf4j.LoggerFactory

class UploadExperimentServlet extends LoggingServlet {

  post("*") {
    logger.info("uploading new experiment: " + request.body)
  }

}
