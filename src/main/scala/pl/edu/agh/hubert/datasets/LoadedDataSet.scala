package pl.edu.agh.hubert.datasets

import scala.collection.mutable.ArrayBuffer

class LoadedDataSet(val raw: Map[String, Array[Double]]) {

  lazy val differentiated: Map[String, Array[Double]] = raw.mapValues(rawValues => differentiate(rawValues))

  private def differentiate(raw: Array[Double]): Array[Double] = {
    val differentiated = ArrayBuffer[Double]()
    for (idx <- 1 to raw.length - 1) {
      differentiated += raw(idx) - raw(idx - 1)
    }

    differentiated.toArray
  }
}

