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
case class HighlightRange(tokenStart: TokenHighLight, tokenEnd: TokenHighLight, color: Option[Color] = None) {
  def stringValue: String = {
    val locationStartIndex = tokenStart.locationIndex.map(i => s":$i").getOrElse("")
    val locationEndIndex = tokenStart.locationIndex.map(i => s":$i").getOrElse("")
    s"(${tokenStart.index}$locationStartIndex, ${tokenEnd.index}$locationEndIndex)"
  }
}

enum Color(val colorName: String, val value: String) extends Enum[Color] {
  case Default extends Color("None", "")
  case Aqua extends Color("aqua", "#00FFFF")
  case Black extends Color("black", "#000000")
  case Blue extends Color("blue", "#0000FF")
  case Cyan extends Color("cyan", "#00FFFF")
  case Fuchsia extends Color("fuchsia", "#FF0080")
  case Gray extends Color("gray", "#808080")
  case Grey extends Color("grey", "#808080")
  case Green extends Color("green", "#00FF00")
  case Lime extends Color("lime", "#32CD32")
  case Magenta extends Color("magenta", "#FF00FF")
  case Maroon extends Color("maroon", "#800000")
  case Navy extends Color("navy", "#000080")
  case Olive extends Color("olive", "#808000")
  case Purple extends Color("purple", "#800080")
  case Red extends Color("red", "#FF0000")
  case Silver extends Color("silver", "#C0C0C0")
  case Teal extends Color("teal", "#008080")
  case White extends Color("white", "#FFFFFF")
  case Yellow extends Color("yellow", "#FFFF00")
}
