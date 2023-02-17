package com.alphasystem
package arabic
package morphologicalanalysis
package morphology

import cats.syntax.functor.*
import arabic.model.*
import morphologicalanalysis.graph.model.GraphNodeType
import morphology.graph.model.*
import morphology.model.*
import arabic.morphologicalengine.conjugation.model.{ MorphologicalTermType, NamedTemplate, OutputFormat }
import arabic.morphologicalengine.generator.model.{ PageOrientation, SortDirection, SortDirective }
import morphology.model.incomplete_verb.{ IncompleteVerbType, KanaPastTense, KanaPresentTense }
import com.typesafe.config.Config
import io.circe.*
import io.circe.Decoder.Result
import io.circe.DecodingFailure.Reason
import io.circe.generic.auto.*
import io.circe.syntax.*

import scala.util.{ Failure, Success, Try }

package object persistence {

  given ArabicLetterTypeDecoder: Decoder[ArabicLetterType] =
    (c: HCursor) =>
      Try(ArabicLetterType.valueOf(c.value.asString.get)) match
        case Failure(ex)    => exceptionToDecodingFailure(ex, c)
        case Success(value) => Right(value)

  given ArabicLetterTypeEncoder: Encoder[ArabicLetterType] =
    (a: ArabicLetterType) => Json.fromString(a.name)

  given NounStatusDecoder: Decoder[NounStatus] =
    (c: HCursor) =>
      Try(NounStatus.valueOf(c.value.asString.get)) match
        case Failure(ex)    => exceptionToDecodingFailure(ex, c)
        case Success(value) => Right(value)

  given NounStatusEncoder: Encoder[NounStatus] =
    (a: NounStatus) => Json.fromString(a.name)

  given NumberTypeDecoder: Decoder[NumberType] =
    (c: HCursor) =>
      Try(NumberType.valueOf(c.value.asString.get)) match
        case Failure(ex)    => exceptionToDecodingFailure(ex, c)
        case Success(value) => Right(value)

  given NumberTypeEncoder: Encoder[NumberType] =
    (a: NumberType) => Json.fromString(a.name)

  given GenderTypeDecoder: Decoder[GenderType] =
    (c: HCursor) =>
      Try(GenderType.valueOf(c.value.asString.get)) match
        case Failure(ex)    => exceptionToDecodingFailure(ex, c)
        case Success(value) => Right(value)

  given GenderTypeEncoder: Encoder[GenderType] =
    (a: GenderType) => Json.fromString(a.name)

  given NamedTagDecoder: Decoder[NamedTag] =
    (c: HCursor) =>
      Try(NamedTag.valueOf(c.value.asString.get)) match
        case Failure(ex)    => exceptionToDecodingFailure(ex, c)
        case Success(value) => Right(value)

  given NamedTagEncoder: Encoder[NamedTag] =
    (a: NamedTag) => Json.fromString(a.name)

  given DiacriticTypeDecoder: Decoder[DiacriticType] =
    (c: HCursor) =>
      Try(DiacriticType.valueOf(c.value.asString.get)) match
        case Failure(ex)    => exceptionToDecodingFailure(ex, c)
        case Success(value) => Right(value)

  given DiacriticTypeEncoder: Encoder[DiacriticType] =
    (a: DiacriticType) => Json.fromString(a.name)

  given HiddenNounStatusDecoder: Decoder[HiddenNounStatus] =
    (c: HCursor) =>
      Try(HiddenNounStatus.valueOf(c.value.asString.get)) match
        case Failure(ex)    => exceptionToDecodingFailure(ex, c)
        case Success(value) => Right(value)

  given HiddenNounStatusEncoder: Encoder[HiddenNounStatus] =
    (a: HiddenNounStatus) => Json.fromString(a.name)

  given HiddenPronounStatusDecoder: Decoder[HiddenPronounStatus] =
    (c: HCursor) =>
      Try(HiddenPronounStatus.valueOf(c.value.asString.get)) match
        case Failure(ex)    => exceptionToDecodingFailure(ex, c)
        case Success(value) => Right(value)

  given HiddenPronounStatusEncoder: Encoder[HiddenPronounStatus] =
    (a: HiddenPronounStatus) => Json.fromString(a.name)

  given NamedTemplateDecoder: Decoder[NamedTemplate] =
    (c: HCursor) =>
      Try(NamedTemplate.valueOf(c.value.asString.get)) match
        case Failure(ex)    => exceptionToDecodingFailure(ex, c)
        case Success(value) => Right(value)

  given NamedTemplateEncoder: Encoder[NamedTemplate] =
    (a: NamedTemplate) => Json.fromString(a.name)

  given ProNounDecoder: Decoder[ProNoun] =
    (c: HCursor) =>
      Try(ProNoun.valueOf(c.value.asString.get)) match
        case Failure(ex)    => exceptionToDecodingFailure(ex, c)
        case Success(value) => Right(value)

  given ProNounEncoder: Encoder[ProNoun] =
    (a: ProNoun) => Json.fromString(a.name)

  given RootTypeDecoder: Decoder[RootType] =
    (c: HCursor) =>
      Try(RootType.valueOf(c.value.asString.get)) match
        case Failure(ex)    => exceptionToDecodingFailure(ex, c)
        case Success(value) => Right(value)

  given RootTypeEncoder: Encoder[RootType] =
    (a: RootType) => Json.fromString(a.name)

  given VerbTypeDecoder: Decoder[VerbType] =
    (c: HCursor) =>
      Try(VerbType.valueOf(c.value.asString.get)) match
        case Failure(ex)    => exceptionToDecodingFailure(ex, c)
        case Success(value) => Right(value)

  given VerbTypeEncoder: Encoder[VerbType] =
    (a: VerbType) => Json.fromString(a.name)

  given WeakVerbTypeDecoder: Decoder[WeakVerbType] =
    (c: HCursor) =>
      Try(WeakVerbType.valueOf(c.value.asString.get)) match
        case Failure(ex)    => exceptionToDecodingFailure(ex, c)
        case Success(value) => Right(value)

  given WeakVerbTypeEncoder: Encoder[WeakVerbType] =
    (a: WeakVerbType) => Json.fromString(a.name)

  given GraphNodeTypeDecoder: Decoder[GraphNodeType] =
    (c: HCursor) =>
      Try(GraphNodeType.valueOf(c.value.asString.get)) match
        case Failure(ex)    => exceptionToDecodingFailure(ex, c)
        case Success(value) => Right(value)

  given GraphNodeTypeEncoder: Encoder[GraphNodeType] =
    (a: GraphNodeType) => Json.fromString(a.name)

  given ConversationTypeEncoder: Encoder[ConversationType] =
    (a: ConversationType) => Json.fromString(a.name)

  given ConversationTypeDecoder: Decoder[ConversationType] =
    (c: HCursor) =>
      Try(ConversationType.valueOf(c.value.asString.get)) match
        case Failure(ex)    => exceptionToDecodingFailure(ex, c)
        case Success(value) => Right(value)

  given LocationTypeEncoder: Encoder[LocationType] =
    (a: LocationType) => Json.fromString(a.name)

  given LocationTypeDecoder: Decoder[LocationType] =
    (c: HCursor) =>
      Try(LocationType.valueOf(c.value.asString.get)) match
        case Failure(ex)    => exceptionToDecodingFailure(ex, c)
        case Success(value) => Right(value)

  given FlexibilityEncoder: Encoder[Flexibility] =
    (a: Flexibility) => Json.fromString(a.name)

  given FlexibilityDecoder: Decoder[Flexibility] =
    (c: HCursor) =>
      Try(Flexibility.valueOf(c.value.asString.get)) match
        case Failure(ex)    => exceptionToDecodingFailure(ex, c)
        case Success(value) => Right(value)

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

  given SortDirectiveEncoder: Encoder[SortDirective] =
    (a: SortDirective) => Json.fromString(a.name)

  given SortDirectiveDecoder: Decoder[SortDirective] =
    (c: HCursor) =>
      Try(SortDirective.valueOf(c.value.asString.get)) match
        case Failure(ex)    => exceptionToDecodingFailure(ex, c)
        case Success(value) => Right(value)

  given NounKindEncoder: Encoder[NounKind] =
    (a: NounKind) => Json.fromString(a.name)

  given NounKindDecoder: Decoder[NounKind] =
    (c: HCursor) =>
      Try(NounKind.valueOf(c.value.asString.get)) match
        case Failure(ex)    => exceptionToDecodingFailure(ex, c)
        case Success(value) => Right(value)

  given NounTypeEncoder: Encoder[NounType] =
    (a: NounType) => Json.fromString(a.name)

  given NounTypeDecoder: Decoder[NounType] =
    (c: HCursor) =>
      Try(NounType.valueOf(c.value.asString.get)) match
        case Failure(ex)    => exceptionToDecodingFailure(ex, c)
        case Success(value) => Right(value)

  given NounPartOfSpeechTypDecoder: Decoder[NounPartOfSpeechType] =
    (c: HCursor) =>
      Try(NounPartOfSpeechType.valueOf(c.value.asString.get)) match
        case Failure(ex)    => exceptionToDecodingFailure(ex, c)
        case Success(value) => Right(value)

  given NounPartOfSpeechTypeEncoder: Encoder[NounPartOfSpeechType] =
    (a: NounPartOfSpeechType) => Json.fromString(a.name)

  given ParticlePartOfSpeechTypeEncoder: Encoder[ParticlePartOfSpeechType] =
    (a: ParticlePartOfSpeechType) => Json.fromString(a.name)

  given ParticlePartOfSpeechTypeDecoder: Decoder[ParticlePartOfSpeechType] =
    (c: HCursor) =>
      Try(ParticlePartOfSpeechType.valueOf(c.value.asString.get)) match
        case Failure(ex)    => exceptionToDecodingFailure(ex, c)
        case Success(value) => Right(value)

  given ProNounPartOfSpeechTypeEncoder: Encoder[ProNounPartOfSpeechType] =
    (a: ProNounPartOfSpeechType) => Json.fromString(a.name)

  given ProNounPartOfSpeechTypeDecoder: Decoder[ProNounPartOfSpeechType] =
    (c: HCursor) =>
      Try(ProNounPartOfSpeechType.valueOf(c.value.asString.get)) match
        case Failure(ex)    => exceptionToDecodingFailure(ex, c)
        case Success(value) => Right(value)

  given VerbPartOfSpeechTypeEncoder: Encoder[VerbPartOfSpeechType] =
    (a: VerbPartOfSpeechType) => Json.fromString(a.name)

  given VerbPartOfSpeechTypeDecoder: Decoder[VerbPartOfSpeechType] =
    (c: HCursor) =>
      Try(VerbPartOfSpeechType.valueOf(c.value.asString.get)) match
        case Failure(ex)    => exceptionToDecodingFailure(ex, c)
        case Success(value) => Right(value)

  given ProNounTypeEncoder: Encoder[ProNounType] =
    (a: ProNounType) => Json.fromString(a.name)

  given ProNounTypeDecoder: Decoder[ProNounType] =
    (c: HCursor) =>
      Try(ProNounType.valueOf(c.value.asString.get)) match
        case Failure(ex)    => exceptionToDecodingFailure(ex, c)
        case Success(value) => Right(value)

  given RelationshipTypeEncoder: Encoder[RelationshipType] =
    (a: RelationshipType) => Json.fromString(a.name)

  given RelationshipTypeDecoder: Decoder[RelationshipType] =
    (c: HCursor) =>
      Try(RelationshipType.valueOf(c.value.asString.get)) match
        case Failure(ex)    => exceptionToDecodingFailure(ex, c)
        case Success(value) => Right(value)

  given MorphologicalTermTypeEncoder: Encoder[MorphologicalTermType] =
    (a: MorphologicalTermType) => Json.fromString(a.name)

  given MorphologicalTermTypeDecoder: Decoder[MorphologicalTermType] =
    (c: HCursor) =>
      Try(MorphologicalTermType.valueOf(c.value.asString.get)) match
        case Failure(ex)    => exceptionToDecodingFailure(ex, c)
        case Success(value) => Right(value)

  given VerbModeEncoder: Encoder[VerbMode] =
    (a: VerbMode) => Json.fromString(a.name)

  given VerbModeDecoder: Decoder[VerbMode] =
    (c: HCursor) =>
      Try(VerbMode.valueOf(c.value.asString.get)) match
        case Failure(ex)    => exceptionToDecodingFailure(ex, c)
        case Success(value) => Right(value)

  given WordTypeEncoder: Encoder[WordType] = (a: WordType) => Json.fromString(a.name)

  given WordTypeDecoder: Decoder[WordType] =
    (c: HCursor) =>
      Try(WordType.valueOf(c.value.asString.get)) match
        case Failure(ex)    => exceptionToDecodingFailure(ex, c)
        case Success(value) => Right(value)

  given MorphologyVerbTypeEncoder: Encoder[MorphologyVerbType] =
    (a: MorphologyVerbType) => Json.fromString(a.name)

  given MorphologyVerbTypeDecoder: Decoder[MorphologyVerbType] =
    (c: HCursor) =>
      Try(MorphologyVerbType.valueOf(c.value.asString.get)) match
        case Failure(ex)    => exceptionToDecodingFailure(ex, c)
        case Success(value) => Right(value)

  given OutputFormatEncoder: Encoder[OutputFormat] =
    (a: OutputFormat) => Json.fromString(a.name)

  given OutputFormatDecoder: Decoder[OutputFormat] =
    (c: HCursor) =>
      Try(OutputFormat.valueOf(c.value.asString.get)) match
        case Failure(ex)    => exceptionToDecodingFailure(ex, c)
        case Success(value) => Right(value)

  given IncompleteVerbTypeEncoder: Encoder[IncompleteVerbType] =
    (a: IncompleteVerbType) =>
      a match
        case v: KanaPastTense =>
          Json.obj(
            ("type", Json.fromString(classOf[KanaPastTense].getSimpleName)),
            ("value", Json.fromString(v.name()))
          )
        case v: KanaPresentTense =>
          Json.obj(
            ("type", Json.fromString(classOf[KanaPresentTense].getSimpleName)),
            ("value", Json.fromString(v.name()))
          )

  given IncompleteVerbTypeDecoder: Decoder[IncompleteVerbType] =
    (c: HCursor) =>
      for {
        `type` <- c.downField("type").as[String]
        value <- c.downField("value").as[String]
      } yield {
        `type` match
          case "KanaPastTense"    => KanaPastTense.valueOf(value)
          case "KanaPresentTense" => KanaPresentTense.valueOf(value)
          case _                  => throw new IllegalArgumentException(s"Invalid type: ${`type`}")
      }

  private def exceptionToDecodingFailure(ex: Throwable, c: HCursor) =
    Left(
      DecodingFailure(
        DecodingFailure.Reason.CustomReason(ex.getMessage),
        c
      )
    )
}
