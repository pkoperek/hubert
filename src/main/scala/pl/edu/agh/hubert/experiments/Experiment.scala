package pl.edu.agh.hubert.experiments

import pl.edu.agh.hubert.languages.Language
import pl.edu.agh.hubert.languages.LanguageProtocol._
import spray.json._

final case class Experiment(
                             id: Int,
                             name: String,
                             description: String,
                             iterations: Int,
                             language: Language
                             )

object ExperimentProtocol extends DefaultJsonProtocol {
  implicit val experimentFormat = jsonFormat5(Experiment)
}