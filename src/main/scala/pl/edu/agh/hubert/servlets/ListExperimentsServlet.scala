package pl.edu.agh.hubert.servlets

class ListExperimentsServlet extends LoggingServlet {


  
  get("*") {
    logger.info("adding new experiment")
  }
  
}