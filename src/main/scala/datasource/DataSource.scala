package datasource

import java.io.File
import io.Source._

object DataSource {

  def readAllInDir(file: File, suffix: String) =
    file.listFiles
      .iterator
      .filter(_.getName.endsWith(suffix))

  def iterateLines(file: File): Iterator[String] = fromFile(file).getLines

}