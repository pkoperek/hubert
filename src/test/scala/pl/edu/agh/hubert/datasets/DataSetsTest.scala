package pl.edu.agh.hubert.datasets

import java.io.File
import java.nio.file.{Files, Path}

import org.scalatest.FunSuite

class DataSetsTest extends FunSuite {

  test("should throw IllegalArgumentException if path not a directory") {
    val file = temporaryFile

    intercept[IllegalArgumentException] {
      DataSets.addFromPath(file.getAbsolutePath)
    }
  }

  test("should add datasets for all files in directory") {
    val directory = createTempDirectory
    temporaryFile(directory, "test1.csv")
    temporaryFile(directory, "test2.csv")
    temporaryFile(directory, "test3.csv")

    DataSets.addFromPath(directory.toFile.getAbsolutePath)
    assert(DataSets.dataSets.size == 3)

    deleteRecursively(directory)
  }

  private def deleteRecursively(directory: Path): Unit = {
    scalax.file.Path(directory.toFile).deleteRecursively()
  }

  private def createTempDirectory = {
    val directory = Files.createTempDirectory("hubert_temp." + System.currentTimeMillis())
    directory.toFile.mkdirs()
    directory
  }

  private def temporaryFile(directory: Path, filename: String): File = {
    val tempFile = new File(directory.toFile, filename)
    tempFile.createNewFile()
    tempFile
  }

  private def temporaryFile: File = {
    val file = File.createTempFile("temp", ".csv")
    file.deleteOnExit()
    file
  }
}
