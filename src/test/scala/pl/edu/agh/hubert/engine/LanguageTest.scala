package pl.edu.agh.hubert.engine

import org.scalatest.FunSuite
import pl.edu.agh.hubert.engine.LanguageProtocol._
import spray.json._

class LanguageTest extends FunSuite {

  val language = new Language("testLanguage", Set(classOf[DummyWord]))

  test("should serialize and deserialize a language") {
    val serializedLanguage = language.toJson.toString()
    val deserializedLanguage = serializedLanguage.parseJson.convertTo[Language]

    assert(deserializedLanguage.name == language.name)
    assert(deserializedLanguage.words == language.words)
  }

}

class DummyWord extends LanguageWord {
  override def evaluateInput(input: Input): Array[Double] = Array.fill(input.variablesCount)(0.0)
}