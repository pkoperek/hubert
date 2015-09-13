package pl.edu.agh.hubert.datasets

import pl.edu.agh.hubert.hubert.Input

import scala.collection.mutable.ArrayBuffer

class LoadedDataSet(val raw: Input, val nameIdx: Map[String, Int]) {

  val differentiated: Input = raw.map(rawValues => differentiate(rawValues))

  val size: Int = if (raw.length > 0) raw(0).length else 0

  private def differentiate(serie: Array[Double]): Array[Double] = {
    val differentiated = ArrayBuffer[Double]()
    for (idx <- 1 to serie.length - 1) {
      differentiated += serie(idx) - serie(idx - 1)
    }

    differentiated.toArray
  }

  def rawByName(name: String): Option[Array[Double]] = {
    val index = nameIdx.get(name)

    if (index.isDefined) {
      return Some(raw(index.get))
    }

    None
  }

  def differentiatedByName(name: String): Option[Array[Double]] = {
    val index = nameIdx.get(name)

    if (index.isDefined) {
      return Some(differentiated(index.get))
    }

    None
  }
}

