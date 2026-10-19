package com.alphasystem
package arabic
package examples_generator
package ui
package utils

import arabic.model.ArabicLetters.{
  ChapterPrefixWord,
  WordOrnateLeftParenthesis,
  WordOrnateRightParenthesis,
  WordSemiColon
}
import arabic.model.ArabicWord
import ui.model.{ ColumnData, ColumnType, HighlightRange, VerseSearchResult }

object ColumnGenerator {

  private val Space = " "
  private val Empty = ""
  private val NewLine = System.lineSeparator()
  private val NoBreakingSpace = "{nbsp}"
  private val DefaultMarkup = "##"
  private val ArabicBoldMarkupStart = "[arabicBold]#"
  private val ArabicSmallMarkupStart = "[arabicSmall]#"
  private val ArabicTableCaptionMarkupStart = "[arabicTableCaption]#"
  private val ArabicNormalMarkupStart = "[arabicNormal]#"
  private val MarkupEnd = "#"

  def buildColumn(separator: String)(columnData: ColumnData): String = {
    val text = columnData.text
    val columnPrefix = columnData.columnPrefix
    val highlights = columnData.highlights.toList
    val verseSearchResult = columnData.verseSearchResult
    columnData.`type` match {
      case ColumnType.Arabic =>
        buildArabicColumn(text, ArabicNormalMarkupStart, columnPrefix, separator, verseSearchResult, highlights)
      case ColumnType.ArabicBold =>
        buildArabicColumn(text, ArabicBoldMarkupStart, columnPrefix, separator, verseSearchResult, highlights)
      case ColumnType.ArabicSmall =>
        buildArabicColumn(text, ArabicSmallMarkupStart, columnPrefix, separator, verseSearchResult, highlights)
      case ColumnType.ArabicTableCaption =>
        buildArabicColumn(text, ArabicTableCaptionMarkupStart, columnPrefix, separator, verseSearchResult, highlights)
      case ColumnType.Translation => s"$columnPrefix$separator[translation]#$text#"
      case ColumnType.Other       => s"$columnPrefix$separator$text"
    }
  }

  private def buildArabicColumn(
    text: String,
    markupStart: String,
    columnPrefix: String,
    separator: String,
    maybeVerseSearchResult: Option[VerseSearchResult],
    highlights: List[HighlightRange]
  ) = {
    val finalText = HighLighter.processHighlights(text, highlights)
    val startSeparator =
      if finalText.startsWith("[") || finalText.startsWith(DefaultMarkup) then NoBreakingSpace else Empty
    val endSeparator = if finalText.endsWith(DefaultMarkup) then NoBreakingSpace else Empty
    val verseReference = buildVerseReference(maybeVerseSearchResult)
    s"$columnPrefix$separator$markupStart$startSeparator$finalText$endSeparator$MarkupEnd$verseReference"
  }

  private def buildVerseReference(maybeVerseSearchResult: Option[VerseSearchResult]) = {
    maybeVerseSearchResult
      .map { verseSearchResult =>
        val chapterName = ArabicWord(verseSearchResult.chapterName).unicode
        val chapterNumber = verseSearchResult.chapterNumber
        val startVerseNumber = verseSearchResult.startVerseIndex
        val endVerseNumber = verseSearchResult.endVerseIndex
        val verseNumberWord =
          if startVerseNumber == endVerseNumber then ArabicWord(startVerseNumber)
          else ArabicWord(startVerseNumber).concat(WordSemiColon).concat(ArabicWord(endVerseNumber))

        val verseReference =
          WordOrnateRightParenthesis
            .concat(ChapterPrefixWord.concatWithSpace(ArabicWord(verseSearchResult.chapterName)))
            .concatWithSpace(ArabicWord(verseSearchResult.chapterNumber).concat(WordSemiColon).concat(verseNumberWord))
            .concat(WordOrnateLeftParenthesis)
            .htmlCode
        s"$NewLine$NewLine$ArabicSmallMarkupStart$NoBreakingSpace[.gray]$DefaultMarkup$verseReference$DefaultMarkup$NoBreakingSpace$MarkupEnd"
      }
      .getOrElse(Empty)
  }
}
