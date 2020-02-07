package reading

case class ParserError(message: String) extends RuntimeException(message)
