package pl.edu.agh.hubert.configuration

import com.typesafe.config.ConfigFactory
import pl.edu.agh.hubert.fitness.FitnessFunction
import pl.edu.agh.hubert.languages.LanguageProtocol._
import pl.edu.agh.hubert.languages.{Language, Languages}
import spray.json._

object Configuration {

  private val config = ConfigFactory.load()

  val threads = config.getInt("executor.threads")
  val taskWaitTime = config.getInt("executor.taskWaitTime")
  val datasetsStorageDirectory = config.getString("datasets.storageDirectory")

  def export(): WebAppConfiguration = WebAppConfiguration(Languages.baseLanguages.toSet, FitnessFunction.functions())
}

case class WebAppConfiguration(languages: Set[Language], fitnessFunctions: Set[String])

object WebAppConfigurationProtocol extends DefaultJsonProtocol {

  implicit val webAppConfigurationFormat = jsonFormat(WebAppConfiguration, "languages", "fitnessFunctions")
  
}