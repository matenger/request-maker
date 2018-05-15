package com.tk

import java.nio.charset.Charset

import com.tk.helper.HttpHelpers
import com.typesafe.scalalogging.StrictLogging
import dispatch.Defaults._
import dispatch._

import scala.io.Source

object Boot extends App with HttpHelpers with StrictLogging {

  override protected def _http = Http.default

  val filename = "src/main/resources/requests.csv"
  Source
    .fromFile(filename)
    .getLines
    .map(generateRequest)
    .foreach(request => {
      withRetry(identity[String]) { () =>
        _http(request OK as.String).either
      }.map {
        case Left(cause) =>
          logger.error(s"Error While Requesting ", cause)
          exit(1)

        case Right(_) =>
          logger.info(s"Request Succeeded")
          exit(0)
      }
    })

  private def generateRequest(_url: String) = {
    url(_url).setContentType("application/json", Charset.forName("utf-8"))
  }

  private def exit(status: Int) = {
    shutdownHttp()
    System.exit(status)
  }
}
