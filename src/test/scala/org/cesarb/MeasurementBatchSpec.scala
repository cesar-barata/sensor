package org.cesarb

import org.scalatest.FlatSpec

class MeasurementBatchSpec extends FlatSpec {

  "merge" should "add the number of processed measurements from the given batches" in {
    val batch1 = MeasurementBatch(10, 0, Map())
    val batch2 = MeasurementBatch(30, 0, Map())
    val merged = batch1.merge(batch2)
    val expected = batch1.numProcessed + batch2.numProcessed
    val actual = merged.numProcessed
    assert(expected == actual)
  }

  it should "add the number of failed measurements from the given batches" in {
    val batch1 = MeasurementBatch(10, 2, Map())
    val batch2 = MeasurementBatch(30, 2, Map())
    val merged = batch1.merge(batch2)
    val expected = batch1.numFailures + batch2.numFailures
    val actual = merged.numFailures
    assert(expected == actual)
  }

  it should "append lists from corresponding sensor IDs on both batches" in {
    val batch1 = MeasurementBatch(10, 2, Map("id" -> List(1,2,3)))
    val batch2 = MeasurementBatch(30, 2, Map("id" -> List(4,5,6)))
    val merged = batch1.merge(batch2)
    val expected = List(1,2,3,4,5,6)
    val actual = merged.sensorData("id")
    assert(expected == actual)
  }

  it should "contain lists for each sensor ID on both batches" in {
    val batch1 = MeasurementBatch(10, 2, Map("id1" -> List(1,2,3)))
    val batch2 = MeasurementBatch(30, 2, Map("id2" -> List(4,5,6)))
    val merged = batch1.merge(batch2)
    val expected = Map("id1" -> List(1,2,3), "id2" -> List(4,5,6))
    val actual = batch1.merge(batch2).sensorData
    assert(expected == actual)
  }

  it should "be a 'commutative' operation" in {
    val batch1 = MeasurementBatch(10, 2, Map("id1" -> List(1,2,3)))
    val batch2 = MeasurementBatch(30, 4, Map("id2" -> List(4,5,6)))
    val merged1 = batch1 merge batch2
    val merged2 = batch2 merge batch1
    assert(merged1.numProcessed == merged2.numProcessed)
    assert(merged1.numFailures == merged2.numFailures)
    assert(merged1.sensorData.keys == merged2.sensorData.keys)
    merged1.sensorData.foldLeft(true) {
      case (isEqual, (id, data)) => isEqual && data.containsSlice(merged2.sensorData(id))
    }
    assert(merged1.stats == merged2.stats)
    assert(merged1.statsByAvg == merged2.stats)
  }

}
