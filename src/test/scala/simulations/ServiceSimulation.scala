package simulations

import com.typesafe.config.ConfigFactory
import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.http.protocol.HttpProtocolBuilder
import scala.concurrent.duration._
import simulations.GoogleScenario.mainScenario

class ServiceSimulation extends Simulation {

  private val appConfig = ConfigFactory.load()
  private val host = appConfig.getString("service.host")
  private val requestCount = appConfig.getInt("scenario.requestCount")
  private val duration = appConfig.getDuration("scenario.duration", SECONDS)
  private val percentile99ResponseTime = appConfig.getDuration("scenario.percentile99ResponseTime", MILLISECONDS)

  val userAgent = s"Gatling/${configuration.core.version}"

  val httpConf: HttpProtocolBuilder = http
    .baseURL(host)
    .userAgentHeader(userAgent)
    .disableCaching
    .disableWarmUp
    .disableAutoReferer
    .disableClientSharing

  println("--- Configuration for current test run : ---")
  println("HOST_NAME : " + host)
  println("REQUEST_COUNT : " + requestCount)
  println("DURATION_MINUTES : " + duration)

  setUp(
    mainScenario.inject(constantUsersPerSec(requestCount) during duration.seconds)
      .protocols(httpConf))
    .maxDuration(duration.minutes)
    .assertions(
      global.responseTime.percentile4.lte(percentile99ResponseTime.toInt)
    )


}
