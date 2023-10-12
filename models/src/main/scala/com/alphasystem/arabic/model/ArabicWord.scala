package com.alphasystem
package arabic
package model

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

  def firstLetter: Option[ArabicLetter] = letters.headOption

  def lastLetter: Option[ArabicLetter] = letters.lastOption

  def letterAt(index: Int): Option[ArabicLetter] = Try(letters(index)).toOption

  def concat(otherWords: ArabicWord*): ArabicWord = concatWith(otherWords, Seq.empty)

  def concatWithSpace(otherWords: ArabicWord*): ArabicWord =
    concatWith(otherWords, Seq(ArabicLetters.LetterSpace))

  def concatenateWithAnd(otherWords: ArabicWord*): ArabicWord =
    concatWith(
      otherWords,
      Seq(
        ArabicLetters.LetterSpace,
        ArabicLetters.LetterWaw,
        ArabicLetters.LetterSpace
      )
    )

  /** Creates new word by replacing `diacritics` at given `index`.
    *
    * @param index
    *   index of letter
    * @param diacritics
    *   new diacritics
    * @return
    *   new word
    */
  def replaceDiacritics(index: Int, diacritics: DiacriticType*): ArabicWord =
    replaceDiacriticsAndAppend(index, diacritics)

  /** Creates new word by replacing `diacritics` at given `index` and append letters.
    *
    * @param index
    *   index of letter
    * @param diacritics
    *   new diacritics
    * @param lettersToAppend
    *   letters to append
    * @return
    *   new word
    */
  def replaceDiacriticsAndAppend(
    index: Int,
    diacritics: Seq[DiacriticType],
    lettersToAppend: ArabicLetter*
  ): ArabicWord = {
    val buffer = letters.toBuffer
    buffer.update(index, buffer(index).replace(diacritics*))
    buffer ++= lettersToAppend
    ArabicWord(buffer.toSeq*)
  }

  /** Create new word by prepending letters.
    *
    * @param lettersToPrepend
    *   letters to prepend
    * @return
    *   new word
    */
  def prependLetters(lettersToPrepend: ArabicLetter*): ArabicWord = {
    val buffer = lettersToPrepend.toBuffer
    buffer ++= letters.toBuffer
    ArabicWord(buffer.toSeq*)
  }

  /** Create new word by appending letters.
    *
    * @param lettersToAppend
    *   letters to append
    * @return
    *   new word
    */
  def appendLetters(lettersToAppend: ArabicLetter*): ArabicWord = {
    val buffer = letters.toBuffer
    buffer ++= lettersToAppend
    ArabicWord(buffer.toSeq*)
  }

  /** Creates new word by replacing letter at the given `index` without replacing its diacritics.
    *
    * @param index
    *   index of letter
    * @param letterType
    *   letter to replace
    * @return
    *   new word
    */
  def replaceLetter(index: Int, letterType: ArabicLetterType): ArabicWord = {
    val buffer = letters.toBuffer
    buffer.update(index, buffer(index).replace(letterType))
    ArabicWord(buffer.toSeq*)
  }

  def replaceLetter(index: Int, letter: ArabicLetter): ArabicWord = {
    val buffer = letters.toBuffer
    buffer.update(index, letter)
    ArabicWord(buffer.toSeq*)
  }

  /** Creates a new word by removing the element at given `index` and append given `letters`.
    *
    * @param index
    *   index of element to remove
    * @param lettersToAppend
    *   letters to append
    * @return
    *   new word
    */
  def removeLetterAndAppend(
    index: Int,
    lettersToAppend: ArabicLetter*
  ): ArabicWord = {
    val buffer = letters.toBuffer
    buffer -= buffer(index)
    buffer ++= lettersToAppend
    ArabicWord(buffer.toSeq*)
  }

  /** Creates a new word by removing the last element and append given `letters`.
    *
    * @param lettersToAppend
    *   letters to append
    * @return
    *   new word
    */
  def removeLastLetterAndAppend(lettersToAppend: ArabicLetter*): ArabicWord =
    removeLetterAndAppend(letters.size - 1, lettersToAppend*)

  /** Creates a new word by removing the firs element.
    */
  def removeFirstLetter(): ArabicWord = ArabicWord(letters.tail*)

  /** Creates a new word by removing the last element.
    */
  def removeLastLetter(): ArabicWord = ArabicWord(letters.dropRight(1)*)

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

  def isEmpty: Boolean = unicode.isEmpty
}

object ArabicWord {

  def apply(): ArabicWord = ArabicWord(Seq.empty[ArabicLetter]*)

  def apply(letters: ArabicLetter*): ArabicWord = new ArabicWord(letters*)

  @targetName("fromArabicLetterType")
  def apply(letterTypes: ArabicLetterType*): ArabicWord =
    ArabicWord(letterTypes.map(ArabicLetter(_))*)

  @targetName("fromString")
  def apply(source: String, fromUnicode: Boolean = true): ArabicWord = {
    def toArabicLetterType(c: Char) =
      if fromUnicode then ArabicLetterType.UnicodesMap.get(c)
      else ArabicLetterType.CodesMap.get(c)

    def toDiacriticType(c: Char) =
      if fromUnicode then DiacriticType.UnicodesMap.get(c)
      else DiacriticType.CodesMap.get(c)

    val trimmedSource = Try(source.trim).toOption.getOrElse("")
    if trimmedSource.isBlank then throw new IllegalArgumentException("source cannot be null or empty")
    else {
      // if first char is diacritic the remove it before processing
      val firstChar = trimmedSource.charAt(0)
      val maybeFirstDiacriticType = toDiacriticType(firstChar)
      val sanitizedSource =
        if maybeFirstDiacriticType.isDefined then trimmedSource.drop(1)
        else trimmedSource

      val letters =
        sanitizedSource
          .foldLeft(
            List.empty[(ArabicLetterType, ListBuffer[DiacriticType])]
          ) { case (ls, c) =>
            val maybeArabicLetterType = toArabicLetterType(c)
            val maybeDiacriticType = toDiacriticType(c)

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
                if c == ' ' then ls :+ (ArabicLetterType.Space, ListBuffer[DiacriticType]())
                else
                  throw new IllegalArgumentException(
                    s"unmapped character: ${Integer.toHexString(c.asDigit)}"
                  )
          }
          .map { case (letter, diacritics) =>
            ArabicLetter(letter, diacritics.toSeq*)
          }

      // add first diacritic, if exists
      val updatedLetters =
        maybeFirstDiacriticType match
          case Some(diacriticType) if letters.nonEmpty =>
            val head = letters.head
            ArabicLetter(
              head.letter,
              head.diacritics :+ diacriticType*
            ) +: letters.tail

          case _ => letters

      ArabicWord(updatedLetters*)
    }
  }
}
