package net.fgsquad

import scala.util.Properties

import org.http4s.server.blaze.BlazeBuilder

object Server {

  def main(args: Array[String]) {
    val port = Properties.envOrElse("PORT", "8181").toInt
    val interface = "0.0.0.0";
    println("Starting on port: " + port)

    val fgsserver = FGService()

    BlazeBuilder.bindHttp(port, interface)
      .mountService(fgsserver, "/")
      .run
      .awaitShutdown()
  }

}
