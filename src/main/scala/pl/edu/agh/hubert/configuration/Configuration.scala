package pl.edu.agh.hubert.configuration

import pl.edu.agh.hubert.languages.Language
import pl.edu.agh.hubert.languages.math.MathLanguage
import pl.edu.agh.hubert.languages.neurons.NeuronsLanguage
import spray.json
import spray.json._

class Configuration {

  val languages = List[Language](new MathLanguage, new NeuronsLanguage)

}

object ConfigurationProtocol extends DefaultJsonProtocol {

  implicit object ConfigurationJsonFormat extends RootJsonFormat[Configuration] {
    def write(c: Configuration) =
      JsObject(
        "languages" -> JsArray(
          c.languages.map(
            l => translateLanguage(l)
          ).toVector
        )
      )

    def translateLanguage(language: Language): JsObject = {
      language.words.map(c => c.getName)
      JsObject(
        "name" -> JsString(language.getClass.getSimpleName),
        "words" -> JsArray(
          language.words.map(wordClass => JsString(wordClass.getSimpleName)).toVector
        )
      )
    }

    def read(value: JsValue) = value match {
      case _ => deserializationError("Configuration class is not deserializable!")
    }
  }

}