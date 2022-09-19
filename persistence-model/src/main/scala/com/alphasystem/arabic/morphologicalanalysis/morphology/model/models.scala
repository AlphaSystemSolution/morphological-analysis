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
  verseCount: Int)
    extends AbstractDocument

case class Verse(
  override val id: String,
  chapterNumber: Int,
  verseNumber: Int,
  text: String,
  tokenCount: Int,
  translation: Option[String] = None)
    extends AbstractDocument

case class Token(
  override val id: String,
  chapterNumber: Int,
  verseNumber: Int,
  tokenNumber: Int,
  token: String,
  translation: Option[String] = None)
    extends AbstractDocument

case class Location(
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
  translation: Option[String] = None,
  namedTag: Option[NamedTag] = None)
    extends Linkable

case class RootWord(
  override val id: String,
  firstRadical: ArabicLetterType,
  secondRadical: ArabicLetterType,
  thirdRadical: ArabicLetterType,
  fourthRadical: Option[ArabicLetterType] = None)
    extends AbstractSimpleDocument

/*case class VerseTokensPair(
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
  pairs: Seq[VerseTokensPair])*/

sealed trait WordProperties extends AbstractSimpleDocument
sealed trait AbstractProperties[+P <: PartOfSpeechType] extends WordProperties {
  val partOfSpeech: P
  val number: NumberType
  val gender: GenderType
}

sealed trait AbstractNounProperties[+P <: PartOfSpeechType]
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
  override val gender: GenderType = GenderType.NONE
}

case class VerbProperties(
  override val id: String,
  override val partOfSpeech: VerbPartOfSpeechType,
  override val number: NumberType,
  override val gender: GenderType,
  conversationType: ConversationType,
  verbType: VerbType,
  mode: Option[VerbMode] = None) // TODO: Incomplete
    extends AbstractProperties[VerbPartOfSpeechType]
