package org.cesarb

object Extensions {

  /**
   * Enriches a Seq of Integers with an average method.
   * @param seq
   */
  implicit class MeasurementSeq(seq: Seq[Int]) {
    /**
     * Calculates the average of all elements in the sequence.
     * @return Returns Some(average) if the sequence is not empty.
     *         Otherwise returns None.
     */
    def average: Option[Int] =
      if (seq.isEmpty)
        None
      else
        Some(seq.sum / seq.size)
  }

  /**
   * Enriches a Map[String, SensorStats] with a display method.
   * @param map
   */
  implicit class StatsMap(map: Map[String, SensorStats]) {

    /**
     * Provides a human-readable (CSV) representation of a association of
     * sensor IDs and the corresponding sensor statistics.
     * @return a CSV string with the format:
     *         <sensor-id>,<min-humidity>,<avg-humidity>,<max-humidity>
     */
    def display: String =
      map.foldLeft(new StringBuilder) {
        case (sb, stats) =>
          sb.append(stats._1)
            .append(',')
            .append(stats._2)
            .append('\n')
      }.toString.strip
  }
}
