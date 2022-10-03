package com.alphasystem.arabic.morphologicalanalysis.morphology.model

import com.alphasystem.arabic.model.{
  ArabicLabel,
  ArabicLetterType,
  ArabicWord
}
import com.alphasystem.morphologicalanalysis.morphology.model.*

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
    ArabicLabel(this, chapterNumber.toString, ArabicWord(chapterName))
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

  val verseId: String = verseNumber.toVerseId(chapterNumber)
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
  properties: WordProperties = defaultProperties,
  translation: Option[String] = None,
  namedTag: Option[NamedTag] = None)
    extends Linkable {
  override val id: String =
    locationNumber.toLocationId(chapterNumber, verseNumber, tokenNumber)

  val tokenId: String = tokenNumber.toTokenId(chapterNumber, verseNumber)
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
sealed trait AbstractProperties[+P <: PartOfSpeechType]
    extends BaseProperties[P] {
  val partOfSpeech: P
  val number: NumberType
  val gender: GenderType
}

sealed trait AbstractNounProperties[+P <: PartOfSpeechType]
    extends AbstractProperties[P] {
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
