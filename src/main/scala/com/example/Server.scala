package net.fgsquared

import scala.util.Properties

import org.http4s.Http4s._
import org.http4s.server.blaze.BlazeBuilder

import org.http4s._
import org.http4s.server._
import org.http4s.dsl._
import org.http4s.twirl._

object Server {

  def main(args: Array[String]) {
    val port = Properties.envOrElse("PORT", "8181").toInt
    println("Starting on port: " + port)

    val fgsserver = HttpService {
      case GET -> Root => Ok(net.fgsquared.html.index())

    }

    BlazeBuilder.bindHttp(port, "0.0.0.0")
      .mountService(fgsserver, "/")
      .run
      .awaitShutdown()
  }
}
