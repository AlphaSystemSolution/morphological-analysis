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
import io.circe.generic.auto.*
import io.circe.syntax.*

import java.io.Closeable
import javax.sql.DataSource
import scala.compiletime.summonAll
import scala.deriving.Mirror

package object persistence {

  type CloseableDataSource = DataSource with Closeable

  inline def stringEnumDecoder[T](using m: Mirror.SumOf[T]): Decoder[T] =
    val elemInstances = summonAll[Tuple.Map[m.MirroredElemTypes, ValueOf]]
      .productIterator
      .asInstanceOf[Iterator[ValueOf[T]]]
      .map(_.value)
    val elemNames = summonAll[Tuple.Map[m.MirroredElemLabels, ValueOf]]
      .productIterator
      .asInstanceOf[Iterator[ValueOf[String]]]
      .map(_.value)
    val mapping = (elemNames zip elemInstances).toMap
    Decoder[String].emap { name =>
      mapping.get(name).fold(Left(s"Name $name is invalid value"))(Right(_))
    }

  inline def stringEnumEncoder[T](using m: Mirror.SumOf[T]): Encoder[T] =
    val elemInstances = summonAll[Tuple.Map[m.MirroredElemTypes, ValueOf]]
      .productIterator
      .asInstanceOf[Iterator[ValueOf[T]]]
      .map(_.value)
    val elemNames = summonAll[Tuple.Map[m.MirroredElemLabels, ValueOf]]
      .productIterator
      .asInstanceOf[Iterator[ValueOf[String]]]
      .map(_.value)
    val mapping = (elemInstances zip elemNames).toMap
    Encoder[String].contramap[T](mapping.apply)

  given NounPartOfSpeechTypDecoder: Decoder[NounPartOfSpeechType] =
    stringEnumDecoder[NounPartOfSpeechType]
  given NounPartOfSpeechTypeEncoder: Encoder[NounPartOfSpeechType] =
    stringEnumEncoder[NounPartOfSpeechType]

  given NounStatusDecoder: Decoder[NounStatus] = stringEnumDecoder[NounStatus]
  given NounStatusEncoder: Encoder[NounStatus] = stringEnumEncoder[NounStatus]

  given NumberTypeDecoder: Decoder[NumberType] = stringEnumDecoder[NumberType]
  given NumberTypeEncoder: Encoder[NumberType] = stringEnumEncoder[NumberType]

  given GenderTypeDecoder: Decoder[GenderType] = stringEnumDecoder[GenderType]
  given GenderTypeEncoder: Encoder[GenderType] = stringEnumEncoder[GenderType]

  given NamedTagDecoder: Decoder[NamedTag] = stringEnumDecoder[NamedTag]
  given NamedTagEncoder: Encoder[NamedTag] = stringEnumEncoder[NamedTag]

  given ArabicLetterTypeDecoder: Decoder[ArabicLetterType] =
    stringEnumDecoder[ArabicLetterType]
  given ArabicLetterTypeEncoder: Encoder[ArabicLetterType] =
    stringEnumEncoder[ArabicLetterType]

  given DiacriticTypeDecoder: Decoder[DiacriticType] =
    stringEnumDecoder[DiacriticType]
  given DiacriticTypeEncoder: Encoder[DiacriticType] =
    stringEnumEncoder[DiacriticType]

  given HiddenNounStatusDecoder: Decoder[HiddenNounStatus] =
    stringEnumDecoder[HiddenNounStatus]
  given HiddenNounStatusEncoder: Encoder[HiddenNounStatus] =
    stringEnumEncoder[HiddenNounStatus]

  given HiddenPronounStatusDecoder: Decoder[HiddenPronounStatus] =
    stringEnumDecoder[HiddenPronounStatus]
  given HiddenPronounStatusEncoder: Encoder[HiddenPronounStatus] =
    stringEnumEncoder[HiddenPronounStatus]

  given NamedTemplateDecoder: Decoder[NamedTemplate] =
    stringEnumDecoder[NamedTemplate]
  given NamedTemplateEncoder: Encoder[NamedTemplate] =
    stringEnumEncoder[NamedTemplate]

  given ProNounDecoder: Decoder[ProNoun] = stringEnumDecoder[ProNoun]
  given ProNounEncoder: Encoder[ProNoun] = stringEnumEncoder[ProNoun]

  given RootTypeDecoder: Decoder[RootType] = stringEnumDecoder[RootType]
  given RootTypeEncoder: Encoder[RootType] = stringEnumEncoder[RootType]

  given VerbTypeDecoder: Decoder[VerbType] = stringEnumDecoder[VerbType]
  given VerbTypeEncoder: Encoder[VerbType] = stringEnumEncoder[VerbType]

  given WeakVerbTypeDecoder: Decoder[WeakVerbType] =
    stringEnumDecoder[WeakVerbType]
  given WeakVerbTypeEncoder: Encoder[WeakVerbType] =
    stringEnumEncoder[WeakVerbType]

  given GraphNodeTypeDecoder: Decoder[GraphNodeType] =
    stringEnumDecoder[GraphNodeType]
  given GraphNodeTypeEncoder: Encoder[GraphNodeType] =
    stringEnumEncoder[GraphNodeType]

  given ConversationTypeDecoder: Decoder[ConversationType] =
    stringEnumDecoder[ConversationType]

  given LocationTypeEncoder: Encoder[LocationType] =
    stringEnumEncoder[LocationType]
  given LocationTypeDecoder: Decoder[LocationType] =
    stringEnumDecoder[LocationType]

  given FlexibilityEncoder: Encoder[Flexibility] =
    stringEnumEncoder[Flexibility]
  given FlexibilityDecoder: Decoder[Flexibility] =
    stringEnumDecoder[Flexibility]

  given PageOrientationEncoder: Encoder[PageOrientation] =
    stringEnumEncoder[PageOrientation]
  given PageOrientationDecoder: Decoder[PageOrientation] =
    stringEnumDecoder[PageOrientation]

  given SortDirectionEncoder: Encoder[SortDirection] =
    stringEnumEncoder[SortDirection]
  given SortDirectionDecoder: Decoder[SortDirection] =
    stringEnumDecoder[SortDirection]

  given SortDirectiveEncoder: Encoder[SortDirective] =
    stringEnumEncoder[SortDirective]
  given SortDirectiveDecoder: Decoder[SortDirective] =
    stringEnumDecoder[SortDirective]

  given NounKindEncoder: Encoder[NounKind] = stringEnumEncoder[NounKind]
  given NounKindDecoder: Decoder[NounKind] = stringEnumDecoder[NounKind]

  given NounTypeEncoder: Encoder[NounType] = stringEnumEncoder[NounType]
  given NounTypeDecoder: Decoder[NounType] = stringEnumDecoder[NounType]

  given ParticlePartOfSpeechTypeEncoder: Encoder[ParticlePartOfSpeechType] =
    stringEnumEncoder[ParticlePartOfSpeechType]
  given ParticlePartOfSpeechTypeDecoder: Decoder[ParticlePartOfSpeechType] =
    stringEnumDecoder[ParticlePartOfSpeechType]

  given ProNounPartOfSpeechTypeEncoder: Encoder[ProNounPartOfSpeechType] =
    stringEnumEncoder[ProNounPartOfSpeechType]
  given ProNounPartOfSpeechTypeDecoder: Decoder[ProNounPartOfSpeechType] =
    stringEnumDecoder[ProNounPartOfSpeechType]

  given ProNounTypeEncoder: Encoder[ProNounType] =
    stringEnumEncoder[ProNounType]

  given ProNounTypeDecoder: Decoder[ProNounType] =
    stringEnumDecoder[ProNounType]

  given RelationshipTypeEncoder: Encoder[RelationshipType] =
    stringEnumEncoder[RelationshipType]
  given RelationshipTypeDecoder: Decoder[RelationshipType] =
    stringEnumDecoder[RelationshipType]

  given SarfTermTypeEncoder: Encoder[SarfTermType] =
    stringEnumEncoder[SarfTermType]
  given SarfTermTypeDecoder: Decoder[SarfTermType] =
    stringEnumDecoder[SarfTermType]

  given VerbModeEncoder: Encoder[VerbMode] = stringEnumEncoder[VerbMode]
  given VerbModeDecoder: Decoder[VerbMode] = stringEnumDecoder[VerbMode]

  given VerbPartOfSpeechTypeEncoder: Encoder[VerbPartOfSpeechType] =
    stringEnumEncoder[VerbPartOfSpeechType]
  given VerbPartOfSpeechTypeDecoder: Decoder[VerbPartOfSpeechType] =
    stringEnumDecoder[VerbPartOfSpeechType]

  /*given WordTypeEncoder: Encoder[WordType] = stringEnumEncoder[WordType]
  given WordTypeDecoder: Decoder[WordType] = stringEnumDecoder[WordType]*/

  given MorphologyVerbTypeEncoder: Encoder[MorphologyVerbType] =
    stringEnumEncoder[MorphologyVerbType]

  given MorphologyVerbTypeDecoder: Decoder[MorphologyVerbType] =
    stringEnumDecoder[MorphologyVerbType]

  given OutputFormatEncoder: Encoder[OutputFormat] =
    stringEnumEncoder[OutputFormat]
  given OutputFormatDecoder: Decoder[OutputFormat] =
    stringEnumDecoder[OutputFormat]

  given encodeProperties: Encoder[WordProperties] = Encoder.instance {
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
    ).reduceLeft(_ or _)

  given encodeGraphNode: Encoder[GraphNode] = Encoder.instance {
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
    ).reduceLeft(_ or _)
}
