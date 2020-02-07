import org.scalatest.FlatSpec
import reading.{ParserError, Reading}

import scala.util.{Failure, Success}

class ReadingParserSpec extends FlatSpec {
  "parseSensorId" should "return an integer wrapped in a Success" in {
    val expected = Success(23)
    val actual = Reading.parseSensorId("s23")
    assert(expected == actual)
  }

  it should "return a Failure indicating a missing \"s\" prefix" in {
    val sourceString = "123"
    val expected = Failure(ParserError(s"String '${sourceString}' does not start with an 's'"))
    val actual = Reading.parseSensorId(sourceString)
    assert(expected == actual)
  }

  it should "return a Failure indicating an integer could not be parsed from source string" in {
    val sourceString = "sabc"
    val expected = Failure(ParserError(s"Couldn't parse an integer for sensor ID from string '${sourceString}'"))
    val actual = Reading.parseSensorId(sourceString)
    assert(expected == actual)
  }
}
