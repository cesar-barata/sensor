package org.cesarb

import java.io.File

case class Stats(min: Int, avg: Double, max: Int)

object Stats {
  implicit class StatsMap[K, V](map: Map[K, V]) {
    def combineKeyWith(key: K, optValue: Option[V], op: (V, V) => V): Map[K, V] =
      map.collect {
        case (currKey, currValue) if currKey == key =>
          currKey -> optValue
            .map(value => op(value, currValue))
            .getOrElse(currValue)
        case x => x
      }
  }

  def fromFile(file: File) = {
    val parsedLines = DataSource
      .iterateLinesNoHeader(file)
      .map(Measurement.parse)

    val initialMaps = (
      Map[Int, Int](), // min
      Map[Int, Int](), // sum
      Map[Int, Int]()  // max
    )

    parsedLines.foldLeft(initialMaps) {
      case ((min, sum, max), Right(Measurement(readSensorId, readHumidity))) => (
        min.combineKeyWith(readSensorId, readHumidity, math.min),
        sum.combineKeyWith(readSensorId, readHumidity, _ + _),
        max.combineKeyWith(readSensorId, readHumidity, math.max)
      )
      case _ => ???
    }
  }

}