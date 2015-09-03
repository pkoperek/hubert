package pl.edu.agh.hubert.experiments

import pl.edu.agh.hubert.datasets.DataSet
import pl.edu.agh.hubert.datasets.DataSetProtocol._
import pl.edu.agh.hubert.languages.Language
import pl.edu.agh.hubert.languages.LanguageProtocol._
import spray.json._

final case class Experiment(
                             id: Int,
                             name: String,
                             description: String,
                             iterations: Int,
                             language: Language,
                             dataSet: DataSet,
                             maxHeight: Int
                             ) {

  def copyWithId(newId: Int): Experiment =
    Experiment(
      newId,
      name,
      description,
      iterations,
      language,
      dataSet,
      maxHeight
    )
}

object ExperimentProtocol extends DefaultJsonProtocol {
  implicit val experimentFormat = jsonFormat7(Experiment)
}