package com.alphasystem
package arabic
package examples_generator
package ui
package model

import java.util.UUID

case class TableData(
  tag: String,
  columns: Seq[TableColumnInfo] = Seq.empty,
  tableWidth: Int = 100,
  frame: Frame = Frame.All,
  grid: Grid = Grid.All,
  role: Option[String] = None) {
  require(tag.nonEmpty, "Tag cannot be empty string")
  // require(columns.nonEmpty, "Columns cannot be empty")
}

case class TableColumnInfo(
  id: UUID,
  width: Int,
  horizontalAlignment: HorizontalAlignment = HorizontalAlignment.Center,
  verticalAlignment: VerticalAlignment = VerticalAlignment.Center)

enum Frame extends Enum[Frame] {
  case None, All, Ends, Sides
}

enum Grid extends Enum[Grid] {
  case None, All, Rows, Cols
}
