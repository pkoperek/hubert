package pl.edu.agh.hubert

import pl.edu.agh.hubert.datasets.DataSet
import pl.edu.agh.hubert.engine.{Languages, Experiment}
import pl.edu.agh.hubert.languages.math.DifferentiationFitnessFunctionFormula

package object testfixtures {

  val circleExperiment = new Experiment(
    0,
    "test experiment",
    "no description",
    1,
    Languages.mathLanguage(),
    dataSet("circle.csv", Set("x", "y")),
    fitnessFunction = classOf[DifferentiationFitnessFunctionFormula].getName
  )

  def dataSet(path: String, variables: Set[String]): DataSet = {
    new DataSet(resolvePath(path), variables)
  }

  def resolvePath(fileName: String): String = {
    getClass.getResource("/csvloader/" + fileName).getPath
  }

}
