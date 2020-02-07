package reading

import scala.util.{Failure, Try}

case class Reading(sensorId: Int, humidity: Int)

object Reading {
  def parseSensorId(source: String): Try[Int] = if (source(0) == 's') {
    Try {
      source.substring(1, source.length()).toInt
    }
      .recoverWith {
        case e: NumberFormatException => Failure(
          ParserErrorOld(s"Couldn't parse an integer for sensor ID from " +
            s"string '${source}'")
        )
      }
  } else {
    Failure(ParserErrorOld(s"String '${source}' does not start with an 's'"))
  }

  def parseHumidity(source: String): Try[Int] = Try(source.toInt)
    .filter(value => 0 <= value && value <= 100)
    .recoverWith {
      case e: NumberFormatException => Failure(
        ParserErrorOld(s"Couldn't parse an integer for humidity from string " +
          s"'${source}'")
      )
      case e: NoSuchElementException => Failure(
        ParserErrorOld(s"Couldn't match integer between 0 and 100 inclusive " +
          s"on string '${source}''")
      )
    }

  val separator = ','
  def parse(source: String): Try[Reading] = {
    source.split(separator) match {
      case Array(idString, humidityString) => for {
        sensorId <- parseSensorId(idString)
        humidity <- parseHumidity(humidityString)
      } yield Reading(sensorId, humidity)
      case _ => Failure(
        ParserErrorOld(s"Couldn't match string '${source}' with reading " +
          s"format: s<integer>,<integer>")
      )
    }
  }
}