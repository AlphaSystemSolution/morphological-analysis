package com.alphasystem
package arabic
package examples_generator
package ui
package model

import java.nio.file.Path
import java.util.UUID
import scala.collection.mutable.ListBuffer

case class TableData(id: UUID, tableProperties: TableProperties, row: Seq[Row] = Seq.empty) {
  lazy val tag: String = tableProperties.tag

  def tableDeceleration(separator: String): String = {
    val buffer = new ListBuffer[String]()

    val cols = tableProperties.columnString
    buffer.addOne(s"""cols="$cols"""")

    buffer.addOne(s"""align="center"""")
    buffer.addOne(s"""halign="center"""")
    buffer.addOne(s"""valign="center"""")

    val tableWidth = tableProperties.tableWidth
    if tableWidth < 100 then buffer.addOne(s"""width="$tableWidth%"""")

    buffer.addOne(s"""frame="${tableProperties.frame.name().toLowerCase}"""")
    buffer.addOne(s"""grid="${tableProperties.grid.name().toLowerCase}"""")
    if separator == "!" then buffer.addOne(s"""separator="$separator"""")

    buffer.mkString("[", ", ", "]")
  }
}

case class Row(id: UUID, columns: Seq[ColumnData] = Seq.empty)

object Row {
  def apply(): Row =
    new Row(UUID.randomUUID(), Seq(ColumnData(UUID.randomUUID(), ColumnType.Other, "{nbsp}")))
}

case class ExampleData(examples: Seq[TableData])

case class ExampleRequest(path: Path, exampleData: ExampleData)
