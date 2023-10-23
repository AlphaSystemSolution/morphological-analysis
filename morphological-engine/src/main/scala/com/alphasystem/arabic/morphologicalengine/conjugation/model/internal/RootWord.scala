package com.alphasystem
package arabic
package morphologicalengine
package conjugation
package model
package internal

import arabic.model.{ ArabicLetter, ArabicLetterType, ArabicSupport, ArabicWord, DiacriticType }

case class RootWord(
  rootLetter: RootLetters,
  `type`: MorphologicalTermType,
  baseWord: ArabicWord,
  derivedWord: ArabicWord)
    extends ArabicSupport {

  lazy val lastLetterIndex: Int = derivedWord.letters.length - 1

  def transform(
    firstRadicalType: ArabicLetterType,
    secondRadicalType: ArabicLetterType,
    thirdRadicalType: ArabicLetterType,
    fourthRadicalType: Option[ArabicLetterType] = None
  ): RootWord = {
    val letters = derivedWord.letters

    var newRootLetters = Seq(
      firstRadical.copy(letter = firstRadicalType),
      secondRadical.copy(letter = secondRadicalType),
      thirdRadical.copy(letter = thirdRadicalType)
    ) ++ fourthRadicalType.zip(rootLetter.fourthRadical).map(tuple => tuple._2.copy(letter = tuple._1))

    var currentLetter = newRootLetters.head
    val updatedLetters =
      letters.zipWithIndex.foldLeft(Seq.empty[ArabicLetter]) { case (updatedLetters, (letter, index)) =>
        if index == currentLetter.index then {
          val updated = updatedLetters :+ letter.replace(currentLetter.letter)
          newRootLetters = newRootLetters.tail
          currentLetter = newRootLetters.headOption.getOrElse(RootLetter(ArabicLetterType.Tatweel, -1))
          updated
        } else updatedLetters :+ letter
      }

    RootWord(rootLetter, `type`, baseWord, ArabicWord(updatedLetters*))
  }

  def isFeminine: Boolean = derivedWord.letters.last.letter == ArabicLetterType.TaMarbuta

  lazy val firstRadical: RootLetter = rootLetter.firstRadical
  lazy val firstRadicalIndex: Int = firstRadical.index
  lazy val firstRadicalLetter: Option[ArabicLetter] = derivedWord.letterAt(firstRadicalIndex)
  lazy val firstRadicalLetterType: Option[ArabicLetterType] = firstRadicalLetter.map(_.letter)
  lazy val firstRadicalDiacritic: Option[DiacriticType] = firstRadicalLetter.flatMap(_.firstDiacritic)

  lazy val secondRadical: RootLetter = rootLetter.secondRadical
  lazy val secondRadicalIndex: Int = secondRadical.index
  lazy val secondRadicalLetter: Option[ArabicLetter] = derivedWord.letterAt(secondRadicalIndex)
  lazy val secondRadicalLetterType: Option[ArabicLetterType] = secondRadicalLetter.map(_.letter)
  lazy val secondRadicalDiacritic: Option[DiacriticType] = secondRadicalLetter.flatMap(_.firstDiacritic)

  lazy val thirdRadical: RootLetter = rootLetter.thirdRadical
  lazy val thirdRadicalIndex: Int = thirdRadical.index
  lazy val thirdRadicalLetter: Option[ArabicLetter] = derivedWord.letterAt(thirdRadicalIndex)
  lazy val thirdRadicalLetterType: Option[ArabicLetterType] = thirdRadicalLetter.map(_.letter)
  lazy val thirdRadicalDiacritic: Option[DiacriticType] = thirdRadicalLetter.flatMap(_.firstDiacritic)

  def toStringValue(outputFormat: OutputFormat): String = {
    import OutputFormat.*

    outputFormat match
      case Unicode    => label
      case Html       => derivedWord.htmlCode
      case BuckWalter => derivedWord.code
  }

  override val label: String = derivedWord.label
}

object RootWord {

  def apply(
    rootLetter: RootLetters,
    `type`: MorphologicalTermType,
    baseWord: ArabicWord,
    derivedWord: ArabicWord
  ): RootWord =
    new RootWord(rootLetter, `type`, baseWord, derivedWord)

  def apply(
    `type`: MorphologicalTermType,
    firstRadicalIndex: Int,
    secondRadicalIndex: Int,
    thirdRadicalIndex: Int,
    fourthRadicalIndex: Option[Int],
    arabicLetters: ArabicLetter*
  ): RootWord = {
    val arabicWord = ArabicWord(arabicLetters*)
    RootWord(
      rootLetter = RootLetters(
        firstRadical = RootLetter(arabicLetters(firstRadicalIndex).letter, firstRadicalIndex),
        secondRadical = RootLetter(arabicLetters(secondRadicalIndex).letter, secondRadicalIndex),
        thirdRadical = RootLetter(arabicLetters(thirdRadicalIndex).letter, thirdRadicalIndex),
        fourthRadical = fourthRadicalIndex.map(index => RootLetter(arabicLetters(index).letter, index))
      ),
      `type` = `type`,
      baseWord = arabicWord,
      derivedWord = arabicWord
    )
  }

  def apply(
    `type`: MorphologicalTermType,
    firstRadicalIndex: Int,
    secondRadicalIndex: Int,
    thirdRadicalIndex: Int,
    arabicLetters: ArabicLetter*
  ): RootWord = RootWord(`type`, firstRadicalIndex, secondRadicalIndex, thirdRadicalIndex, None, arabicLetters*)
}
