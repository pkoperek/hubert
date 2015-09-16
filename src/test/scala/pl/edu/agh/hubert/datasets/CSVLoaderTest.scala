package pl.edu.agh.hubert.datasets

import org.scalatest.{FunSuite, Matchers}

class CSVLoaderTest extends FunSuite with Matchers {

  test("load empty file") {
    assert(CSVLoader.load(new DataSet(resolvePath("empty.csv"), Set())).raw.isEmpty)
  }

  test("load sample file") {
    val loaded = CSVLoader.load(new DataSet(resolvePath("noSpaces.csv"), Set("varA", "varB", "varC")))

    loaded.raw.nonEmpty should be
    loaded.rawByName("varA").get should equal(Array(1.0, 4.0, 7.0))
    loaded.rawByName("varB").get should equal(Array(2.0, 5.0, 8.0))
    loaded.rawByName("varC").get should equal(Array(3.0, 6.0, 9.0))
  }

  test("load with engineering notation") {
    val loaded = CSVLoader.load(new DataSet(resolvePath("linesWithE.csv"), Set("x", "y")))

    loaded.raw.nonEmpty should be
    loaded.rawByName("x").get should equal(Array(1.7501908933647871e-002, 4.9880440460896436e+000, 6.2831853071795862e+000))
    loaded.rawByName("y").get should equal(Array(3.9993873820055290e+000, 1.0887093215587045e+000, 4.0000000000000000e+000))
  }

  test("load sample file with spaces") {
    val loaded = CSVLoader.load(new DataSet(resolvePath("withSpaces.csv"), Set("varA", "varB", "varC")))

    loaded.raw.nonEmpty should be
    loaded.rawByName("varA").get should equal(Array(1.0, 4.0, 7.0))
    loaded.rawByName("varB").get should equal(Array(2.0, 5.0, 8.0))
    loaded.rawByName("varC").get should equal(Array(3.0, 6.0, 9.0))
  }

  test("load only a single variable from a file") {
    val loaded = CSVLoader.load(new DataSet(resolvePath("withSpaces.csv"), Set("varB")))

    loaded.raw.nonEmpty should be
    loaded.rawByName("varB").get should equal(Array(2.0, 5.0, 8.0))
    loaded.rawByName("varA") should be(None)
    loaded.rawByName("varC") should be(None)
  }

  test("load file with just the description of data") {
    val loaded = CSVLoader.load(new DataSet(
      resolvePath("firstline.csv"),
      Set("varA", "varB", "varC")))

    assert(loaded.raw.nonEmpty)
    assert(loaded.rawByName("varA").get.isEmpty)
    assert(loaded.rawByName("varB").get.isEmpty)
    assert(loaded.rawByName("varC").get.isEmpty)
  }

  private def resolvePath(fileName: String): String = {
    getClass.getResource("/csvloader/" + fileName).getPath
  }
}
