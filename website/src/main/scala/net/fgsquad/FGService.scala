package net.fgsquad

import org.http4s._
import org.http4s.server._
import org.http4s.dsl._
import org.http4s.twirl._
import org.http4s.headers.`Content-Type`

import org.http4s.Http4s._

object FGService {

  def resourceStream(path: String) = {
    var result = getClass.getClassLoader.getResourceAsStream(path)
    Option(result)
  }

  def getAllBytes(is: java.io.InputStream): Array[Byte] = {
    Stream.continually(is.read).takeWhile(x => -1 != x).map(_.toByte).toArray
  }

  def imageResource(filename: String) = {
    var mstream = resourceStream("images/" + filename)
    mstream.map(stream => Ok(getAllBytes(stream)))
      .getOrElse(NotFound("image not found"))
  }

  def cssResource(filename: String) = {
    var mstream = resourceStream(filename)
    var contentType = `Content-Type`(MediaType.forExtension("css").get)
    mstream.map(stream => Ok(getAllBytes(stream)).map(resp => resp.putHeaders(contentType)))
      .getOrElse(NotFound("stylesheet not found"))
  }

  def apply() = HttpService {
    case GET -> Root => Ok(net.fgsquad.html.index())
    case GET -> Root / "assets" / "images" / filename => imageResource(filename)
    case GET -> Root / "assets" / "css" / filename => cssResource(filename)

  }
}
