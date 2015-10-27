package pl.edu.agh

import scala.reflect.ClassTag
import scala.util.Random

/**
 * Created by pkoperek on 10/13/15.
 */
package object hubert {

  def randomPairs[T: ClassTag](toGroup: Array[T]): Array[(T, T)] = {
    Random.shuffle(toGroup.toIterator).grouped(2).map(array => (array.head, array(1))).toArray
  }

  def flat[T: ClassTag](toFlat: Array[(T,T)]): Array[T] = {
    toFlat.flatMap( pair => Array(pair._1, pair._2))
  }

}