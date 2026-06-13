package com.alphasystem
package arabic
package cli
package examples

import scala.collection.mutable.ListBuffer

object TableGenerator {

  def generateTable(table: Table): Seq[String] = {
    val tag = table.tag
    val buffer = ListBuffer(s"// tag::$tag[]")

    val tableSeparator = if table.nestedTable then "!" else "|"
    buffer
      .addOne(
        s"""[cols="${table.columns}", align="center", halign="center", valign="center", separator="$tableSeparator"]"""
      )
      .addOne(s"$tableSeparator===")
      .addOne("")
      .addAll(table.rows.map(RowGenerator.buildRow(tableSeparator)).flatMap(s => s :+ ""))
      .addOne(s"$tableSeparator===")
      .addOne(s"// end::$tag[]")
      .addOne("")
      .toSeq
  }
}
