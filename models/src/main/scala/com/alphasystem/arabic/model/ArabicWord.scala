package com.alphasystem.arabic.model

import scala.annotation.targetName

case class ArabicWord(letters: ArabicLetter*) extends ArabicSupport {

  private val (codeStr, unicodeStr, htmlStr) =
    letters.foldLeft(("", "", "")) { case ((s1, s2, s3), l) =>
      (s1 + l.code, s2 + l.unicode, s3 + l.htmlCode)
    }

  val code: String = codeStr
  val unicode: String = unicodeStr
  val htmlCode: String = htmlStr

  def concatWithSpace(otherWords: ArabicWord*): ArabicWord =
    concatWith(otherWords, Seq(ArabicLetters.LETTER_SPACE))

  def concatenateWithAnd(otherWords: ArabicWord*): ArabicWord =
    concatWith(
      otherWords,
      Seq(
        ArabicLetters.LETTER_SPACE,
        ArabicLetters.LETTER_WAW,
        ArabicLetters.LETTER_SPACE
      )
    )

  private def concatWith(
    otherWords: Seq[ArabicWord],
    separators: Seq[ArabicLetter]
  ): ArabicWord = {
    val allLetters =
      otherWords.foldLeft(letters) { case (ls, aw) =>
        (ls ++ separators) ++ aw.letters
      }
    ArabicWord(allLetters*)
  }

  override val label: ArabicWord = this
}

object ArabicWord {

  def apply(): ArabicWord = ArabicWord(Seq.empty[ArabicLetter]*)

  def apply(letters: ArabicLetter*): ArabicWord = new ArabicWord(letters*)

  @targetName("fromArabicLetterType")
  def apply(letterTypes: ArabicLetterType*): ArabicWord =
    ArabicWord(letterTypes.map(ArabicLetter(_))*)
}
