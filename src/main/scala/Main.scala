import measurements.Measurement

object Main {
  def main(args: Array[String]): Unit = {
    println(Measurement.parse("s12100"))
  }
}