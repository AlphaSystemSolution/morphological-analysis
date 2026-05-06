package com.alphasystem
package arabic
package cli
package asciidoc
package v2

enum ColumnType extends Enum[ColumnType] {
  case Translation, Arabic, Other
}

case class Column(
  `type`: ColumnType,
  text: String,
  columnPrefix: Option[String] = None,
  tokenRanges: Seq[TokenRange] = Seq.empty)

case class TokenRange(minToken: Int, maxToken: Int, highLightColor: Option[String] = None)

case class Row(columns: Seq[Column])

case class Table(tag: String, columns: String, rows: Seq[Row])

case class ExampleRequest(examples: Seq[Table])
