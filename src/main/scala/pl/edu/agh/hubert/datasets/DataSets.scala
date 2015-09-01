package pl.edu.agh.hubert.datasets

import java.io.File

import scala.collection.mutable.ArrayBuffer
import scala.io.{BufferedSource, Source}
import scalax.file.Path

object DataSets {

  private val storage = ArrayBuffer[DataSet]()

  private def addDataSet(dataSet: DataSet): Unit = {
    storage += dataSet
  }

  def clear(): Unit = {
    storage.clear()
  }

  def addFromPath(path: String): Unit = {
    val datasetDirectory: File = new File(path)

    if (!datasetDirectory.isDirectory) {
      throw new IllegalArgumentException("Path " + path + " is not a directory!")
    }

    val csvFiles = datasetDirectory.listFiles.filter(x => x.getName.endsWith(".csv"))

    for (csvFile <- csvFiles) {
      val maybeDataSet = translateToDataSet(csvFile)
      if (maybeDataSet.isDefined) {
        addDataSet(maybeDataSet.get)
      }
    }
  }

  private def translateToDataSet(csvFile: File): Option[DataSet] = {
    val line = firstLine(csvFile)

    if (line.isDefined) {
      if (line.get.startsWith("#")) {
        val variables = line.get.replaceAll("#", "").replaceAll(" ", "").split(",")
        return Some(new DataSet(csvFile.getAbsolutePath, variables.toSet))
      } else {
        return Some(new DataSet(csvFile.getAbsolutePath, Set()))
      }
    }

    None
  }

  private def firstLine(csvFile: File): Option[String] = {
    val source = Source.fromFile(csvFile)

    try {
      val lineIterator = source.getLines().take(1)
      if (lineIterator.hasNext) {
        Some(lineIterator.next())
      } else {
        None
      }
    }
    finally {
      source.close()
    }
  }

  def dataSets = storage.toArray

}
