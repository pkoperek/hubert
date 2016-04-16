package pl.edu.agh.hubert

import pl.edu.agh.hubert.datasets.DataSet

package object testfixtures {

  def dataSet(path: String, variables: Set[String]): DataSet = {
    new DataSet(resolvePath(path), variables)
  }

  def resolvePath(fileName: String): String = {
    getClass.getResource("/csvloader/" + fileName).getPath
  }

}
