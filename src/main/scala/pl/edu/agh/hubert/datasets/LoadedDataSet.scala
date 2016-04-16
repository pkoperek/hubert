package pl.edu.agh.hubert.datasets

import pl.edu.agh.hubert.engine.Input

class LoadedDataSet private(
                             val raw: Input,
                             val rawAlignedToDifferences: Input,
                             val seriesOfDifferences: Input,
                             val nameIdx: Map[String, Int]
                           ) {

  val rawSize: Int = if (raw.length > 0) raw(0).length else 0
  val differencesSize: Int = rawSize - 2

  def this(raw: Input, nameIdx: Map[String, Int]) {
    this(
      raw,
      raw.map(rawValues => dropFirstAndLast(rawValues)),
      raw.map(rawValues => differences(rawValues)),
      nameIdx
    )
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
      rawAlignedToDifferences.map(serie => indices.map(index => serie(index))),
      seriesOfDifferences.map(differencesOfSerie => indices.map(index => differencesOfSerie(index))),
      nameIdx
    )
  }

  def hasTime(): Boolean = {
    nameIdx.contains("t")
  }

  def variables(): Set[String] = {
    nameIdx.keySet
  }

  def timeVariableName(): String = "t"

  def timeVariableIndex(): Option[Int] = indexOf(timeVariableName())

  def indexOf(variable: String): Option[Int] = {
    nameIdx.get(variable)
  }
}