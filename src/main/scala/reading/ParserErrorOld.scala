package reading

sealed abstract class ParserError extends Throwable
case class InvalidReadingFormat(input: String) extends ParserError
case class SensorIdPrefixError(input: String) extends ParserError
case class InvalidIdNumber(input: String) extends ParserError
case class HumidityOutOfBounds(value: Int) extends ParserError
case class InvalidHumidityFormat(input: String) extends ParserError

case class ParserErrorOld(message: String) extends RuntimeException(message)
