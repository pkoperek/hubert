package pl.edu.agh.hubert

import scala.collection.mutable.ArrayBuffer

package object datasets {

  def differences(serie: Array[Double]): Array[Double] = {
    // central difference
    val differentiated = ArrayBuffer[Double]()
    for (idx <- 1 to serie.length - 2) {
      differentiated += serie(idx + 1) - serie(idx - 1)
    }

    differentiated.toArray
  }

  def dropFirstAndLast(serie: Array[Double]): Array[Double] = serie.drop(1).dropRight(1)

}
