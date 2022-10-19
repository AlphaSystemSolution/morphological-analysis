package com.alphasystem
package arabic
package morphologicalanalysis
package morphology
package model

import arabic.model.ArabicLetterType.*
import arabic.model.{ ArabicLabel, ArabicLetterType, ArabicSupportEnum, ArabicWord }
import WordType.{ NOUN, PARTICLE, PRO_NOUN, VERB }
import com.alphasystem.morphologicalanalysis.morphology.model.{
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

trait AbstractSimpleDocument {
  val id: String
}

trait AbstractDocument extends AbstractSimpleDocument {
  val displayName: String = s"${getClass.getSimpleName}:$id"
}

trait Linkable extends AbstractDocument

case class Chapter(
  chapterName: String,
  chapterNumber: Int,
  verseCount: Int)
    extends AbstractDocument {
  override val id: String = chapterNumber.toChapterId

  val toArabicLabel: ArabicLabel[Chapter] =
    ArabicLabel(this, chapterNumber.toString, chapterName)
}

case class Verse(
  chapterNumber: Int,
  verseNumber: Int,
  text: String,
  tokenCount: Int,
  translation: Option[String] = None)
    extends AbstractDocument {
  override val id: String = verseNumber.toVerseId(chapterNumber)

  val chapterId: String = chapterNumber.toChapterId
}

case class Token(
  chapterNumber: Int,
  verseNumber: Int,
  tokenNumber: Int,
  token: String,
  translation: Option[String] = None)
    extends AbstractDocument {
  override val id: String = tokenNumber.toTokenId(chapterNumber, verseNumber)

  override val displayName: String = tokenNumber.toTokenDisplayName(chapterNumber, verseNumber)

  val verseId: String = verseNumber.toVerseId(chapterNumber)

  val toArabicLabel: ArabicLabel[Token] =
    ArabicLabel(this, tokenNumber.toString, token)
}

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
    extends Linkable {
  require(validateProperties, "Invalid property type")

  override val id: String =
    locationNumber.toLocationId(chapterNumber, verseNumber, tokenNumber)

  override val displayName: String =
    locationNumber.toLocationDisplayName(
      chapterNumber,
      verseNumber,
      tokenNumber
    )

  val tokenId: String = tokenNumber.toTokenId(chapterNumber, verseNumber)

  val toArabicLabel: ArabicLabel[Location] =
    ArabicLabel(this, locationNumber.toString, alternateText)

  private def validateProperties: Boolean =
    wordType match
      case NOUN     => properties.getClass == classOf[NounProperties]
      case PRO_NOUN => properties.getClass == classOf[ProNounProperties]
      case VERB     => properties.getClass == classOf[VerbProperties]
      case PARTICLE => properties.getClass == classOf[ParticleProperties]
}

case class RootWord(
  override val id: String,
  firstRadical: ArabicLetterType,
  secondRadical: ArabicLetterType,
  thirdRadical: ArabicLetterType,
  fourthRadical: Option[ArabicLetterType] = None)
    extends AbstractSimpleDocument

case class VerseTokensPair(
  override val id: String,
  verseNumber: Int,
  firstTokenIndex: Int = 1,
  lastTokenIndex: Int = -1)
    extends AbstractDocument {
  require(verseNumber > 0, "verseNumber must be a positive integer")
  require(firstTokenIndex > 0, "firstTokenIndex must be a positive integer")
}

case class VerseTokenPairGroup(
  id: String,
  chapterNumber: Int,
  includeHidden: Boolean,
  pairs: Seq[VerseTokensPair])

sealed trait WordProperties

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
    extends AbstractNounProperties[NounPartOfSpeechType]

case class ProNounProperties(
  override val partOfSpeech: ProNounPartOfSpeechType,
  override val status: NounStatus,
  override val number: NumberType,
  override val gender: GenderType,
  conversationType: ConversationType,
  proNounType: ProNounType)
    extends AbstractNounProperties[ProNounPartOfSpeechType]

case class ParticleProperties(
  override val partOfSpeech: ParticlePartOfSpeechType)
    extends BaseProperties[ParticlePartOfSpeechType]

case class VerbProperties(
  override val partOfSpeech: VerbPartOfSpeechType,
  override val number: NumberType,
  override val gender: GenderType,
  conversationType: ConversationType,
  verbType: VerbType,
  mode: VerbMode) // TODO: Incomplete
    extends AbstractProperties[VerbPartOfSpeechType]

enum WordType(
  override val code: String,
  override val word: ArabicWord,
  val properties: WordProperties)
    extends Enum[VerbType]
    with ArabicSupportEnum {

  case NOUN
      extends WordType(
        "Noun",
        ArabicWord(ALIF_HAMZA_BELOW, SEEN, MEEM),
        defaultNounProperties
      )

  case PRO_NOUN
      extends WordType(
        "Pronoun",
        ArabicWord(DDAD, MEEM, YA, RA),
        defaultProNounProperties
      )

  case VERB extends WordType("Verb", ArabicWord(FA, AIN, LAM), defaultVerbProperties)

  case PARTICLE
      extends WordType(
        "Particle",
        ArabicWord(HHA, RA, FA),
        defaultParticleProperties
      )
}
