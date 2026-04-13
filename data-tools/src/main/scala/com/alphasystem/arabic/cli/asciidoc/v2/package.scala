package com.alphasystem
package arabic
package cli
package asciidoc

import io.circe.*
import io.circe.generic.auto.*
import io.circe.parser.*

import java.nio.file.Path
import scala.io.Source
import scala.util.{ Failure, Success, Using }

package object v2 {

  def toRequest(path: Path): Table =
    Using(Source.fromFile(path.toFile))(source => toRequest(source.mkString)) match
      case Failure(ex)    => throw ex
      case Success(value) => value

  private def toRequest(json: String): Table =
    decode[Table](json) match
      case Left(ex) =>
        ex.printStackTrace()
        throw ex
      case Right(value) => value
}
