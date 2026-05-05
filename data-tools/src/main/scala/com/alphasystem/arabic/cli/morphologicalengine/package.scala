package com.alphasystem
package arabic
package cli

import arabic.morphologicalengine.generator.model.{ ChartConfiguration, ConjugationTemplate }
import arabic.morphologicalengine.conjugation.model.{ ConjugationConfiguration, ConjugationInput, RootLetters }
import io.circe.generic.semiauto.{ deriveDecoder, deriveEncoder }
import io.circe.{ Decoder, Encoder }
import io.circe.yaml.v12.parser

import java.nio.file.Path
import scala.io.Source
import scala.util.{ Failure, Success, Using }

package object morphologicalengine {

  given Decoder[SingleConjugation] = deriveDecoder
  given Encoder[SingleConjugation] = deriveEncoder
  given Decoder[SingleConjugationRequest] = deriveDecoder
  given Encoder[SingleConjugationRequest] = deriveEncoder
  given Decoder[PairedConjugationRequest] = deriveDecoder
  given Encoder[PairedConjugationRequest] = deriveEncoder
  given Decoder[PairedConjugation] = deriveDecoder
  given Encoder[PairedConjugation] = deriveEncoder
  given Decoder[ConjugationRequest] = deriveDecoder
  given Encoder[ConjugationRequest] = deriveEncoder

  given Decoder[RootLetters] = deriveDecoder
  given Encoder[RootLetters] = deriveEncoder
  given Decoder[ChartConfiguration] = deriveDecoder
  given Encoder[ChartConfiguration] = deriveEncoder
  given Decoder[ConjugationConfiguration] = deriveDecoder
  given Encoder[ConjugationConfiguration] = deriveEncoder
  given Decoder[ConjugationInput] = deriveDecoder
  given Encoder[ConjugationInput] = deriveEncoder
  given Decoder[ConjugationTemplate] = deriveDecoder
  given Encoder[ConjugationTemplate] = deriveEncoder

  given Decoder[DisplaySettings] =
    Decoder.instance { cursor =>
      for {
        showPronouns <- cursor.downField("showPronouns").as[Option[Boolean]]
        showNumbers <- cursor.downField("showNumbers").as[Option[Boolean]]
        showGenders <- cursor.downField("showGenders").as[Option[Boolean]]
        showConversationTypes <- cursor.downField("showConversationTypes").as[Option[Boolean]]
        showNounStatus <- cursor.downField("showNounStatus").as[Option[Boolean]]
        showTermTypeCaption <- cursor.downField("showTermTypeCaption").as[Option[Boolean]]
        tableWidth <- cursor.downField("tableWidth").as[Option[Int]]
      } yield DisplaySettings(
        showPronouns = showPronouns,
        showNumbers = showNumbers,
        showGenders = showGenders,
        showConversationTypes = showConversationTypes,
        showNounStatus = showNounStatus,
        showTermTypeCaption = showTermTypeCaption,
        tableWidth = tableWidth
      )
    }

  given Encoder[DisplaySettings] =
    Encoder.forProduct7(
      "showPronouns",
      "showNumbers",
      "showGenders",
      "showConversationTypes",
      "showNounStatus",
      "showTermTypeCaption",
      "tableWidth"
    ) { settings =>
      (
        settings.showPronouns,
        settings.showNumbers,
        settings.showGenders,
        settings.showConversationTypes,
        settings.showNounStatus,
        settings.showTermTypeCaption,
        settings.tableWidth
      )
    }

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
