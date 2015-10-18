package pl.edu.agh.hubert.datasets

import pl.edu.agh.hubert.engine.Input

class LoadedDataSet private(
                             val raw: Input,
                             val nameIdx: Map[String, Int],
                             val differentiated: Input
                             ) {

  val rawSize: Int = if (raw.length > 0) raw(0).length else 0

  def this(raw: Input, nameIdx: Map[String, Int]) {
    this(raw, nameIdx, raw.map(rawValues => differentiate(rawValues)))
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
      return Some(differentiated(index.get))
    }

    None
  }

  def subset(indices: Array[Int]): LoadedDataSet = {
    println("Subset with indices: " + indices.mkString(",") + " size: " +  rawSize)

    new LoadedDataSet(
      raw.map(rawSerie => indices.map(index => rawSerie(index))),
      nameIdx,
      differentiated.map(differentiatedSerie => indices.map(index => differentiatedSerie(index)))
    )
  }

}