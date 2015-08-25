package pl.edu.agh.hubert.languages

object Languages {

  def mathLanguage(): Language = Language(
    "math",
    Array(
      classOf[Sin],
      classOf[Cos],
      classOf[Constant],
      classOf[Variable]
    )
  )

  def neuronsLanguage(): Language = Language("neurons", Array())

  val baseLanguages = Array[Language](mathLanguage(), neuronsLanguage())

  def baseLanguageByName(name: String): Option[Language] = {
    for (language <- baseLanguages) {
      if (language.name == name) {
        return Some(language)
      }
    }

    None
  }

}

case class Language(name: String, words: Array[Class[_]])

import spray.json.{DefaultJsonProtocol, JsArray, JsNumber, JsObject, JsString, JsValue, RootJsonFormat, _}

object LanguageProtocol extends DefaultJsonProtocol {

  implicit object ColorJsonFormat extends RootJsonFormat[Language] {
    def write(l: Language) = JsObject(
      "name" -> JsString(l.name),
      "words" -> JsArray(
        l.words.map(clazz => JsString(clazz.getCanonicalName)).toVector
      )
    )

    def read(value: JsValue) =
      value.asJsObject.getFields("name", "words") match {
        case Seq(JsString(name), JsArray(words)) =>
          Language(
            name,
            words.map(word => Class.forName(word.toString().replaceAll("\"", ""))).toArray
          )
        case _ => deserializationError("Langugae expected")
      }
  }

}