package org.cesarb

import java.io.File
import Extensions.StatsMap

object Main {
  val usage =
    """
      |usage: sensor [DIRECTORY]
      |""".stripMargin

  private def formatHeader(numFilesProc: Int, numMeasurementsProc: Int,
                   numFailedMeasurements: Int): String =
    s"""|Num of processed files: $numFilesProc
      |Num of processed measurements: $numMeasurementsProc
      |Num of failed measurements: $numFailedMeasurements
      |
      |Sensors with highest avg humidity:
      |
      |sensor-id,min,avg,max
      |""".stripMargin

  def showOutput(result: (Int, MeasurementBatch)): Unit = {
    val (numFilesProcessed, batch) = result
    print(formatHeader(numFilesProcessed, batch.numProcessed, batch.numFailures))
    print(batch.statsByAvg.display)
  }

  def main(args: Array[String]): Unit = {
    if (args.isEmpty) {
      println(usage)
      sys.exit(1)
    }

    val inputPath = new File(args(0))
    if (!inputPath.exists) {
      println(s"'${inputPath.getCanonicalPath}' does not exist in your file system.")
      sys.exit(1)
    }

    val result = MeasurementBatch.fromDir(inputPath)
    showOutput(result)
  }
}