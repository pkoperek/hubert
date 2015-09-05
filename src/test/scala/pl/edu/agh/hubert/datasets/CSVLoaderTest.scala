package pl.edu.agh.hubert.datasets

import org.scalatest.{FunSuite, Matchers}

class CSVLoaderTest extends FunSuite with Matchers {

  test("load empty file") {
    assert(CSVLoader.load(new DataSet(resolvePath("empty.csv"), Set())).isEmpty)
  }

  test("load sample file") {
    val loaded = CSVLoader.load(new DataSet(resolvePath("noSpaces.csv"), Set("varA", "varB", "varC")))

    loaded.nonEmpty should be
    loaded("varA") should equal (Array(1.0, 4.0, 7.0))
    loaded("varB") should equal (Array(2.0, 5.0, 8.0))
    loaded("varC") should equal (Array(3.0, 6.0, 9.0))
  }

  test("load sample file with spaces") {
    val loaded = CSVLoader.load(new DataSet(resolvePath("withSpaces.csv"), Set("varA", "varB", "varC")))

    loaded.nonEmpty should be
    loaded("varA") should equal (Array(1.0, 4.0, 7.0))
    loaded("varB") should equal (Array(2.0, 5.0, 8.0))
    loaded("varC") should equal (Array(3.0, 6.0, 9.0))
  }

  test("load only a single variable from a file") {
    val loaded = CSVLoader.load(new DataSet(resolvePath("withSpaces.csv"), Set("varB")))

    loaded.nonEmpty should be
    loaded("varB") should equal (Array(2.0, 5.0, 8.0))
    loaded should (not contain key ("varA") and not contain key ("varC"))
  }

  test("load file with just the description of data") {
    val loaded = CSVLoader.load(new DataSet(
      resolvePath("firstline.csv"),
      Set("varA", "varB", "varC")))

    assert(loaded.nonEmpty)
    assert(loaded("varA").isEmpty)
    assert(loaded("varB").isEmpty)
    assert(loaded("varC").isEmpty)
  }

  private def resolvePath(fileName: String): String = {
    getClass.getResource("/csvloader/" + fileName).getPath
  }
}
