package pl.edu.agh.hubert

import scala.collection.mutable.ArrayBuffer

package object datasets {

  def differences(serie: Array[Double]): Array[Double] = {
    // backward difference
    val differentiated = ArrayBuffer[Double]()
    // TODO: the central difference has actually better accuracy - add an option to use different formulas
    for (idx <- 1 to serie.length - 2) {
      differentiated += serie(idx) - serie(idx - 1)
    }

    differentiated.toArray
  }

  def dropFirstAndLast(serie: Array[Double]): Array[Double] = serie.drop(1).dropRight(1)

}
