package pl.edu.agh.hubert.experiments

import org.scalatest.FunSuite
import pl.edu.agh.hubert.languages.Languages

import spray.json._
import pl.edu.agh.hubert.experiments.ExperimentProtocol._

class ExperimentTest extends FunSuite {

  val experiment = new Experiment(1, "name of experiment", "description", Languages.mathLanguage())
  
  test("serialize and deserialize experiment") {
    val experimentAsJson = experiment.toJson.toString()
    val deserializedExperiment = experimentAsJson.parseJson.convertTo[Experiment]
    
    assert(experiment.name == deserializedExperiment.name)
    assert(experiment.description == deserializedExperiment.description)
    
    // TODO: learn how to implement equals in scala and fix the test!
    assert(experiment.language == deserializedExperiment.language)
    assert(experiment.id == deserializedExperiment.id)
  }
  
}
