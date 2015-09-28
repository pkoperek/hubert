package pl.edu.agh.hubert.engine

import pl.edu.agh.hubert.languages.math.MathRandomIndividualGenerator

import scala.util.Random

trait IndividualGenerator {

  def generateGenome(maxHeight: Int): LanguageWord
  def generateIndividual(): Individual

}

object IndividualGenerator {

  def apply(experiment: Experiment): IndividualGenerator = {
    experiment.language.name match {
      case "math" => return new MathRandomIndividualGenerator(
        experiment.language.words.toArray,
        experiment.maxHeight, 
        new Random(),
        experiment.dataSet.variables.size
      )
    }

    throw new RuntimeException("No individual generator specified!")
  }

}

