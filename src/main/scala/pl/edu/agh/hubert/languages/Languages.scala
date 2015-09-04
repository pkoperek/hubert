package pl.edu.agh.hubert.languages

import spray.json.{DefaultJsonProtocol, JsArray, JsNumber, JsObject, JsString, JsValue, RootJsonFormat, _}

object Languages {

  def mathLanguage(): Language = Language(
    "math",
    Set(
      classOf[Sin],
      classOf[Cos],
      classOf[Constant],
      classOf[Variable]
    )
  )

  def neuronsLanguage(): Language = Language("neurons", Set())

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

final case class Language(name: String, words: Set[Class[_]]) {}

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
            words.map(word => Class.forName(word.toString().replaceAll("\"", ""))).toSet
          )
        case _ => deserializationError("Langugae expected")
      }
  }

}