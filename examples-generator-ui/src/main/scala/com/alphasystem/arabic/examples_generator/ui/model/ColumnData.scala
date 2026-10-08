package com.alphasystem
package arabic
package examples_generator
package ui
package model

case class ColumnData(
  `type`: ColumnType,
  text: String,
  settings: Option[ColumnSettings] = None,
  verseSearchResult: Option[VerseSearchResult] = None,
  highlights: Seq[HighlightRange] = Seq.empty)

enum ColumnType extends Enum[ColumnType] {
  case Arabic, ArabicBold, ArabicSmall, ArabicTableCaption, Translation, Other
}

enum HorizontalAlignment(val value: String) extends Enum[HorizontalAlignment] {
  case Default extends HorizontalAlignment("")
  case Left extends HorizontalAlignment("<")
  case Right extends HorizontalAlignment(">")
  case Center extends HorizontalAlignment("^")
}

enum VerticalAlignment(val value: String) extends Enum[VerticalAlignment] {
  case Default extends VerticalAlignment("")
  case Top extends VerticalAlignment(".<")
  case Bottom extends VerticalAlignment(".>")
  case Center extends VerticalAlignment(".^")
}

case class ColumnSettings(
  colSpan: Int = 1,
  rowSpan: Int = 1,
  horizontalAlignment: HorizontalAlignment = HorizontalAlignment.Left,
  verticalAlignment: VerticalAlignment = VerticalAlignment.Center)

case class TokenHighLight(index: Int, locationIndex: Option[Int] = None)
case class HighlightRange(tokenStart: TokenHighLight, tokenEnd: TokenHighLight, color: Option[String] = None)
