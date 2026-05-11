package com.alphasystem
package arabic
package cli
package examples

import java.nio.file.{ Files, Path }
import scala.collection.mutable.ListBuffer
import scala.jdk.CollectionConverters.*

object ExampleGenerator {

  private val verseSearch = new cli.examples.VerseSearch()

  def buildDocument(srcPath: Path, destPath: Path, attributes: String): Unit = {
    val tableRequests: Seq[TableRequest] = toExampleRequest(srcPath).examples
    val buffer = ListBuffer(attributes)
    val tables =
      tableRequests.map { tableRequest =>
        val rows =
          tableRequest.rows.map { rowInputs =>
            val columns =
              rowInputs.inputs.map { input =>
                val verses =
                  (input.text, input.searchRequest) match {
                    case (Some(text), _)          => Seq(text)
                    case (_, Some(searchRequest)) => searchVerse(searchRequest.chapterNumber, searchRequest.verses)
                    case _ => throw new IllegalArgumentException("Either text or searchRequest must be present")
                  }
                Column(input.`type`, verses, input.columnPrefix, input.highlights.getOrElse(Seq.empty))
              }
            Row(columns)
          }
        Table(tableRequest.tag, tableRequest.columns, rows)
      }

    tables.foreach(table => buffer.addAll(TableGenerator.generateTable(table)))
    Files.write(destPath, buffer.toSeq.asJava)
  }

  private def searchVerse(chapterNumber: Int, verses: Seq[VerseRequest]) =
    verses.map { case VerseRequest(verseNumber, tokenRange) =>
      Verse(verseNumber, verseSearch.searchVerse(chapterNumber, verseNumber, tokenRange))
    }
}
