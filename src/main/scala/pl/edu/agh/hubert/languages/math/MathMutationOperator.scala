package pl.edu.agh.hubert.languages.math

import pl.edu.agh.hubert.engine.{Individual, IndividualGenerator, LanguageWord, MutationOperator}

import scala.util.Random

class MathMutationOperator(
                            mutationProbability: Double,
                            maxHeight: Int,
                            individualGenerator: IndividualGenerator
                            ) extends MutationOperator {

  override def mutate(individual: Individual): Individual = {
    val mathIndividual = individual.asInstanceOf[MathIndividual]
    new MathIndividual(mutate(mathIndividual.tree, 0))
  }

  private def mutate(root: LanguageWord, depth: Int): LanguageWord = {
    if (Random.nextDouble() < mutationProbability) {
      individualGenerator.generateGenome(maxHeight - depth)
    } else {

      root match {
        case plus: Plus => new Plus(mutate(plus.leftWord, depth + 1), mutate(plus.rightWord, depth + 1))
        case minus: Minus => new Plus(mutate(minus.leftWord, depth + 1), mutate(minus.rightWord, depth + 1))
        case mul: Mul => new Plus(mutate(mul.leftWord, depth + 1), mutate(mul.rightWord, depth + 1))
        case sin: Sin => new Sin(mutate(sin.internalWord, depth + 1))
        case cos: Cos => new Cos(mutate(cos.internalWord, depth + 1))
        case word: LanguageWord => word
      }
    }
  }
}