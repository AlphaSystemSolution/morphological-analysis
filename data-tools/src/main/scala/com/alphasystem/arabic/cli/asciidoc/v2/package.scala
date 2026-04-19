package com.alphasystem
package arabic
package cli
package asciidoc

import io.circe.generic.auto.*
import io.circe.yaml.v12.parser

import java.nio.file.Path
import scala.io.Source
import scala.util.{ Failure, Success, Using }

package object v2 {

  def toRequest(path: Path): ExampleRequest =
    Using(Source.fromFile(path.toFile))(source => toRequest(source.mkString)) match
      case Failure(ex)    => throw ex
      case Success(value) => value

  private def toRequest(ymlString: String): ExampleRequest =
    parser.parse(ymlString) match {
      case Left(ex) => throw ex
      case Right(value) =>
        value.as[ExampleRequest] match {
          case Left(ex)     => throw ex
          case Right(value) => value
        }
    }

}
