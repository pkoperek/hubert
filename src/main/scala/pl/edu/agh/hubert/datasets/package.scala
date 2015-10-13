package pl.edu.agh.hubert

import scala.collection.mutable.ArrayBuffer

/**
 * Created by pkoperek on 10/13/15.
 */
package object datasets {

  def differentiate(serie: Array[Double]): Array[Double] = {
    val differentiated = ArrayBuffer[Double]()
    for (idx <- 1 to serie.length - 1) {
      differentiated += serie(idx) - serie(idx - 1)
    }

    differentiated.toArray
  }

}
