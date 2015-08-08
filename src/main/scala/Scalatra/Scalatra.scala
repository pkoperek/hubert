import org.scalatra.LifeCycle
import javax.servlet.ServletContext
import org.slf4j.LoggerFactory
import pl.edu.agh.hubert._

class ScalatraBootstrap extends LifeCycle {

  val logger =  LoggerFactory.getLogger(getClass)

  override def init(context: ServletContext) {

    logger.info("Initializing Scalatra")
    
    // mount servlets like this:
    context mount (new NewExperimentServlet, "/add")
    context mount (new ListExperimentsServlet, "/list")
    context mount (new UploadExperimentServlet, "/upload")
  }
}