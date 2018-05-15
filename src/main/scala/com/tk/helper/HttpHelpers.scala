package com.tk.helper

import com.typesafe.scalalogging.StrictLogging
import dispatch.Defaults._
import dispatch._

import scala.concurrent.Future
import scala.util.Right

trait HttpHelpers extends StrictLogging {

  protected def _http: Http

  protected def withRetry[I, O](mapper: I => O)(
      f: () => Future[Either[Throwable, I]]): Future[Either[Throwable, O]] = {
    retry.Backoff()(() =>
      f().map {
        case Left(cause) =>
          logger.error(s"Request not complete: ${cause.getMessage}")
          Left(cause)

        case Right(body) =>
          try {
            Right(mapper(body))
          } catch {
            case cause: Throwable =>
              Left(cause)
          }
    })
  }

  def shutdownHttp(): Unit = {
    _http.client.close()
    Defaults.timer.stop()
    ()
  }

}
