package pl.edu.agh.hubert.datasets

import java.io.File
import java.nio.file.{Files, Path}

import org.scalatest.{BeforeAndAfter, FunSuite}

import scala.collection.mutable
import scalax.io.Resource

class DataSetsTest extends FunSuite with BeforeAndAfter {

  var directory: Path = null

  before {
    DataSets.clear()

    directory = createTempDirectory
  }

  after {
    deleteRecursively()
  }

  test("should throw IllegalArgumentException if path not a directory") {
    val file = File.createTempFile("temp", ".csv")
    file.deleteOnExit()

    intercept[IllegalArgumentException] {
      DataSets.addFromPath(file.getAbsolutePath)
    }
  }

  test("should create the data set if the file doesn't contain the variables line") {
    val temporaryFileWithVariables: File = temporaryFile("test1.csv")
    Resource.fromFile(temporaryFileWithVariables).writeStrings(Array(
      "1,2,3"
    ))

    DataSets.addFromPath(directory.toFile.getAbsolutePath)

    assert(DataSets.dataSets.nonEmpty)
    val dataSet = DataSets.dataSets(0)

    assert(dataSet.path == temporaryFileWithVariables.getAbsolutePath)
    assert(dataSet.variables.isEmpty)
  }

  test("should ignore the file if it is empty") {
    temporaryFile("test1.csv")

    DataSets.addFromPath(directory.toFile.getAbsolutePath)

    assert(DataSets.dataSets.isEmpty)
  }

  test("should read first line from csv and parse variable names") {
    val temporaryFileWithVariables: File = temporaryFile("test1.csv")
    writeCorrectContent(temporaryFileWithVariables)

    DataSets.addFromPath(directory.toFile.getAbsolutePath)
    val dataSet = DataSets.dataSets(0)

    assert(dataSet.variables.contains("var1"))
    assert(dataSet.variables.contains("var2"))
    assert(dataSet.variables.contains("var3"))
    assert(dataSet.variables.size == 3)
  }

  test("should ignore spaces in the first line") {
    val temporaryFileWithVariables: File = temporaryFile("test1.csv")
    writeContent(temporaryFileWithVariables, Array(
      "#     var1    , var2   ,   var3   ",
      "1,2,3"
    ))

    DataSets.addFromPath(directory.toFile.getAbsolutePath)
    val dataSet = DataSets.dataSets(0)

    assert(dataSet.variables.contains("var1"))
    assert(dataSet.variables.contains("var2"))
    assert(dataSet.variables.contains("var3"))
    assert(dataSet.variables.size == 3)
  }

  test("should add datasets for all files in directory") {
    writeCorrectContent(temporaryFile("test1.csv"))
    writeCorrectContent(temporaryFile("test2.csv"))
    writeCorrectContent(temporaryFile("test3.csv"))

    DataSets.addFromPath(directory.toFile.getAbsolutePath)
    assert(DataSets.dataSets.size == 3)
  }

  private def deleteRecursively(): Unit = {
    scalax.file.Path(directory.toFile).deleteRecursively()
  }

  private def createTempDirectory = {
    val directory = Files.createTempDirectory("hubert_temp." + System.currentTimeMillis())
    directory.toFile.mkdirs()
    directory
  }

  private def temporaryFile(filename: String): File = {
    val tempFile = new File(directory.toFile, filename)
    tempFile.createNewFile()
    tempFile
  }

  private def writeCorrectContent(file: File): Unit = {
    writeContent(file, Array(
      "#var1,var2,var3",
      "1,2,3"
    ))
  }

  private def writeContent(file: File, content: Array[String]): Unit = {
    Resource.fromFile(file).writeStrings(content, separator = "\n")
  }
}
