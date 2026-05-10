package com.alphasystem
package arabic
package cli
package examples

import arabic.model.ArabicLetterType

import scala.annotation.tailrec
import scala.collection.mutable.ListBuffer

object RowGenerator {

  // for testing purposes only
  private var DoNotEncodeText = false

  private val Empty = ""
  private val NoBreakingSpace = "{nbsp}"
  private val DefaultMarkup = "##"
  private val ArabicNormalMarkupStart = "[arabicNormal]#"
  private val ColoredMarkupStart = (color: String) => s"[$color]$DefaultMarkup"
  private val MarkupEnd = "#"

  def buildRow(row: Row): Seq[String] =
    row.columns.foldLeft(List.empty[String]) { (acc, column) =>
      val verses = column.verses
      val text = verses.map(_.text).mkString(" ")
      val columnPrefix = column.columnPrefix.getOrElse("")
      val columnText =
        column.`type` match {
          case ColumnType.Translation => s"$columnPrefix|[translation]#$text#"
          case ColumnType.Other       => s"$columnPrefix|$text"
          case ColumnType.Arabic      => generateArabicColumn(verses, columnPrefix, column.highlights)
        }
      acc :+ columnText
    }

  private def generateArabicColumn(verses: Seq[Verse], columnPrefix: String, verseHighlights: Seq[VerseRange]) = {
    val markupTexts =
      verses.map { case Verse(verseNumber, text) =>
        val highlights =
          verseHighlights.find(_.verseNumber == verseNumber).flatMap(_.tokenRange).getOrElse(Seq.empty).toList
        encode(text, highlights)
      }

    val finalText = markupTexts.mkString(s"{nbsp}${ArabicLetterType.EndOfAyah.htmlCode}{nbsp}")
    val startSeparator =
      if finalText.startsWith("[") || finalText.startsWith(DefaultMarkup) then NoBreakingSpace else Empty
    val endSeparator = if finalText.startsWith(DefaultMarkup) then NoBreakingSpace else Empty
    s"$columnPrefix|$ArabicNormalMarkupStart$startSeparator$finalText$endSeparator$MarkupEnd"
  }

  private[examples] def disableEncoding(): Unit = DoNotEncodeText = true

  private[examples] def encode(token: String, highlights: List[Highlight]): String = {
    val buffer = ListBuffer[String]()
    buffer.addAll(token.split(" "))
    encode(buffer, highlights.reverse).mkString(NoBreakingSpace)
  }

  @tailrec
  private def encode(tokens: ListBuffer[String], highlights: List[Highlight]): ListBuffer[String] = {
    highlights match {
      case Nil => tokens
      case Highlight(
            Token(startTokenIndex, maybeStartLocationIndex),
            Token(endTokenIndex, maybeEndLocationIndex),
            color
          ) :: tail =>
        val startLocationIndex = maybeStartLocationIndex.getOrElse(1)
        val endLocationIndex = maybeEndLocationIndex.getOrElse(-1)

        // first process the endLocationIndex and insert the end markup
        val endIndex = endTokenIndex - 1
        val updatedEndText = insertEndMarkup(endTokenIndex, endLocationIndex, tokens(endIndex))
        tokens.update(endIndex, updatedEndText)

        // now process the startLocationIndex and insert the start markup
        val startIndex = startTokenIndex - 1
        val updatedStartText = insertStartMarkup(startTokenIndex, startLocationIndex, tokens(startIndex), color)
        tokens.update(startIndex, updatedStartText)

        encode(tokens, tail)
    }
  }

  private def insertStartMarkup(tokenIndex: Int, locationIndex: Int, text: String, maybeColor: Option[String]) = {
    val tokens = text.split("").toSeq
    val textBefore =
      if locationIndex == 1 then getTokensWithinBound(1, -1, tokens)
      else getTokensWithinBound(1, locationIndex - 1, tokens)
    val textAfter = if locationIndex == 1 then "" else getTokensWithinBound(locationIndex, -1, tokens)
    val markup = maybeColor.map(ColoredMarkupStart).getOrElse(DefaultMarkup)
    if locationIndex == 1 then s"$markup$textBefore$textAfter" else s"$textBefore$markup$textAfter"
  }

  private def insertEndMarkup(tokenIndex: Int, locationIndex: Int, text: String) = {
    val tokens = text.split("").toSeq
    val textBefore = getTokensWithinBound(1, locationIndex, tokens)
    val textAfter = if locationIndex == -1 then "" else getTokensWithinBound(locationIndex + 1, -1, tokens)
    s"$textBefore$DefaultMarkup$textAfter"
  }

  private[examples] def getTokensWithinBound(start: Int, end: Int, tokens: Seq[String]): String = {
    if end <= -1 then tokens.drop(start - 1).mkString else tokens.slice(start - 1, end).mkString
  }

  private def encode(text: String) =
    if text.startsWith("{") || DoNotEncodeText then text else arabic.model.toHtmlCodeString(text)
}
