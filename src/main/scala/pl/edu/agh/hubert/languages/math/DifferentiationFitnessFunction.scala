package pl.edu.agh.hubert.languages.math

import pl.edu.agh.hubert.datasets.CSVLoader
import pl.edu.agh.hubert.engine._

class DifferentiationFitnessFunction(val experiment: Experiment) extends FitnessFunction {

  private val loadedDataSet = CSVLoader.load(experiment.dataSet)
  private val pairings = loadedDataSet.raw.indices.combinations(2).toArray.map(c => (c.seq(0), c.seq(1)))

  override def evaluatePopulation(toEvaluate: Array[Individual]): Array[EvaluatedIndividual] = {
    toEvaluate.map(individual => evaluateIndividual(individual))
  }

  private def evaluateIndividual(individual: Individual): EvaluatedIndividual =
    new EvaluatedIndividual(
      individual,
      evaluateFitness(individual)
    )

  private def evaluateFitness(individual: Individual): Option[Double] = {
    evaluateFitness(individual.asInstanceOf[MathIndividual])
  }

  private def evaluateFitness(individual: MathIndividual): Option[Double] = {

    val N = loadedDataSet.rawSize

    val pairingErrors = pairings.par.map(pairing => {
      val x = pairing._1
      val y = pairing._2

      val dx: LanguageWord = individual.differentiatedBy(x)
      val dy: LanguageWord = individual.differentiatedBy(y)

      val dx_sym = dx.evaluateInput(loadedDataSet.raw)
      val dy_sym = dy.evaluateInput(loadedDataSet.raw)

      val dx_num = loadedDataSet.seriesOfDifferences(x)
      val dy_num = loadedDataSet.seriesOfDifferences(y)

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