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
    val endSeparator = if finalText.startsWith(DefaultMarkup) then NoBreakingSpace else Empty
    s"$columnPrefix|$ArabicNormalMarkupStart$startSeparator$finalText$endSeparator$MarkupEnd"
  }

  private[examples] def disableEncoding(): Unit = DoNotEncodeText = true

  private[examples] def processText(token: String, highlights: List[Highlight]): String = {
    val tokenInfos = token.split(Space).zipWithIndex.map { case (token, index) => TokenInfo(index, token) }.toSeq
    processHighlights(token, "", highlights.reverse, tokenInfos).trim.replaceAll(Space, NoBreakingSpace)
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
      case Highlight(
            Token(startTokenIndex, maybeStartLocationIndex),
            Token(endTokenIndex, maybeEndLocationIndex),
            color
          ) :: tail =>
        val startLocationIndex = maybeStartLocationIndex.getOrElse(1)
        val endLocationIndex = maybeEndLocationIndex.getOrElse(-1)

        // process the endLocationIndex, find any text that is after the endLocationIndex, no highlights needed for this text,
        // encode and append to result, insert the end markup
        var index = endTokenIndex - 1
        var tokenInfo = tokenInfos(index)
        // sum of lengths of tokens from the beginning until the "index" (endTokenIndex), add "index" for spaces in between tokens
        var sum = tokenInfos.take(index).map(_.tokenLength).sum + index

        val end = if endLocationIndex == -1 then tokenInfo.text.length else endLocationIndex

        println(
          s"Token: $token, $sum, startTokenIndex: $startTokenIndex, startLocationIndex: $startLocationIndex, endTokenIndex:" +
            s" $endTokenIndex, endLocationIndex: $endLocationIndex, end: $end"
        )

        // text from beginning to sum + endLocationIndex, end markup will be inserted after it and will be carried over
        var remainingText = token.substring(0, sum + end)

        // text from sum + endLocationIndex to the end of the token, no highlights needed for this text,
        val nonHighlightedText = token.substring(sum + end)

        println(s"remainingText: $remainingText, nonHighlightedText: $nonHighlightedText")

        // encode and pre-pend to result, add end markup in front
        var updatedResult = s"$DefaultMarkup${encode(nonHighlightedText)}$result"
        println(s"Result 1: $updatedResult")

        // now process the startLocationIndex
        index = startTokenIndex - 1
        tokenInfo = tokenInfos(index)
        // same logic as above, here "index" would be startTokenIndex
        sum = tokenInfos.take(index).map(_.tokenLength).sum + index

        // this is the text that needs to be highlighted, encode and pre-pend to result, add start markup in front
        val textToHighlight = remainingText.substring(sum + startLocationIndex - 1)

        // text from beginning to sum + startLocationIndex - 1, this is the text that will be carried over to the next highlights
        remainingText = remainingText.substring(0, sum + startLocationIndex - 1)

        val startMarkup = color.map(ColoredMarkupStart).getOrElse(DefaultMarkup)
        println(s"remainingText: $remainingText, textToHighlight: $textToHighlight")

        val space = if startLocationIndex == 1 then Space else Empty
        updatedResult = s"$space$startMarkup${RowGenerator.encode(textToHighlight)}$updatedResult"
        println(s"Result 2: $updatedResult")

        processHighlights(remainingText, updatedResult.trim, tail, tokenInfos)
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

  private[examples] def encode(text: String) =
    if text.isBlank || text.startsWith("{") || DoNotEncodeText then text else arabic.model.toHtmlCodeString(text)

  private[examples] case class TokenInfo(index: Int, text: String) {
    lazy val tokenLength: Int = text.length
    lazy val tokens: Seq[String] = text.split("").toSeq
  }
}
