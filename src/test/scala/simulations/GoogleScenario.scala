package simulations

import java.util.concurrent.atomic.AtomicLong

import io.gatling.core.Predef._
import io.gatling.core.feeder.RecordSeqFeederBuilder
import io.gatling.core.structure.ScenarioBuilder
import requests.GoogleRequest.googleRequest

object GoogleScenario {

  val feeder: RecordSeqFeederBuilder[String] = csv("urls.csv").records.circular

  val mainScenario: ScenarioBuilder = scenario("Main scenario")
    .exec(session => {
      var randomLong = new AtomicLong(System.nanoTime())
      session.setAll(("randomNumber", s"R_${randomLong.getAndIncrement}"))
    })
    .exec(googleRequest)


}
