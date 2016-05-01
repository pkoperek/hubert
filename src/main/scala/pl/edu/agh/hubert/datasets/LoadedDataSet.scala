package pl.edu.agh.hubert.datasets

import pl.edu.agh.hubert.engine.Input

class LoadedDataSet private(
                             val raw: Input,
                             val rawAlignedToDifferences: Input,
                             val seriesOfDifferences: Input,
                             val nameIdx: Map[String, Int]
                           ) {

  val rawSize: Int = raw.dataPointsCount
  val differencesSize: Int = rawSize - 2

  def this(raw: Input, nameIdx: Map[String, Int]) {
    this(
      raw,
      new Input(raw.series.map(dropFirstAndLast)),
      new Input(raw.series.map(differences)),
      nameIdx
    )
  }

  def rawByName(name: String): Option[Array[Double]] = {
    val index = nameIdx.get(name)

    if (index.isDefined) {
      return Some(raw.serie(index.get))
    }

    None
  }

  def differentiatedByName(name: String): Option[Array[Double]] = {
    val index = nameIdx.get(name)

    if (index.isDefined) {
      return Some(seriesOfDifferences.serie(index.get))
    }

    None
  }

  def subset(indices: Array[Int]): LoadedDataSet = {
    new LoadedDataSet(
      new Input(raw.series.map(rawSerie => indices.map(index => rawSerie(index)))),
      new Input(rawAlignedToDifferences.series.map(serie => indices.map(index => serie(index)))),
      new Input(seriesOfDifferences.series.map(differencesOfSerie => indices.map(index => differencesOfSerie(index)))),
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