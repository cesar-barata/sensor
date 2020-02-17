package org.cesarb

import org.scalatest.FlatSpec
import Measurement.parse

class LineParserSpec extends FlatSpec {

  "parse" should "return 'Right' for a properly formatted line with humidity in [0,100]" in {
    val expected = Right(Measurement("s0", Some(2)))
    val input = "s0,2"
    val actual = parse(input)
    assert(expected == actual)
  }

  it should "return 'Right' for a properly formatted line with \"NaN\" for humidity" in {
    val expected = Right(Measurement("s0", None))
    val input = "s0,NaN"
    val actual = parse(input)
    assert(expected == actual)
  }

  it should "return 'Left' for properly formatted but with humidity value outside of [0,100]" in {
    val input = "s1,200"
    val result = parse(input)
    assert(result.isLeft)
  }

  it should "return 'Left' for a non-comma separated string" in {
    val input = "s023"
    val result = parse(input)
    assert(result.isLeft)
  }

}
