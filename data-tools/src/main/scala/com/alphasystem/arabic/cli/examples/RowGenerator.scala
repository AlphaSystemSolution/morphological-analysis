package com.alphasystem
package arabic
package cli
package examples

import arabic.model.ArabicLetterType

import scala.annotation.tailrec

object RowGenerator {

  // for testing purposes only
  private var DoNotEncodeText = false

  private val Empty = ""
  private val Space = " "
  private val NoBreakingSpace = "{nbsp}"
  private val DefaultMarkup = "##"
  private val ArabicNormalMarkupStart = "[arabicNormal]#"
  private val ColoredMarkupStart = (color: String) => s"[$color]$DefaultMarkup"
  private val MarkupEnd = "#"

  def buildRow(row: Row): Seq[String] =
    row.columns.foldLeft(List.empty[String]) { (acc, column) =>
      val verses = column.verses
      val text = verses.map(_.text).mkString(Space)
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
        processText(text, highlights)
      }

    val finalText = markupTexts.mkString(s"{nbsp}${ArabicLetterType.EndOfAyah.htmlCode}{nbsp}")
    val startSeparator =
      if finalText.startsWith("[") || finalText.startsWith(DefaultMarkup) then NoBreakingSpace else Empty
    val endSeparator = if finalText.endsWith(DefaultMarkup) then NoBreakingSpace else Empty
    s"$columnPrefix|$ArabicNormalMarkupStart$startSeparator$finalText$endSeparator$MarkupEnd"
  }

  private[examples] def disableEncoding(): Unit = DoNotEncodeText = true

  private[examples] def processText(token: String, highlights: List[Highlight]): String = {
    val tokenInfos = token.split(Space).zipWithIndex.map { case (token, index) => TokenInfo(index, token) }.toSeq
    processHighlights(token, "", highlights.reverse, tokenInfos).trim
  }

  @tailrec
  private def processHighlights(
    token: String,
    result: String,
    highlights: List[Highlight],
    tokenInfos: Seq[TokenInfo]
  ): String = {
    highlights match {
      case Nil if token.isBlank => result
      case Nil                  => s"${encode(token)}$result"
      case Highlight(tokenStart, tokenEnd, color) :: tail =>
        val startOffset = tokenOffset(tokenStart, tokenInfos)
        val endOffset = tokenOffset(tokenEnd, tokenInfos, endOffset = true)

        val beforeHighlight = token.substring(0, startOffset)
        val highlightedText = token.substring(startOffset, endOffset)
        val afterHighlight = token.substring(endOffset)

        val startMarkup = color.map(ColoredMarkupStart).getOrElse(DefaultMarkup)
        val leadingSpace = if tokenStart.locationIndex.getOrElse(1) == 1 then Space else Empty

        val updatedResult =
          s"$leadingSpace$startMarkup${encode(highlightedText)}$DefaultMarkup${encode(afterHighlight)}$result"

        processHighlights(beforeHighlight, updatedResult.trim, tail, tokenInfos)
    }
  }

  private def tokenOffset(token: Token, tokenInfos: Seq[TokenInfo], endOffset: Boolean = false): Int = {
    val providedTokenIndex = token.index
    val tokenIndex = if endOffset && providedTokenIndex <= -1 then tokenInfos.length else providedTokenIndex
    val index = tokenIndex - 1
    val tokenInfo = tokenInfos(index)
    val previousTokensLength = tokenInfos.take(index).map(_.tokenLength).sum
    val previousSpacesLength = index

    val locationIndex =
      token.locationIndex.getOrElse {
        if endOffset then tokenInfo.text.length
        else 1
      }

    previousTokensLength + previousSpacesLength + locationIndex - Option.when(!endOffset)(1).getOrElse(0)
  }

  private[examples] def getTokensWithinBound(start: Int, end: Int, tokens: Seq[String]): String = {
    if end <= -1 then tokens.drop(start - 1).mkString else tokens.slice(start - 1, end).mkString
  }

  private def encode(text: String) =
    if text.isBlank || text.startsWith("{") || DoNotEncodeText then text else arabic.model.toHtmlCodeString(text)

  private case class TokenInfo(index: Int, text: String) {
    lazy val tokenLength: Int = text.length
    lazy val tokens: Seq[String] = text.split("").toSeq
  }
}
