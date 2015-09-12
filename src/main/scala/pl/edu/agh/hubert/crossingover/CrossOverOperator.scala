package pl.edu.agh.hubert.crossingover

import pl.edu.agh.hubert.languages._
import pl.edu.agh.hubert.{MathIndividual, Individual}
import pl.edu.agh.hubert.experiments.Experiment

import scala.collection.mutable.ArrayBuffer
import scala.util.Random

trait CrossOverOperator {

  def crossOver(left: Individual, right: Individual): (Individual, Individual)

}

object CrossOverOperator {

  def apply(experiment: Experiment): CrossOverOperator = {
    experiment.language.name match {
      case "math" => return new MathCrossOverOperator(experiment.crossOverProbability)
    }

    throw new IllegalArgumentException("Unknown language: " + experiment.language.name)
  }

}

class MathCrossOverOperator(crossOverProbability: Double) extends CrossOverOperator {

  override def crossOver(left: Individual, right: Individual): (Individual, Individual) = {
    val mathLeft = left.asInstanceOf[MathIndividual]
    val mathRight = right.asInstanceOf[MathIndividual]

    if (Random.nextDouble() < crossOverProbability) {
      val leftCrossOverPoint = (Random.nextDouble() * mathLeft.size).round
      val rightCrossOverPoint = (Random.nextDouble() * mathRight.size).round

      val leftCut = new SubtreeBuffer()
      val rightCut = new SubtreeBuffer()

      findSubtreeToCut(leftCrossOverPoint, mathLeft.tree, leftCut)
      findSubtreeToCut(rightCrossOverPoint, mathRight.tree, rightCut)

      val leftCrossed = mix(mathLeft.tree, leftCut.tree, rightCut.tree)
      val rightCrossed = mix(mathRight.tree, rightCut.tree, leftCut.tree)

      (new MathIndividual(leftCrossed), new MathIndividual(rightCrossed))
    }

    (left, right)
  }

  private def findSubtreeToCut(
                                pointToFind: Long,
                                tree: LanguageWord,
                                subtreeToCutBuffer: SubtreeBuffer,
                                currentPoint: Long = 0): Long = {

    if (currentPoint > pointToFind) {
      return currentPoint
    }

    if (pointToFind == currentPoint) {
      subtreeToCutBuffer.tree = tree
      return pointToFind
    }

    val highestChild = tree match {
      case sin: Sin => findSubtreeToCut(pointToFind, sin.internalWord, subtreeToCutBuffer, currentPoint + 1)
      case cos: Cos => findSubtreeToCut(pointToFind, cos.internalWord, subtreeToCutBuffer, currentPoint + 1)
      case plus: Plus => {
        val leftIdx = findSubtreeToCut(pointToFind, plus.leftWord, subtreeToCutBuffer, currentPoint + 1)
        val rightIdx = findSubtreeToCut(pointToFind, plus.rightWord, subtreeToCutBuffer, leftIdx + 1)
        rightIdx
      }
      case minus: Minus => {
        val leftIdx = findSubtreeToCut(pointToFind, minus.leftWord, subtreeToCutBuffer, currentPoint + 1)
        val rightIdx = findSubtreeToCut(pointToFind, minus.rightWord, subtreeToCutBuffer, leftIdx + 1)
        rightIdx
      }
      case mul: Mul => {
        val leftIdx = findSubtreeToCut(pointToFind, mul.leftWord, subtreeToCutBuffer, currentPoint + 1)
        val rightIdx = findSubtreeToCut(pointToFind, mul.rightWord, subtreeToCutBuffer, leftIdx + 1)
        rightIdx
      }
      case _ => currentPoint
    }

    highestChild
  }

  private def mix(tree: LanguageWord, toFind: LanguageWord, toReplace: LanguageWord): LanguageWord = {
    if (toFind.eq(toReplace)) {
      return toReplace
    }

    val treeCopy = tree match {
      case sin: Sin => new Sin(mix(sin.internalWord, toFind, toReplace))
      case cos: Cos => new Cos(mix(cos.internalWord, toFind, toReplace))
      case plus: Plus => new Plus(mix(plus.leftWord, toFind, toReplace), mix(plus.rightWord, toFind, toReplace))
      case minus: Minus => new Plus(mix(minus.leftWord, toFind, toReplace), mix(minus.rightWord, toFind, toReplace))
      case mul: Mul => new Plus(mix(mul.leftWord, toFind, toReplace), mix(mul.rightWord, toFind, toReplace))
      case word: LanguageWord => word
    }

    treeCopy
  }

  private class SubtreeBuffer(var tree: LanguageWord = null)

}