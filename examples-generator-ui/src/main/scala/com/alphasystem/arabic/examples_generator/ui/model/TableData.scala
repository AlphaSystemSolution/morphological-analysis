package com.alphasystem
package arabic
package examples_generator
package ui
package model

import java.util.UUID

case class TableData(tableProperties: TableProperties, row: Seq[Row] = Seq.empty) {
  lazy val id: String = tableProperties.tag
}

case class Row(id: UUID, columns: Seq[ColumnData] = Seq.empty)
