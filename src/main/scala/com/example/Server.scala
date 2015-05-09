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

    def resourceStream(path: String) = {
      var result = getClass.getClassLoader.getResourceAsStream(path)
      Option(result)
    }

    val fgsserver = HttpService {
      case GET -> Root => Ok(net.fgsquared.html.index())
      case GET -> Root / "assets" / "images" / filename => {
        var mstream = resourceStream("public/images/" + filename)
        mstream match {
          case None => Ok("404")
          case Some(stream) => {
            val bytes = getAllBytes(stream);
            println("got bytes")
            Ok(bytes)
          }
        }
      }

    }

    BlazeBuilder.bindHttp(port, "0.0.0.0")
      .mountService(fgsserver, "/")
      .run
      .awaitShutdown()
  }

  def getAllBytes(is: java.io.InputStream): Array[Byte] = {
    Stream.continually(is.read).takeWhile(-1 !=).map(_.toByte).toArray
  }
}
