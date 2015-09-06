package pl.edu.agh.hubert.datasets

import pl.edu.agh.hubert.hubert.Input

import scala.collection.mutable.ArrayBuffer

class LoadedDataSet(val raw: Input, val nameIdx: Map[String, Int]) {

  lazy val differentiated: Input = raw.map(rawValues => differentiate(rawValues))

  private def differentiate(raw: Array[Double]): Array[Double] = {
    val differentiated = ArrayBuffer[Double]()
    for (idx <- 1 to raw.length - 1) {
      differentiated += raw(idx) - raw(idx - 1)
    }

    differentiated.toArray
  }
  
  def rawByName(name: String): Option[Array[Double]] = {
    val index = nameIdx.get(name)
    
    if(index.isDefined) {
      return Some(raw(index.get)) 
    }
      
    None
  }

  def differentiatedByName(name: String): Option[Array[Double]] = {
    val index = nameIdx.get(name)
    
    if(index.isDefined) {
      return Some(differentiated(index.get))
    }
      
    None
  }
}

