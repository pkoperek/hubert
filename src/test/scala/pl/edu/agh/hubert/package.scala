package pl.edu.agh.hubert

import java.io.File

import pl.edu.agh.hubert.datasets.DataSet
import pl.edu.agh.hubert.engine.{Experiment, Languages}
import pl.edu.agh.hubert.languages.math.{DifferentiationFitnessFunctionFormula, DifferentiationWithFitnessPredictionFitnessFunction}

import scalax.io.Resource

package object testfixtures {

  val staticExperimentFile = File.createTempFile("temp", ".csv")
  staticExperimentFile.deleteOnExit()
  Resource.fromFile(staticExperimentFile).writeStrings(List("#t,varA\n", "1.0,3.0\n", "2.0,4.0\n", "3.0,6.0"))

  val staticExperiment = Experiment(
    1,
    "exp1",
    "desc1",
    10,
    Languages.mathLanguage(),
    new DataSet(staticExperimentFile.getAbsolutePath, Set("t", "varA")),
    fitnessFunction = classOf[DifferentiationWithFitnessPredictionFitnessFunction].getName
  )

  val circleExperiment = new Experiment(
    0,
    "test experiment",
    "no description",
    1,
    Languages.mathLanguage(),
    dataSet("circle_1.csv", Set("t", "x", "y")),
    fitnessFunction = classOf[DifferentiationFitnessFunctionFormula].getName
  )

  def dataSet(path: String, variables: Set[String]): DataSet = {
    new DataSet(resolvePath(path), variables)
  }

  def resolvePath(fileName: String): String = {
    getClass.getResource("/csvloader/" + fileName).getPath
  }

}
