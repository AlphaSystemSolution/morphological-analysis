package com.alphasystem
package arabic
package cli

import arabic.morphologicalengine.generator.model.ConjugationTemplate
import io.circe.Decoder
import io.circe.generic.auto.*
import io.circe.yaml.v12.parser

import java.nio.file.Path
import scala.io.Source
import scala.util.{ Failure, Success, Using }

package object morphologicalengine {

  private[cli] def toSingleConjugationRequest(path: Path): SingleConjugationRequest =
    fromFile(path, toSingleConjugationRequest)

  private[cli] def toSingleConjugationRequest(ymlString: String): SingleConjugationRequest =
    fromString[SingleConjugationRequest](ymlString)

  private[cli] def toPairedConjugationRequest(path: Path): PairedConjugationRequest =
    fromFile(path, toPairedConjugationRequest)

  private[cli] def toPairedConjugationRequest(ymlString: String): PairedConjugationRequest =
    fromString[PairedConjugationRequest](ymlString)

  private[cli] def toConjugationTemplate(path: Path): ConjugationTemplate =
    fromFile(path, toConjugationTemplate)

  private[cli] def toConjugationTemplate(ymlString: String): ConjugationTemplate =
    fromString[ConjugationTemplate](ymlString)

  private def fromFile[T](path: Path, fromString: String => T)(using dec: Decoder[T]): T =
    Using(Source.fromFile(path.toFile))(source => fromString(source.mkString)) match
      case Failure(ex)    => throw ex
      case Success(value) => value

  private def fromString[T](ymlString: String)(using dec: Decoder[T]): T =
    parser.parse(ymlString) match {
      case Left(ex) => throw ex
      case Right(value) =>
        value.as[T] match {
          case Left(ex)     => throw ex
          case Right(value) => value
        }
    }
}
