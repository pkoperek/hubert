package pl.edu.agh.hubert.engine

import org.scalatest.FunSuite
import pl.edu.agh.hubert.datasets.DataSet
import pl.edu.agh.hubert.experiments.Experiment
import pl.edu.agh.hubert.languages.Languages

class DefaultEvolutionEngineTest extends FunSuite {

  test("should execute experiment with math language") {
    val experiment = Experiment(
      1,
      "exp1",
      "desc1",
      10,
      Languages.mathLanguage(),
      new DataSet("/tmp/path", Set("varA", "varB")),
      7)

    try {
      new DefaultEvolutionEngine(experiment).evolve()
    } catch {
      case e: Throwable => fail("Exception thrown! " + e)
    }
  }

}
