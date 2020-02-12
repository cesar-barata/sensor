package org.cesarb

import java.io.File
import scala.io.Source._

object DataSource {

  def readAllInDir(file: File, suffix: String): Iterator[File] =
    file.listFiles
      .iterator
      .filter(_.getName.endsWith(suffix))

  def iterateLines(file: File): Iterator[String] = fromFile(file).getLines

  def iterateLinesNoHeader(file: File): Iterator[String] = iterateLines(file).drop(1)

}