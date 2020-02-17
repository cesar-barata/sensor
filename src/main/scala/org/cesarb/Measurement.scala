package org.cesarb

/**
 * Represents a measurement from a input file.
 * @param sensor the ID for the read sensor.
 * @param humidity the humidity measurement of the sensor.
 */
case class Measurement(sensor: String, humidity: Option[Int])

/**
 * A base class for parsing errors.
 */
sealed abstract class ParsingError

/**
 * Represents a syntatically invalid measurement reading format.
 * @param failingLine the faulty line.
 */
case class InvalidFormat(failingLine: String) extends ParsingError

object Measurement {
  /**
   * A valid measurement line the format:
   *
   * s<integer>,<integer in the interval [0,100]>
   *
   * The first integer represents the sensor ID, while the second
   * represents the humidity measurement.
   * The following pattern accepts any string as sensor ID.
   */
  private val linePattern = "^(.+)\\,([0-9][0-9]?|100|NaN)$".r

  /**
   * Parses a single line of the input file.
   * @param line the line to be parsed.
   * @return Right - representing either a successful or failed measurement.
   *         Left - representing a invalid line format in the input file.
   */
  def parse(line: String): Either[ParsingError, Measurement] = line match {
    case linePattern(id, hum) => Right(Measurement(
      id,
      if (hum == "NaN") None else Some(hum.toInt)
    ))
    case _ => Left(InvalidFormat(line))
  }
}
