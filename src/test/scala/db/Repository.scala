package db

import com.typesafe.config.{Config, ConfigFactory}
import slick.driver.PostgresDriver.api._
import slick.lifted.QueryBase
import utils.Implicits._

import scala.concurrent.Await.result
import scala.concurrent.duration.Duration.Inf
import skinny.http._


object Repository {

  private val appConfig: Config = ConfigFactory.load()

  private val postgresUrl: String = appConfig.getString("db.postgres.url")
  private val postgresDriver: String = appConfig.getString("db.postgres.driver")

  private val influxDbUrl: String = appConfig.getString("db.influx.url")


  def checkDataRowsStatuses(): Unit = {
    val db = Database.forURL(postgresUrl, postgresDriver)
    val table = TableQuery[DataRows]
    val query = table.filter(_.status like s"AAAAAAAAAAAAAAAAAA%")
    println(s"Query to retrieve statuses = ${query.result.statements}")

    println("First time retrieving status: ")
    printAllDataRows(db, query)

    sendDataToInfluxDB(db, query)
  }

  private def executeSQLQuery(db: Database, query: QueryBase[Seq[GoogleDataRow]]) = result(db.run(query.result), Inf)

  private def printAllDataRows(db: Database, query: QueryBase[Seq[GoogleDataRow]]): Unit = executeSQLQuery(db, query).toList foreach println

  private def sendDataToInfluxDB(db: Database, query: QueryBase[Seq[GoogleDataRow]]): Unit =  {
    val requestToInfluxDB = executeSQLQuery(db, query).toList.mapToInfluxDbRequest
    println(s"Request to save statuses to Influx DB: \n$requestToInfluxDB")
    HTTP.post(influxDbUrl, requestToInfluxDB)
  }


}
