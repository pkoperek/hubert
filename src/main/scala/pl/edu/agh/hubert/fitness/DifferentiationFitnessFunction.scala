package pl.edu.agh.hubert.fitness

import pl.edu.agh.hubert.languages.LanguageWord
import pl.edu.agh.hubert.{MathIndividual, Individual}
import pl.edu.agh.hubert.datasets.CSVLoader
import pl.edu.agh.hubert.experiments.Experiment

class DifferentiationFitnessFunction(val experiment: Experiment) extends FitnessFunction {

  private val loadedDataSet = CSVLoader.load(experiment.dataSet)
  private val pairings = loadedDataSet.raw.indices.combinations(2).toArray.map(c => (c.seq(0), c.seq(1)))

  override def evaluateIndividual(individual: Individual): Option[Double] = {
    evaluateIndividual(individual.asInstanceOf[MathIndividual])
  }

  private def evaluateIndividual(individual: MathIndividual): Option[Double] = {

    val N = loadedDataSet.size

    val pairingErrors = pairings.par.map(pairing => {
      val x = pairing._1
      val y = pairing._2

      val dx: LanguageWord = individual.differentiatedBy(x)
      val dy: LanguageWord = individual.differentiatedBy(y)

      val dx_sym = dx.evaluateInput(loadedDataSet.raw)
      val dy_sym = dy.evaluateInput(loadedDataSet.raw)

      val dx_num = loadedDataSet.differentiated(x)
      val dy_num = loadedDataSet.differentiated(y)

      val filtered = dx_sym.zip(dy_sym).zip(dx_num).zip(dy_num)
        .filter(r => r._1._1._2 > 0 && r._2 > 0)

      if (filtered.length == 0) {
        None
      } else {
        // -N - the same as multiplyiing by -1 at the beginning
        val pairingError = filtered
          //          .par
          .map(r => (r._1._1._1 / r._1._1._2) - (r._1._2 / r._2))
          .map(x => Math.log(1 + (if (x > 0) x else -x)))
          .sum / -N

        Some(pairingError)
      }
    })
      .seq
      .filter(o => o.isDefined)
      .map(o => o.get)

    if (pairingErrors.nonEmpty) {
      Some(pairingErrors.min)
    } else {
      None
    }
  }

}