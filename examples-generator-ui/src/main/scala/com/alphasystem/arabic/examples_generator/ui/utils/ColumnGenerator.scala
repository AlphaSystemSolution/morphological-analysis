package com.alphasystem
package arabic
package examples_generator
package ui
package utils

import ui.model.{ ColumnData, ColumnType, HighlightRange }

object ColumnGenerator {

  // for testing purposes only
  private var DoNotEncodeText = false

  private val Empty = ""
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
    columnData.`type` match {
      case ColumnType.Arabic => buildArabicColumn(text, ArabicNormalMarkupStart, columnPrefix, separator, highlights)
      case ColumnType.ArabicBold =>
        buildArabicColumn(text, ArabicBoldMarkupStart, columnPrefix, separator, highlights)
      case ColumnType.ArabicSmall =>
        buildArabicColumn(text, ArabicSmallMarkupStart, columnPrefix, separator, highlights)
      case ColumnType.ArabicTableCaption =>
        buildArabicColumn(text, ArabicTableCaptionMarkupStart, columnPrefix, separator, highlights)
      case ColumnType.Translation => s"$columnPrefix$separator[translation]#$text#"
      case ColumnType.Other       => s"$columnPrefix$separator$text"
    }
  }

  private def buildArabicColumn(
    text: String,
    markupStart: String,
    columnPrefix: String,
    separator: String,
    highlights: List[HighlightRange]
  ) = {
    val finalText = HighLighter.processHighlights(text, highlights)
    val startSeparator =
      if finalText.startsWith("[") || finalText.startsWith(DefaultMarkup) then NoBreakingSpace else Empty
    val endSeparator = if finalText.endsWith(DefaultMarkup) then NoBreakingSpace else Empty
    s"$columnPrefix$separator$markupStart$startSeparator$finalText$endSeparator$MarkupEnd"
  }
}
