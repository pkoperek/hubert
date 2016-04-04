package pl.edu.agh.hubert.datasets

import pl.edu.agh.hubert.engine.Input

class LoadedDataSet private(
                             val raw: Input,
                             val nameIdx: Map[String, Int],
                             val seriesOfDifferences: Input
                           ) {

  val rawSize: Int = if (raw.length > 0) raw(0).length else 0
  val differencesSize: Int = rawSize - 1

  def this(raw: Input, nameIdx: Map[String, Int]) {
    this(raw, nameIdx, raw.map(rawValues => differences(rawValues)))
  }

  def rawByName(name: String): Option[Array[Double]] = {
    val index = nameIdx.get(name)

    if (index.isDefined) {
      return Some(raw(index.get))
    }

    None
  }

  def differentiatedByName(name: String): Option[Array[Double]] = {
    val index = nameIdx.get(name)

    if (index.isDefined) {
      return Some(seriesOfDifferences(index.get))
    }

    None
  }

  def subset(indices: Array[Int]): LoadedDataSet = {
    new LoadedDataSet(
      raw.map(rawSerie => indices.map(index => rawSerie(index))),
      nameIdx,
      seriesOfDifferences.map(differencesOfSerie => indices.map(index => differencesOfSerie(index)))
    )
  }

  def hasTime(): Boolean = {
    nameIdx.contains("t")
  }

  def variables(): Set[String] = {
    nameIdx.keySet
  }

  def timeVariableName(): String = "t"

  def indexOf(variable: String): Option[Int] = {
    nameIdx.get(variable)
  }
}