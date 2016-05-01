package pl.edu.agh.hubert.engine

class Input(
             val series: Array[Array[Double]]
           ) {

  val dataPointsCount: Int = if (variablesCount > 0) series(0).length else 0

  def variablesCount = series.size

  def isEmpty = series.isEmpty

  def nonEmpty = series.nonEmpty

  def serie(idx: Int) = series(idx)

}