package org.cesarb

import java.io.File
import scala.util.{Success, Try, Using}
import Extensions.MeasurementSeq
import scala.collection.immutable.ListMap

/**
 * Represents a partially processed measurement batch.
 * @param numProcessed the number of processed measurements in the batch.
 * @param numFailures the number of failed measurements in the batch.
 * @param sensorData collection of measurements by sensor ID.
 */
case class MeasurementBatch(numProcessed: Int, numFailures: Int,
                            sensorData: Map[String, List[Int]]) {

  /**
   * Merges two MeasurementBatch objects by adding the number of processed
   * measurements as well the number of failed ones and by merging the
   * measurements lists for the corresponding sensor IDs.
   * @param that
   * @return
   */
  def merge(that: MeasurementBatch): MeasurementBatch = MeasurementBatch(
    numProcessed = this.numProcessed + that.numProcessed,
    numFailures  = this.numFailures + that.numFailures,
    sensorData   = (sensorData.toSeq ++ that.sensorData)
      .groupMapReduce(_._1)(_._2)((a, b) => a.appendedAll(b))
  )

  /**
   * Provides statistical data, specifically minimum, average and
   * maximum, for this MeasurementBatch.
   * @return a Map from sensor IDs to their computed statistics.
   */
  def stats: Map[String, SensorStats] = sensorData.map {
    case (id, data) => id -> SensorStats(
      data.minByOption(identity),
      data.average,
      data.maxByOption(identity)
    )
  }

  def statsByAvg: Map[String, SensorStats] = {
    ListMap(stats.toSeq.sortWith{
      case ((k1, v1), (k2, v2)) => (v1.avg, v2.avg) match {
        case (Some(stats1), Some(stats2)) => stats1 > stats2
        case (Some(_), None) => true
        case (None, Some(_)) => false
      }
    }:_*)
  }
}

object MeasurementBatch {
  def empty: MeasurementBatch = MeasurementBatch(0, 0, Map())

  /**
   * Parses an iterator of strings assuming each element of the iterator
   * corresponds to a CSV line.
   * @param lines the iterator to be parsed.
   * @return a MeasurementBatch with the parsed data.
   */
  def parse(lines: Iterator[String]): MeasurementBatch = {
    val parsedLines = lines.map(Measurement.parse)

    parsedLines.foldLeft(MeasurementBatch(0, 0, Map())) {
      /*
      This represents the case where parsing was successful but
      measurement failed ("NaN").
       */
      case (stats,
        Right(Measurement(sensorId, None))) =>
          val list = stats.sensorData.getOrElse(sensorId, Nil)
          val newMap = stats.sensorData.updated(sensorId, list)
          MeasurementBatch(
            stats.numProcessed + 1,
            stats.numFailures + 1,
            newMap
          )

      /*
      This represents the case where parsing and measurement were successful.
       */
      case (stats,
        Right(Measurement(sensorId, Some(humidity)))) =>
          val currList = stats.sensorData.getOrElse(sensorId, Nil)
          val newList = currList :+ humidity
          val newMap = stats.sensorData.updated(sensorId, newList)
          MeasurementBatch(
            stats.numProcessed + 1,
            stats.numFailures,
            newMap
          )

      /*
      This represents parsing failures, which are just ignored.
       */
      case x => x._1
    }
  }

  /**
   * Parses a CSV file with the expected format into a Stats object.
   * @param file The source file.
   * @return Success with a Stats object or a Failure in case any
   *         file operations has failed.
   */
  def fromFile(file: File): Try[MeasurementBatch] =
    Using(io.Source.fromFile(file)){ source =>
      parse(source.getLines.drop(1))
    }

  /**
   * Reads and parses all files in a given directory.
   * @param path the directory to process.
   * @return a tuple containing the number of (successfully) processed files
   *         and a MeasurementBatch summarizing the data from all files.
   */
  def fromDir(path: File): (Int, MeasurementBatch) = {
    if (!path.isDirectory)
      (0, empty)
    else
      path.listFiles.map(fromFile).collect { case Success(batch) => batch }.foldLeft(0, empty) {
        case ((count, merged), current) => (count + 1, merged.merge(current))
      }
  }
}