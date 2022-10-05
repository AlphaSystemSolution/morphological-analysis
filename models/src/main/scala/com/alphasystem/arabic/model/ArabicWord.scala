package com.alphasystem.arabic.model

import scala.annotation.targetName
import scala.collection.mutable.ListBuffer
import scala.util.Try

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

  override val label: String = this.unicode
}

object ArabicWord {

  def apply(): ArabicWord = ArabicWord(Seq.empty[ArabicLetter]*)

  def apply(letters: ArabicLetter*): ArabicWord = new ArabicWord(letters*)

  @targetName("fromArabicLetterType")
  def apply(letterTypes: ArabicLetterType*): ArabicWord =
    ArabicWord(letterTypes.map(ArabicLetter(_))*)

  @targetName("fromString")
  def apply(source: String, fromUnicode: Boolean = true): ArabicWord = {
    val trimmedSource = Try(source.trim).toOption.getOrElse("")
    if trimmedSource.isBlank then
      throw new IllegalArgumentException("source cannot be null or empty")
    else {
      val letters =
        trimmedSource
          .foldLeft(
            List.empty[(ArabicLetterType, ListBuffer[DiacriticType])]
          ) { case (ls, c) =>
            val maybeArabicLetterType =
              if fromUnicode then ArabicLetterType.UnicodesMap.get(c)
              else ArabicLetterType.CodesMap.get(c)

            val maybeDiacriticType =
              if fromUnicode then DiacriticType.UnicodesMap.get(c)
              else DiacriticType.CodesMap.get(c)

            (maybeArabicLetterType, maybeDiacriticType) match
              case (Some(value), None) =>
                ls :+ (value, ListBuffer[DiacriticType]())
              case (None, Some(value)) =>
                if ls.isEmpty then
                  throw new IllegalArgumentException(
                    s"word must start with a letter: $source"
                  )
                else {
                  val last = ls.last
                  ls.dropRight(1) :+ (last._1, last._2.append(value))
                }

              case _ =>
                if c == ' ' then
                  ls :+ (ArabicLetterType.SPACE, ListBuffer[DiacriticType]())
                else
                  throw new IllegalArgumentException(
                    s"unmapped character: ${Integer.toHexString(c.asDigit)}"
                  )
          }
          .map { case (letter, diacritics) =>
            ArabicLetter(letter, diacritics.toSeq*)
          }

      ArabicWord(letters*)
    }
  }
}
