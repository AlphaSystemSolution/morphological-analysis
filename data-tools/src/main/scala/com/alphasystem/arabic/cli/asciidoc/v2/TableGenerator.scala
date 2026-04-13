package com.alphasystem
package arabic
package cli
package asciidoc
package v2

import java.nio.file.{Files, Path}
import scala.annotation.tailrec
import scala.collection.mutable.ListBuffer
import scala.io.Source
import scala.util.Using
import scala.jdk.CollectionConverters.*

object TableGenerator {

  def buildDocument(srcPath: Path, destPath: Path, attributesPath: Option[Path]): Unit = {
    val table = toRequest(srcPath)

    val attributes =
      attributesPath match
        case Some(path) =>
          Using(Source.fromFile(path.toFile))(_.mkString).toOption.getOrElse("")
        case None => ""

    Files.write(destPath, generateTable(table, attributes).asJava)
  }

  private def generateTable(rows: Seq[Row]): Seq[String] = {
    rows.foldLeft(List.empty[String]) { case (ls, row) =>
      val rowText =
        row.columns.foldLeft(List.empty[String]) { case (acc, column) =>
          val columnText =
            column match {
              case Translation(text, columnPrefix)         => s"${columnPrefix.getOrElse("")}|[translation]#$text#"
              case Arabic(text, tokenRanges, columnPrefix) => generateArabicColumn(text, tokenRanges, columnPrefix)
              case OtherColumn(text, columnPrefix)         => s"${columnPrefix.getOrElse("")}|$text"
            }
          acc :+ columnText
        }

      ls ::: (rowText :+ "")
    }
  }

  def generateTable(table: Table, docAttributes: String): Seq[String] = {
    val buffer = ListBuffer(docAttributes)
    buffer.addOne(s"""[cols="${table.columns}", align="center", halign="center", valign="center"]""")
    buffer.addOne("|===").addOne("").addAll(generateTable(table.rows))
    buffer.addOne("|===").toSeq
  }

  private def generateArabicColumn(text: String, tokenRanges: Seq[TokenRange], columnPrefix: Option[String]): String = {
    val tokens = text.trim.split(" ").zipWithIndex.toSeq
    s"${columnPrefix.getOrElse("")}|${encode(tokens, tokenRanges, "")}"
  }

  @tailrec
  private def encode(tokens: Seq[(String, Int)], tokenRanges: Seq[TokenRange], result: String): String = {
    if tokens.isEmpty then {
      var updatedResult = result.trim
      updatedResult = if updatedResult.endsWith("##") then s"$updatedResult{nbsp}" else updatedResult
      s"$updatedResult#"
    }
    else {
      val (token, index) = tokens.head
      val currentIndex = index + 1
      val tokenRange = tokenRanges.headOption.getOrElse(TokenRange(0, 0))
      val (minToken, maxToken, highLightColor) = (tokenRange.minToken, tokenRange.maxToken, tokenRange.highLightColor)
      val encodedText = arabic.model.toHtmlCodeString(token)

      // minToken is equal to currentIndex then begin highlight markup
      val highlightMarkup =
        if minToken == currentIndex then highLightColor.map(color => s"[.highlight-$color]##").getOrElse("##") else ""

      val tail = tokens.tail
      val isLastToken = tail.isEmpty

      var updatedResult = if result.isEmpty && highlightMarkup.nonEmpty then "{nbsp}" else ""
      updatedResult = if result.isEmpty then s"[arabicNormal]#$updatedResult" else result
      updatedResult = s"$updatedResult$highlightMarkup$encodedText "

      // end of markup, if maxToken is equal to currentIndex, or maxToken is -1, and if this is the last token
      val endMarkup = maxToken == currentIndex || (maxToken == -1 && tail.isEmpty)
      updatedResult = if endMarkup then s"${updatedResult.trim}## " else updatedResult

      encode(tail, if endMarkup then tokenRanges.tail else tokenRanges, updatedResult)
    }
  }
}
