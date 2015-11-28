package pl.edu.agh.hubert

import scala.collection.mutable.ArrayBuffer

package object datasets {

  def differentiate(serie: Array[Double]): Array[Double] = {
    val differentiated = ArrayBuffer[Double]()
    for (idx <- 1 to serie.length - 1) {
      differentiated += serie(idx) - serie(idx - 1)
    }

    differentiated.toArray
  }

}
