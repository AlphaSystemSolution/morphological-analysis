package com.alphasystem
package arabic
package examples_generator
package ui
package utils

import ui.model.{ ExampleData, Row }

import java.nio.file.{ Files, Path }
import scala.collection.mutable.ListBuffer
import scala.jdk.CollectionConverters.*

object DocumentGenerator {

  private[utils] val DefaultAttributes = """// THIS FILE IS AUTO-GENERATED, DO NOT EDIT
                                           |:encoding: utf-8
                                           |:lang: en
                                           |:last-update-label!:
                                           |
                                           |include::ref.adoc[]
                                           |
                                           |//
                                           |""".stripMargin

  private[utils] def buildTable(
    exampleData: ExampleData,
    nestedTable: Boolean = false,
    attributes: String = DefaultAttributes
  ): Seq[String] = {
    val tableRequests = exampleData.examples
    val buffer = ListBuffer(attributes)
    tableRequests.foreach { tableData =>
      val separator = if nestedTable then "!" else "|"
      val tableProperties = tableData.tableProperties
      val tag = tableProperties.tag
      buffer.addOne(s"// tag::$tag[]")
      tableProperties.role.foreach(role => buffer.addOne(s"[.$role]"))
      buffer.addOne(tableData.tableDeceleration(separator)).addOne(s"$separator===").addOne("")
      tableData
        .row
        .zipWithIndex
        .map { case (row, index) =>
          buildRow(separator)(row, index)
        }
        .foreach(buffer.addAll)
      buffer.addOne(s"$separator===").addOne(s"// end::$tag[]").addOne("")
    }
    buffer.toSeq
  }

  private[utils] def buildRow(separator: String)(row: Row, index: Int): Seq[String] =
    s"// Row ${index + 1}" +: row.columns.map(ColumnGenerator.buildColumn(separator)) :+ ""

  def generateDocument(
    exampleData: ExampleData,
    destPath: Path,
    nestedTable: Boolean = false,
    attributes: String = DefaultAttributes
  ): Path = Files.write(destPath, buildTable(exampleData, nestedTable, attributes).asJava)
}
