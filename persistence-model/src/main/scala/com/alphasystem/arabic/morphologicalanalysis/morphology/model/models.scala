package com.alphasystem
package arabic
package morphologicalanalysis
package morphology
package model

import arabic.model.ArabicLetterType.*
import arabic.model.{ ArabicLabel, ArabicLetterType, ArabicLetters, ArabicSupportEnum, ArabicWord }
import WordType.{ NOUN, PARTICLE, PRO_NOUN, VERB }
import model.{
  ConversationType,
  GenderType,
  NamedTag,
  NounKind,
  NounPartOfSpeechType,
  NounStatus,
  NounType,
  NumberType,
  PartOfSpeechType,
  ParticlePartOfSpeechType,
  ProNounPartOfSpeechType,
  ProNounType,
  VerbMode,
  VerbPartOfSpeechType,
  VerbType
}

trait Entity[ID] {
  def id: ID
}

case class Chapter(
  chapterName: String,
  chapterNumber: Int,
  verseCount: Int)
    extends Entity[Int] {

  override val id: Int = chapterNumber
  val toArabicLabel: ArabicLabel[Chapter] = ArabicLabel(this, chapterNumber.toString, chapterName)
}

case class Verse(
  chapterNumber: Int,
  verseNumber: Int,
  text: String,
  tokenCount: Int,
  translation: Option[String] = None)
    extends Entity[Long] {

  override val id: Long = verseNumber.toVerseId(chapterNumber)
}

case class Token(
  chapterNumber: Int,
  verseNumber: Int,
  tokenNumber: Int,
  token: String,
  hidden: Boolean = false,
  translation: Option[String] = None,
  locations: Seq[Location] = Seq.empty)
    extends Entity[Long] {

  override val id: Long = tokenNumber.toTokenId(chapterNumber, verseNumber)
  val displayName: String = tokenNumber.toTokenDisplayName(chapterNumber, verseNumber)

  val verseId: Long = verseNumber.toVerseId(chapterNumber)

  val toArabicLabel: ArabicLabel[Token] = ArabicLabel(this, tokenNumber.toString, token)
}

trait Linkable

case class Location(
  chapterNumber: Int,
  verseNumber: Int,
  tokenNumber: Int,
  locationNumber: Int,
  hidden: Boolean,
  startIndex: Int,
  endIndex: Int,
  derivedText: String,
  text: String,
  alternateText: String,
  wordType: WordType = WordType.NOUN,
  properties: WordProperties = WordType.NOUN.properties,
  translation: Option[String] = None,
  namedTag: Option[NamedTag] = None)
    extends Entity[Long]
    with Linkable {

  override val id: Long = locationNumber.toLocationId(chapterNumber, verseNumber, tokenNumber)
  val tokenId: Long = tokenNumber.toTokenId(chapterNumber, verseNumber)

  val displayName: String =
    locationNumber.toLocationDisplayName(
      chapterNumber,
      verseNumber,
      tokenNumber
    )

  val toArabicLabel: ArabicLabel[Location] = ArabicLabel(this, locationNumber.toString, alternateText)

  def partOfSpeechType: PartOfSpeechType =
    properties match
      case p: NounProperties     => p.partOfSpeech
      case p: ProNounProperties  => p.partOfSpeech
      case p: VerbProperties     => p.partOfSpeech
      case p: ParticleProperties => p.partOfSpeech
}

case class RootWord(
  id: String,
  firstRadical: ArabicLetterType,
  secondRadical: ArabicLetterType,
  thirdRadical: ArabicLetterType,
  fourthRadical: Option[ArabicLetterType] = None)

sealed trait WordProperties {
  def toText: String
}

sealed trait BaseProperties[+P <: PartOfSpeechType] extends WordProperties {
  val partOfSpeech: P
}
sealed trait AbstractProperties[+P <: PartOfSpeechType] extends BaseProperties[P] {
  val partOfSpeech: P
  val number: NumberType
  val gender: GenderType
}

sealed trait AbstractNounProperties[+P <: PartOfSpeechType] extends AbstractProperties[P] {
  val status: NounStatus
}

case class NounProperties(
  override val partOfSpeech: NounPartOfSpeechType,
  override val status: NounStatus,
  override val number: NumberType,
  override val gender: GenderType,
  nounType: NounType,
  nounKind: NounKind)
    extends AbstractNounProperties[NounPartOfSpeechType] {

  override def toText: String = WordType.NOUN.word.concatWithSpace(status.word).unicode
}

case class ProNounProperties(
  override val partOfSpeech: ProNounPartOfSpeechType,
  override val status: NounStatus,
  override val number: NumberType,
  override val gender: GenderType,
  conversationType: ConversationType,
  proNounType: ProNounType)
    extends AbstractNounProperties[ProNounPartOfSpeechType] {

  override def toText: String =
    partOfSpeech match
      case ProNounPartOfSpeechType.Pronoun              => ArabicLetters.WordTatweel.unicode
      case ProNounPartOfSpeechType.RelativePronoun      => ArabicLetters.WordTatweel.unicode
      case ProNounPartOfSpeechType.DemonstrativePronoun => ArabicLetters.WordTatweel.unicode
}

case class ParticleProperties(
  override val partOfSpeech: ParticlePartOfSpeechType)
    extends BaseProperties[ParticlePartOfSpeechType] {

  override def toText: String = partOfSpeech.word.unicode
}

case class VerbProperties(
  override val partOfSpeech: VerbPartOfSpeechType,
  override val number: NumberType,
  override val gender: GenderType,
  conversationType: ConversationType,
  verbType: VerbType,
  mode: VerbMode) // TODO: Incomplete
    extends AbstractProperties[VerbPartOfSpeechType] {

  override def toText: String = ArabicLetters.WordTatweel.unicode
}

enum WordType(
  override val code: String,
  override val word: ArabicWord,
  val properties: WordProperties)
    extends Enum[VerbType]
    with ArabicSupportEnum {

  case NOUN
      extends WordType(
        "Noun",
        ArabicWord(AlifHamzaBelow, Seen, Meem),
        defaultNounProperties
      )

  case PRO_NOUN
      extends WordType(
        "Pronoun",
        ArabicWord(Ddad, Meem, Ya, Ra),
        defaultProNounProperties
      )

  case VERB extends WordType("Verb", ArabicWord(Fa, Ain, Lam), defaultVerbProperties)

  case PARTICLE
      extends WordType(
        "Particle",
        ArabicWord(Hha, Ra, Fa),
        defaultParticleProperties
      )
}
