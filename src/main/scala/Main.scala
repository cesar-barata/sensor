import scala.util.{Failure, Try}

case class Reading(sensorId: Int, humidity: Int)
case class ParserError(message: String) extends RuntimeException(message)

object Reading {
  def parseSensorId(source: String): Try[Int] = if (source(0) == 's') {
      Try { source.substring(1, source.length()).toInt }
        .recoverWith {
          case e: NumberFormatException => Failure(ParserError(s"Couldn't parse an integer for sensor ID from string '${source}'"))
        }
  } else {
    Failure(ParserError(s"String '${source}' does not start with an 's'"))
  }

  def parseHumidity(source: String): Try[Int] = Try { source.toInt }
    .filter(value => 0 <= value && value <= 100)
    .recoverWith {
      case e: NumberFormatException => Failure(ParserError(s"Couldn't parse an integer for humidity from string '${source}'"))
      case e: NoSuchElementException => Failure(ParserError(s"Couldn't match integer between 0 and 100 inclusive on string '${source}''"))
    }

  val separator = ','
  def parse(source: String): Try[Reading] = {
    source.split(separator) match {
      case Array(idString, humidityString) => for {
        sensorId <- parseSensorId(idString)
        humidity <- parseHumidity(humidityString)
      } yield Reading(sensorId, humidity)
      case _ => Failure(ParserError(s"Couldn't match string '${source}' with reading format: s<integer>,<integer>"))
    }
  }
}

object Main {
  def main(args: Array[String]): Unit = {
    println(Reading.parse("sa,100"))
  }
}