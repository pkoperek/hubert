package pl.edu.agh.hubert.datasets

import java.io.File

import scala.collection.mutable.ArrayBuffer

object DataSets {

  private val storage = ArrayBuffer[DataSet]()

  private def addDataSet(dataSet: DataSet): Unit = {
    storage += dataSet
  }

  def addFromPath(path: String): Unit = {
    val datasetDirectory: File = new File(path)

    if (!datasetDirectory.isDirectory) {
      throw new IllegalArgumentException("Path " + path + " is not a directory!")
    }
  }

  def dataSets = storage.toArray

}
