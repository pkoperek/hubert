package pl.edu.agh.hubert.experiments

import pl.edu.agh.hubert.languages.Language
import pl.edu.agh.hubert.languages.LanguageProtocol._
import spray.json._

case class Experiment(
                       id: Int,
                       name: String,
                       description: String,
                       language: Language
                       )

object ExperimentProtocol extends DefaultJsonProtocol {
  implicit val experimentFormat = jsonFormat4(Experiment)
}