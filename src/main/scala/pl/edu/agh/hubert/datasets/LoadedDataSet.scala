package pl.edu.agh.hubert.datasets

case class LoadedDataSet(
                          raw: Map[String, Array[Double]],
                          differentiated: Map[String, Array[Double]]
                          )

