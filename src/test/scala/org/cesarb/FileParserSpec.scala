package org.cesarb

import org.scalatest.FlatSpec

import MeasurementBatch.parse

class FileParserSpec extends FlatSpec {

  "parse" should "map sensor IDs into their measured values" in {
    val input =
      """
        |sensor-id,humidity
        |s1,10
        |s2,88
        |s1,NaN
        |""".stripMargin.split("\n").iterator

    val result = parse(input)
    assert(result.sensorData.get("s1").contains(Seq(10)))
    assert(result.sensorData.get("s2").contains(Seq(88)))
  }

  it should "not create mappings for invalid input format" in {
    val input =
      """
        |sensor-id,humidity
        |s110
        |s2,88
        |,NaN
        |""".stripMargin.split("\n").iterator

    val result = parse(input)
    assert(result.sensorData.get("s1").isEmpty)
    assert(result.sensorData.get("s2").isDefined)
    assert(result.sensorData.get("s110").isEmpty)
  }

}
