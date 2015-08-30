package pl.edu.agh.hubert.datasets

import java.io.File
import java.nio.file.{StandardCopyOption, Path, Files}

import org.scalatest.FunSuite

class DataSetsTest extends FunSuite {

  test("should throw IllegalArgumentException if path not a directory") {
    val file = temporaryFile

    intercept[IllegalArgumentException] {
      DataSets.addFromPath(file.getAbsolutePath)
    }
  }
  
  test("should add datasets for all files in directory") {
    fail("implement me")
//    val directory = Files.createTempDirectory("tmpdir")
//    directory.toAbsolutePath.toFile.mkdirs()

//    Files.move(, directory, StandardCopyOption.REPLACE_EXISTING);
    
  }

  def temporaryFile: File = {
    val file = File.createTempFile("temp", ".csv")
    file.deleteOnExit()
    file
  }
}
