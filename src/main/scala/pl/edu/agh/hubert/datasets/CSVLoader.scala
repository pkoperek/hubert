package pl.edu.agh.hubert.datasets

import scala.collection.mutable.ArrayBuffer
import scala.io.{BufferedSource, Source}

object CSVLoader {

  def load(dataSet: DataSet): Map[String, Array[Double]] = {
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

    buffers.mapValues[Array[Double]](v => v.toArray)
  }

}
