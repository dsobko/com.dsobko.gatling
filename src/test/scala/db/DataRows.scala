package db

import java.sql.Timestamp

import slick.driver.PostgresDriver.api._

case class GoogleDataRow(uuid: String, status: String, createdAt: Timestamp, updatedAt: Timestamp) {
  override def toString = s"""status="$status",uuid="$uuid",createdAt="$createdAt",updatedAt="$updatedAt""""

  def mapToInfluxDbString = s"""status=$status uuid="$uuid",createdAt="$createdAt",updatedAt="$updatedAt""""
}

class DataRows(tag: Tag) extends Table[GoogleDataRow](tag, "google_data") {
  def uuid = column[String]("uuid")
  def status = column[String]("status")
  def createdAt = column[Timestamp]("created_at")
  def updatedAt = column[Timestamp]("updated_at")
  def * = (uuid, status, createdAt, updatedAt) <> (GoogleDataRow.tupled, GoogleDataRow.unapply)
}

