import org.scalatest.FlatSpec

import measurements._

class MeasurementParserSpec extends FlatSpec {
  "parse" should "return 'Right' for a properly formatted line with humidity in [0,100]" in {
    val expected = Right(Measurement(0, Some(2)))
    val input = "s0,2"
    val actual = Measurement.parse(input)
    assert(expected == actual)
  }

  it should "return 'Left' for properly formatted but with humidity value outside of [0,100]" in {
    val input = "s1,200"
    val result = Measurement.parse(input)
    assert(result.isLeft)
  }

  it should "return 'Left' for a non-comma separated string" in {
    val input = "s023"
    val result = Measurement.parse(input)
    assert(result.isLeft)
  }

  it should "return 'Left' for a string not starting with an 's'" in {
    val input = "1,12"
    val result = Measurement.parse(input)
    assert(result.isLeft)
  }
}
