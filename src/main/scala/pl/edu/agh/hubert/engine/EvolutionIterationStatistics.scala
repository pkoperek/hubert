package pl.edu.agh.hubert.engine

import spray.json._

case class EvolutionIterationStatistics(
                                         val avgFitness: Double,
                                         val minFitness: Double,
                                         val maxFitness: Double,
                                         val bestIndividual: String = "None"
                                       )

object EvolutionIterationStatisticsProtocol extends DefaultJsonProtocol {

  implicit object EvolutionIterationStatisticsJsonFormat extends RootJsonFormat[EvolutionIterationStatistics] {
    def write(is: EvolutionIterationStatistics) =
      JsObject(
        "avg" -> JsNumber(is.avgFitness),
        "min" -> JsNumber(is.minFitness),
        "max" -> JsNumber(is.maxFitness),
        "bestIndividual" -> JsString(is.bestIndividual)
      )

    def read(value: JsValue) = value match {
      case _ => deserializationError("EvolutionIterationStatistics can't be deserialized!")
    }
  }

}