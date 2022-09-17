package com.alphasystem.arabic.morphologicalanalysis.morphology.model

import com.alphasystem.arabic.model.{ ArabicLetterType, ArabicWord }
import com.alphasystem.morphologicalanalysis.morphology.model.*

trait AbstractSimpleDocument {
  val id: String
}

trait AbstractDocument extends AbstractSimpleDocument {
  val displayName: String = s"${getClass.getSimpleName}:$id"
}

trait Linkable extends AbstractDocument

case class Chapter(
  override val id: String,
  chapterName: String,
  chapterNumber: Int,
  verseCount: Int,
  verses: Seq[Verse])
    extends AbstractDocument

case class Verse(
  override val id: String,
  chapterNumber: Int,
  verseNumber: Int,
  text: String,
  tokenCount: Int,
  verse: ArabicWord,
  tokens: Seq[Token])
    extends AbstractDocument

case class Location[P <: PartOfSpeechType, AP <: AbstractProperties[P]](
  override val id: String,
  chapterNumber: Int,
  verseNumber: Int,
  tokenNumber: Int,
  locationNumber: Int,
  hidden: Boolean,
  startIndex: Int,
  endIndex: Int,
  derivedText: String,
  text: String,
  translation: String,
  namedTag: Option[NamedTag] = None,
  properties: Seq[AP] = Seq.empty)
    extends Linkable

case class Token(
  override val id: String,
  chapterNumber: Int,
  verseNumber: Int,
  tokenNumber: Int,
  token: Int,
  translation: String)
    extends AbstractDocument

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

sealed trait AbstractProperties[P <: PartOfSpeechType]
    extends AbstractSimpleDocument {
  val partOfSpeech: P
  val number: NumberType
  val gender: GenderType
}

sealed trait AbstractNounProperties[P <: PartOfSpeechType]
    extends AbstractProperties[P] {
  val status: NounStatus
}

case class NounProperties(
  override val id: String,
  override val partOfSpeech: NounPartOfSpeechType,
  override val status: NounStatus,
  override val number: NumberType,
  override val gender: GenderType)
    extends AbstractNounProperties[NounPartOfSpeechType]

case class ProNounProperties(
  override val id: String,
  override val partOfSpeech: ProNounPartOfSpeechType,
  override val status: NounStatus,
  override val number: NumberType,
  override val gender: GenderType,
  conversationType: ConversationType,
  proNounType: ProNounType)
    extends AbstractNounProperties[ProNounPartOfSpeechType]

case class ParticleProperties(
  override val id: String,
  override val partOfSpeech: ParticlePartOfSpeechType)
    extends AbstractProperties[ParticlePartOfSpeechType] {
  override val number: NumberType = NumberType.NONE
  override val gender: GenderType = GenderType.None
}

case class VerbProperties(
  override val id: String,
  override val partOfSpeech: VerbPartOfSpeechType,
  override val number: NumberType,
  override val gender: GenderType,
  conversationType: ConversationType,
  verbType: VerbType,
  mode: VerbMode) // TODO: Incomplete
    extends AbstractProperties[VerbPartOfSpeechType]
