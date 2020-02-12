package org.cesarb

case class Measurement(sensor: Int, humidity: Option[Int])

sealed abstract class ParsingError
case class InvalidFormat(failingLine: String) extends ParsingError

object Measurement {
  private val linePattern = "^^s(\\d+)\\,([0-9][0-9]?|100|NaN)$".r

  def parse(line: String): Either[ParsingError, Measurement] = line match {
    case linePattern(id, hum) => Right(Measurement(
      id.toInt,
      if (hum == "NaN") None else Some(hum.toInt)
    ))
    case _ => Left(InvalidFormat(line))
  }
}
