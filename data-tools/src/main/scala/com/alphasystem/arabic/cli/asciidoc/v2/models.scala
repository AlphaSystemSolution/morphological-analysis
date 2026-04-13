package com.alphasystem
package arabic
package cli
package asciidoc
package v2

sealed trait Column {
  def text: String
  def columnPrefix: Option[String]
}

case class Translation(text: String, columnPrefix: Option[String] = None) extends Column

case class TokenRange(minToken: Int, maxToken: Int, highLightColor: Option[String] = None)

case class Arabic(text: String, tokenRanges: Seq[TokenRange], columnPrefix: Option[String] = None) extends Column

case class OtherColumn(text: String, columnPrefix: Option[String] = None) extends Column

case class Row(columns: Seq[Column])

case class Table(columns: String, rows: Seq[Row])