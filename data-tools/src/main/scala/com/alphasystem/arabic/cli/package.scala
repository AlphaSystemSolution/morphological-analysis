package com.alphasystem
package arabic

import io.circe.Decoder
import io.circe.yaml.v12.parser
import org.rogach.scallop.{ ValueConverter, singleArgConverter }

import java.nio.file.{ Path, Paths }
import scala.io.Source
import scala.util.{ Failure, Success, Using }

package object cli {

  given pathConverter: ValueConverter[Path] = singleArgConverter[Path](arg => Paths.get(arg))

  def readAsciidocAttributes(attributesPath: Option[Path]): String =
    attributesPath match
      case Some(path) =>
        Using(Source.fromFile(path.toFile))(_.mkString).toOption.getOrElse("")
      case None => ""

  def fromFile[T](path: Path, fromString: String => T)(using dec: Decoder[T]): T =
    Using(Source.fromFile(path.toFile))(source => fromString(source.mkString)) match
      case Failure(ex)    => throw ex
      case Success(value) => value

  def fromString[T](ymlString: String)(using dec: Decoder[T]): T =
    parser.parse(ymlString) match {
      case Left(ex) => throw ex
      case Right(value) =>
        value.as[T] match {
          case Left(ex)     => throw ex
          case Right(value) => value
        }
    }
}
