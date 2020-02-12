package org.cesarb

import java.io.File

case class Stats(min: Int, avg: Int, max: Int)

object Stats {

  def processFile(file: File): Map[Int, Stats] = ???

}