package pl.edu.agh.hubert.datasets

import scala.collection.mutable.ArrayBuffer

object DataSets {

  private val storage = ArrayBuffer[DataSet]()

  def addDataSet(dataSet: DataSet): Unit = {
    storage += dataSet
  }

  def addFromPath(path: String): Unit = {
    // TODO: read all csv files from path, read the first line and treat is as a list of 
    // parameters, add new elements to storage

  }
  
  def dataSets = storage.toArray

}
