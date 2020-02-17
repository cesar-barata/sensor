package org.cesarb

import org.scalatest.FlatSpec
import Extensions.MeasurementSeq

class SensorStatsSpec extends FlatSpec {

  it should "calculate the correct statistics for humidity measurement " +
    "after a merge" in {
    val min = 1
    val max = 20
    val avg = (min + max) / 2
    val measurements1 = ((min + 1) to max).toList
    val measurements2 = (min until max).toList
    val batch1 = MeasurementBatch(1, 0, Map("id" -> measurements1))
    val batch2 = MeasurementBatch(1, 0, Map("id" -> measurements2))
    val actualMin = batch1.merge(batch2).stats("id").min.get
    val actualAvg = batch1.merge(batch2).stats("id").avg.get
    val actualMax = batch1.merge(batch2).stats("id").max.get
    assert(min == actualMin)
    assert(avg == actualAvg)
    assert(max == actualMax)
  }

  "statsByAvg" should "calculate the correct statistics and order by " +
    "average value descending" in {
    val sensor1 = (1 to 10).toList
    val sensor2 = (0 to 20 by 3).toList
    val sensor3 = (1 to 15).toList
    val batch = MeasurementBatch(1, 0, Map(
      "id1" -> sensor1,
      "id2" -> sensor2,
      "id3" -> sensor3
    ))
    val stats = batch.statsByAvg
    assert(stats("id2").avg.get > stats("id3").avg.get && stats("id3").avg.get > stats("id1").avg.get)
  }

}
