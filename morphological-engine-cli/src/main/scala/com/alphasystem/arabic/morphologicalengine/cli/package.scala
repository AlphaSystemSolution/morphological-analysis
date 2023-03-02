package com.alphasystem
package arabic
package morphologicalengine

import org.rogach.scallop.{ ArgType, ValueConverter }

import java.nio.file.{ Path, Paths }
import cats.syntax.functor.*
import arabic.model.ArabicLetterType
import arabic.morphologicalengine.conjugation.model.NamedTemplate
import arabic.morphologicalengine.generator.model.{
  ConjugationTemplate,
  DocumentFormat,
  PageOrientation,
  SortDirection,
  SortDirective
}
import io.circe.*
import io.circe.Decoder.Result
import io.circe.DecodingFailure.Reason
import io.circe.generic.auto.*
import io.circe.syntax.*
import io.circe.parser.*

import scala.io.Source
import scala.util.{ Failure, Success, Try }

package object cli {

  given PathConverter: ValueConverter[Path] = new ValueConverter[Path] {
    override def parse(s: List[(String, List[String])]): Either[String, Option[Path]] =
      s match
        case (_, p :: _) :: _ =>
          if p.isBlank then Right(None)
          else Right(Some(Paths.get(p)))
        case _ => Right(None)

    override val argType: ArgType.V = org.rogach.scallop.ArgType.SINGLE
  }

  def toConjugationTemplate(path: Path): ConjugationTemplate = {
    val source = Source.fromFile(path.toFile)
    val json = source.mkString
    Try(source.close())
    decode[ConjugationTemplate](json) match
      case Left(ex)     => throw ex
      case Right(value) => value
  }

  given ArabicLetterTypeDecoder: Decoder[ArabicLetterType] =
    (c: HCursor) =>
      Try(ArabicLetterType.valueOf(c.value.asString.get)) match
        case Failure(ex)    => exceptionToDecodingFailure(ex, c)
        case Success(value) => Right(value)

  given ArabicLetterTypeEncoder: Encoder[ArabicLetterType] =
    (a: ArabicLetterType) => Json.fromString(a.name)

  given NamedTemplateDecoder: Decoder[NamedTemplate] =
    (c: HCursor) =>
      Try(NamedTemplate.valueOf(c.value.asString.get)) match
        case Failure(ex)    => exceptionToDecodingFailure(ex, c)
        case Success(value) => Right(value)

  given NamedTemplateEncoder: Encoder[NamedTemplate] =
    (a: NamedTemplate) => Json.fromString(a.name)

  given PageOrientationEncoder: Encoder[PageOrientation] =
    (a: PageOrientation) => Json.fromString(a.name)

  given PageOrientationDecoder: Decoder[PageOrientation] =
    (c: HCursor) =>
      Try(PageOrientation.valueOf(c.value.asString.get)) match
        case Failure(ex)    => exceptionToDecodingFailure(ex, c)
        case Success(value) => Right(value)

  given SortDirectionEncoder: Encoder[SortDirection] =
    (a: SortDirection) => Json.fromString(a.name)

  given SortDirectionDecoder: Decoder[SortDirection] =
    (c: HCursor) =>
      Try(SortDirection.valueOf(c.value.asString.get)) match
        case Failure(ex)    => exceptionToDecodingFailure(ex, c)
        case Success(value) => Right(value)

  given DocumentFormatDecoder: Decoder[DocumentFormat] =
    (c: HCursor) =>
      Try(DocumentFormat.valueOf(c.value.asString.get)) match
        case Failure(ex)    => exceptionToDecodingFailure(ex, c)
        case Success(value) => Right(value)

  given DocumentFormatEncoder: Encoder[DocumentFormat] =
    (a: DocumentFormat) => Json.fromString(a.name)

  private def exceptionToDecodingFailure(ex: Throwable, c: HCursor) =
    Left(
      DecodingFailure(
        DecodingFailure.Reason.CustomReason(ex.getMessage),
        c
      )
    )
}
