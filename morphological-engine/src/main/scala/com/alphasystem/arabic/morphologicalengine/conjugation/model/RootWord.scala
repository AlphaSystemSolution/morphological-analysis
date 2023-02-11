package com.alphasystem
package arabic
package morphologicalengine
package conjugation
package model

import arabic.model.{ ArabicLetter, ArabicLetterType, ArabicSupport, ArabicWord }

case class RootWord(rootLetter: RootLetters, baseWord: ArabicWord, derivedWord: ArabicWord) extends ArabicSupport {

  lazy val thirdRadicalLetter: ArabicLetterType = rootLetter.thirdRadical.letter
  lazy val thirdRadicalIndex: Int = rootLetter.thirdRadical.index
  lazy val lastLetterIndex: Int = derivedWord.letters.length - 1

  def transform(
    firstRadical: ArabicLetterType,
    secondRadical: ArabicLetterType,
    thirdRadical: ArabicLetterType,
    fourthRadical: Option[ArabicLetterType] = None
  ): RootWord = {
    val letters = derivedWord.letters

    var newRootLetters = Seq(
      rootLetter.firstRadical.copy(letter = firstRadical),
      rootLetter.secondRadical.copy(letter = secondRadical),
      rootLetter.thirdRadical.copy(letter = thirdRadical)
    ) ++ fourthRadical.zip(rootLetter.fourthRadical).map(tuple => tuple._2.copy(letter = tuple._1))

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

    RootWord(rootLetter, baseWord, ArabicWord(updatedLetters*))
  }

  def isFeminine: Boolean = derivedWord.letters.last.letter == ArabicLetterType.TaMarbuta

  def toStringValue(outputFormat: OutputFormat): String = {
    import OutputFormat.*

    outputFormat match
      case Unicode    => label
      case Html       => derivedWord.htmlCode
      case BuckWalter => derivedWord.code
      case Stream     => throw new RuntimeException("Not implemented")
  }

  override val label: String = derivedWord.label
}

object RootWord {

  def apply(rootLetter: RootLetters, baseWord: ArabicWord, derivedWord: ArabicWord): RootWord =
    new RootWord(rootLetter, baseWord, derivedWord)

  def apply(
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
      baseWord = arabicWord,
      derivedWord = arabicWord
    )
  }

  def apply(
    firstRadicalIndex: Int,
    secondRadicalIndex: Int,
    thirdRadicalIndex: Int,
    arabicLetters: ArabicLetter*
  ): RootWord = RootWord(firstRadicalIndex, secondRadicalIndex, thirdRadicalIndex, None, arabicLetters*)
}
