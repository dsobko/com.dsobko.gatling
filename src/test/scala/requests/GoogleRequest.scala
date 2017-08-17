package requests

import io.gatling.core.Predef.{feed, _}
import io.gatling.core.structure.ChainBuilder
import io.gatling.http.Predef.{http, status}
import io.gatling.http.util.HttpHelper.RedirectStatusCodes
import simulations.GoogleScenario.feeder

object GoogleRequest {


  val googleRequest: ChainBuilder = feed(feeder)
    .exec(
      http("Request to Google services")
        .get("${requestUrl}")
        .disableFollowRedirect
        .check(status.in(RedirectStatusCodes))
    )

}
