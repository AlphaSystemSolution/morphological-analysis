package com.alphasystem.arabic.morphologicalanalysis.morphology

import cats.syntax.functor.*
import com.alphasystem.arabic.morphologicalanalysis.morphology.model.*
import com.alphasystem.morphologicalanalysis.morphology.model.*
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

  given encodeProperties: Encoder[WordProperties] = Encoder.instance {
    case p @ NounProperties(_, _, _, _, _)          => p.asJson
    case p @ ProNounProperties(_, _, _, _, _, _, _) => p.asJson
    case p @ VerbProperties(_, _, _, _, _, _, _)    => p.asJson
    case p @ ParticleProperties(_, _)               => p.asJson
  }

  given decodeProperties: Decoder[WordProperties] =
    List[Decoder[WordProperties]](
      Decoder[NounProperties].widen,
      Decoder[ProNounProperties].widen,
      Decoder[VerbProperties].widen,
      Decoder[ParticleProperties].widen
    ).reduceLeft(_ or _)
}
