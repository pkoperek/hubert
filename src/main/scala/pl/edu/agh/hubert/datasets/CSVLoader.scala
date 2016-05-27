package pl.edu.agh.hubert.datasets

import pl.edu.agh.hubert.engine.Input

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer
import scala.io.Source

object CSVLoader {

  def load(dataSet: DataSet): LoadedDataSet = {
    val source = Source.fromFile(dataSet.path)

    val buffers = dataSet.variables.map(v => (v, ArrayBuffer[Double]())).toMap

    try {
      val sourceLines = source.getLines()
      if (sourceLines.hasNext) {
        val firstLine = sourceLines.next()
        if (firstLine.startsWith("#")) {
          val indexes = firstLine
            .replaceAll("#", "")
            .replaceAll(" ", "")
            .split(",")
            .zipWithIndex
            .filter(varWithIdx => dataSet.variables.contains(varWithIdx._1))
            .toMap

          for (line <- sourceLines) {
            if (!line.startsWith("#")) {
              val lineValues = line.split(",").map(x => x.toDouble)

              for (index <- indexes) {
                buffers(index._1) += lineValues(index._2)
              }
            }
          }
        }
      }
    } finally {
      source.close()
    }

    toLoadedDataSet(buffers)
  }

  private def toLoadedDataSet(buffers: Map[String, ArrayBuffer[Double]]): LoadedDataSet = {
    val seriesBuffer = ArrayBuffer[Array[Double]]()
    val namesToIdx = mutable.Map[String, Int]()
    for (variableSerie <- buffers) {
      seriesBuffer += variableSerie._2.toArray
      namesToIdx += (variableSerie._1 -> (seriesBuffer.size - 1))
    }

    new LoadedDataSet(new Input(seriesBuffer.toArray), namesToIdx.toMap)
  }
}
