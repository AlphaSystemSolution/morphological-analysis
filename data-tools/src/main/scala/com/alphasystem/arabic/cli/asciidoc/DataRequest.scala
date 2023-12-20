package com.alphasystem
package arabic
package cli
package asciidoc

case class DataRequest(columns: String, translationColumnIndex: Int, arabicColumnIndex: Int, requests: Seq[Column])

case class ColumnInfo(index: Int, value: String)

case class Column(
  translation: String,
  request: SearchRequest,
  highlightedTokens: Seq[VerseSearch] = Seq.empty,
  remainingColumns: Seq[ColumnInfo] = Seq.empty)
