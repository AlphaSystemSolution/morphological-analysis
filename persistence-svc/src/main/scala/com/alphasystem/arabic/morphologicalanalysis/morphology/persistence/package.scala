package com.alphasystem.arabic.morphologicalanalysis.morphology

import cats.syntax.functor.*
import com.alphasystem.arabic.model.*
import com.alphasystem.arabic.morphologicalanalysis.morphology.graph.model.*
import com.alphasystem.arabic.morphologicalanalysis.morphology.model.*
import com.alphasystem.morphologicalanalysis.graph.model.GraphNodeType
import com.alphasystem.morphologicalanalysis.morphology.model.{
  VerbType as MorphologyVerbType,
  *
}
import com.alphasystem.morphologicalengine.conjugation.model.OutputFormat
import io.circe.*
import io.circe.Decoder.Result
import io.circe.DecodingFailure.Reason
import io.circe.generic.auto.*
import io.circe.syntax.*

import java.io.Closeable
import javax.sql.DataSource
import scala.util.{ Failure, Success, Try }

package object persistence {

  type CloseableDataSource = DataSource with Closeable

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

  given SarfTermTypeEncoder: Encoder[SarfTermType] =
    (a: SarfTermType) => Json.fromString(a.name)
  given SarfTermTypeDecoder: Decoder[SarfTermType] =
    (c: HCursor) =>
      Try(SarfTermType.valueOf(c.value.asString.get)) match
        case Failure(ex)    => exceptionToDecodingFailure(ex, c)
        case Success(value) => Right(value)

  given VerbModeEncoder: Encoder[VerbMode] =
    (a: VerbMode) => Json.fromString(a.name)
  given VerbModeDecoder: Decoder[VerbMode] =
    (c: HCursor) =>
      Try(VerbMode.valueOf(c.value.asString.get)) match
        case Failure(ex)    => exceptionToDecodingFailure(ex, c)
        case Success(value) => Right(value)

  /*given WordTypeEncoder: Encoder[WordType] = stringEnumEncoder[WordType]
  given WordTypeDecoder: Decoder[WordType] = stringEnumDecoder[WordType]*/

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

  /*given encodeProperties: Encoder[WordProperties] = Encoder.instance {
    case p: NounProperties     => p.asJson
    case p: ProNounProperties  => p.asJson
    case p: VerbProperties     => p.asJson
    case p: ParticleProperties => p.asJson
  }

  given decodeProperties: Decoder[WordProperties] =
    List[Decoder[WordProperties]](
      Decoder[NounProperties].widen,
      Decoder[ProNounProperties].widen,
      Decoder[VerbProperties].widen,
      Decoder[ParticleProperties].widen
    ).reduceLeft(_ or _)*/

  /*given encodeGraphNode: Encoder[GraphNode] = Encoder.instance {
    case g: PartOfSpeechNode => g.asJson
    case g: PhraseNode       => g.asJson
    case g: HiddenNode       => g.asJson
    case g: TerminalNode     => g.asJson
    case g: ReferenceNode    => g.asJson
    case g: RelationshipNode => g.asJson
    case g: RootNode         => g.asJson
  }

  given decodeGraphNode: Decoder[GraphNode] =
    List[Decoder[GraphNode]](
      Decoder[PartOfSpeechNode].widen,
      Decoder[PhraseNode].widen,
      Decoder[HiddenNode].widen,
      Decoder[TerminalNode].widen,
      Decoder[ReferenceNode].widen,
      Decoder[RelationshipNode].widen,
      Decoder[RootNode].widen
    ).reduceLeft(_ or _)*/

  /*implicit val encodePartOfSpeechType: Encoder[PartOfSpeechType] =
    Encoder.instance {
      case t: NounPartOfSpeechType     => t.asJson
      case t: ProNounPartOfSpeechType  => t.asJson
      case t: ParticlePartOfSpeechType => t.asJson
      case t: VerbPartOfSpeechType     => t.asJson
    }

  implicit val decodePartOfSpeechType: Decoder[PartOfSpeechType] =
    List[Decoder[PartOfSpeechType]](
      Decoder[NounPartOfSpeechType].widen,
      Decoder[ProNounPartOfSpeechType].widen,
      Decoder[ParticlePartOfSpeechType].widen,
      Decoder[VerbPartOfSpeechType].widen
    ).reduceLeft(_ or _)*/

  private def exceptionToDecodingFailure(ex: Throwable, c: HCursor) =
    Left(
      DecodingFailure(
        DecodingFailure.Reason.CustomReason(ex.getMessage),
        c
      )
    )
}
