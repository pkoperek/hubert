package pl.edu.agh.hubert.languages

import org.scalatest.FunSuite
import pl.edu.agh.hubert.InputRow

import spray.json._
import pl.edu.agh.hubert.languages.LanguageProtocol._

class LanguageTest extends FunSuite {

  val language = new Language("testLanguage", Array(classOf[DummyWord]))

  test("should serialize and deserialize a language") {
    val serializedLanguage = language.toJson.toString()
    val deserializedLanguage = serializedLanguage.parseJson.convertTo[Language]

    assert(deserializedLanguage.name == language.name)
    assert(deserializedLanguage.words.deep == language.words.deep)
  }

}

class DummyWord extends LanguageWord {
  override def evaluateInput(input: InputRow): Double = 1.0
}