package utils

import db.GoogleDataRow

object Implicits {

  implicit class MinutesToMillis(val i: Int) {
    def minutesToMillis: Long = i * 60 * 1000
  }

  implicit class InfluxDBStringConverter(val l: List[GoogleDataRow]) {
    def mapToInfluxDbRequest: String = l.map(_.mapToInfluxDbString).mkString("rows,", "\nrows,", "")
  }

}
