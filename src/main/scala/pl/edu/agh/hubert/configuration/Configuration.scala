package pl.edu.agh.hubert.configuration

import pl.edu.agh.hubert.languages.{MathLanguage, NeuronsLanguage}
import spray.json._

class Configuration {

  val languages = List(new MathLanguage, new NeuronsLanguage)

}

object ConfigurationProtocol extends DefaultJsonProtocol {

  implicit object ConfigurationJsonFormat extends RootJsonFormat[Configuration] {
    def write(c: Configuration) =
      JsObject(
        "languages" -> JsArray(
          c.languages.map(l => JsString(l.getClass.getName)).toVector
        )
      )

    def read(value: JsValue) = value match {
      case _ => deserializationError("Configuration class is not deserializable!")
    }
  }

}