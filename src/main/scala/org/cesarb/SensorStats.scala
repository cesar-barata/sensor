package org.cesarb

/**
 * Represents the summarized statistics for a given sensor.
 * @param min the minimum humidity measurement registered by the sensor.
 * @param avg the average humidity measurement registered by the sensor.
 * @param max the maximum humidity measurement registered by the sensor.
 */
case class SensorStats(min: Option[Int], avg: Option[Int], max: Option[Int]) {

  /**
   * @return a human readable representation of a SensorStats.
   */
  override def toString: String = {
    val minStr = min.getOrElse("NaN")
    val avgStr = avg.getOrElse("NaN")
    val maxStr = max.getOrElse("NaN")
    s"$minStr,$avgStr,$maxStr"
  }
}