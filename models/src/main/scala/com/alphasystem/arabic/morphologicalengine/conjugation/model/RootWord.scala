package com.alphasystem
package arabic
package morphologicalengine
package conjugation
package model

import arabic.model.{ ArabicLetter, ArabicSupport, ArabicWord }

case class RootWord(rootLetter: RootLetters, baseWord: ArabicWord, derivedWord: ArabicWord) extends ArabicSupport {

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
