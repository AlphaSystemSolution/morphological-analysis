package com.alphasystem
package arabic
package examples_generator
package examples

import arabic.model.ArabicLetters.{ ChapterPrefixWord, WordOrnateLeftParenthesis, WordOrnateRightParenthesis }
import arabic.model.{ ArabicLetterType, ArabicWord }
import examples_generator.model.{ ColumnData, ColumnType, HighlightRange, VerseSearchResult }

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
    var finalText = HighLighter.processHighlights(text, highlights)
    finalText = maybeVerseSearchResult
      .map(r => r.startVerseNumber to r.endVerseNumber)
      .getOrElse(Seq.empty)
      .map(vn =>
        ArabicLetterType.OrnateRightParenthesis.htmlCode + ArabicWord(vn).htmlCode + ArabicLetterType
          .OrnateLeftParenthesis
          .htmlCode
      )
      .foldLeft(finalText) { case (text, verseNumber) =>
        text.replaceFirst(ArabicLetterType.EndOfAyah.htmlCode, verseNumber)
      }

    val startSeparator =
      if finalText.startsWith("[") || finalText.startsWith(DefaultMarkup) then NoBreakingSpace else Empty
    val endSeparator = if finalText.endsWith(DefaultMarkup) then NoBreakingSpace else Empty
    val chapterReference = maybeVerseSearchResult
      .filter(_.appendChapterReference)
      .map(buildChapterReference)
      .getOrElse(Empty)
    s"$columnPrefix$separator$markupStart$startSeparator$finalText$endSeparator$MarkupEnd$chapterReference"
  }

  private def buildChapterReference(verseSearchResult: VerseSearchResult) = {
    val chapterName = ArabicWord(verseSearchResult.chapterName).unicode
    val chapterNumber = verseSearchResult.chapterNumber
    val chapterReference =
      WordOrnateRightParenthesis
        .concat(ChapterPrefixWord.concatWithSpace(ArabicWord(verseSearchResult.chapterName)))
        .concatWithSpace(ArabicWord(verseSearchResult.chapterNumber))
        .concat(WordOrnateLeftParenthesis)
        .htmlCode
    s"$NewLine$ArabicSmallMarkupStart$NoBreakingSpace[gray]$DefaultMarkup$chapterReference$DefaultMarkup$NoBreakingSpace$MarkupEnd"
  }
}
