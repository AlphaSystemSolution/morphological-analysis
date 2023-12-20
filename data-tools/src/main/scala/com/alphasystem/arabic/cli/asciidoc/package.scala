package com.alphasystem
package arabic
package cli

import io.circe.*
import io.circe.generic.auto.*
import io.circe.parser.*

import java.nio.file.Path
import scala.io.Source
import scala.util.{ Failure, Success, Using }

package object asciidoc {

  def toDataRequest(path: Path): DataRequest =
    Using(Source.fromFile(path.toFile))(source => toDataRequest(source.mkString)) match
      case Failure(ex)    => throw ex
      case Success(value) => value

  private def toDataRequest(json: String): DataRequest =
    decode[DataRequest](json) match
      case Left(ex) =>
        ex.printStackTrace()
        throw ex
      case Right(value) => value
}
