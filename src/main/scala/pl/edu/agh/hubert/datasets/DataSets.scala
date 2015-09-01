package pl.edu.agh.hubert.datasets

import java.io.File

import scala.collection.mutable.ArrayBuffer
import scalax.file.Path

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
    
    val csvFiles = datasetDirectory.listFiles.filter(x => x.getName.endsWith(".csv"))
    
    for(csvFile <- csvFiles) {
      addDataSet(new DataSet(csvFile.getAbsolutePath, Set()))
    }
  }

  def dataSets = storage.toArray

}
