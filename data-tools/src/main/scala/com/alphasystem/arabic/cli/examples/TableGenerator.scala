package com.alphasystem
package arabic
package cli
package examples

import scala.collection.mutable.ListBuffer

object TableGenerator {

  def generateTable(table: Table): Seq[String] = {
    val tag = table.tag
    val buffer = ListBuffer(s"// tag::$tag[]")

    buffer
      .addOne(s"""[cols="${table.columns}", align="center", halign="center", valign="center"]""")
      .addOne("|===")
      .addOne("")
      .addAll(table.rows.map(RowGenerator.buildRow).flatMap(s => s :+ ""))
      .addOne("|===")
      .addOne(s"// end::$tag[]")
      .addOne("")
      .toSeq
  }
}
