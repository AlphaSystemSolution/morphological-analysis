package com.alphasystem
package arabic
package cli
package examples

import io.circe.{ Decoder, Encoder, HCursor, Json }
import io.circe.generic.auto.*

import java.nio.file.Path
import scala.util.{ Failure, Success, Try }

enum ColumnType extends Enum[ColumnType] {
  case Translation, Arabic, ArabicSmall, ArabicTableCaption, Other
}

case class ExampleRequest(examples: Seq[TableRequest])

case class TableRequest(tag: String, columns: String, rows: Seq[RowRequest], nestedTable: Option[Boolean] = None)

case class RowRequest(inputs: Seq[Input])

case class Table(tag: String, columns: String, nestedTable: Boolean, rows: Seq[Row])

case class Row(columns: Seq[Column])

case class Column(
  `type`: ColumnType,
  verses: Seq[Verse],
  columnPrefix: Option[String] = None,
  highlights: Seq[VerseRange] = Seq.empty)

case class Input(
  `type`: ColumnType,
  text: Option[Verse] = None,
  searchRequest: Option[SearchRequest] = None,
  columnPrefix: Option[String] = None,
  highlights: Option[Seq[VerseRange]] = None) {
  require(text.isDefined || searchRequest.isDefined, "Either text or searchRequest must be provided")
}

case class SearchRequest(chapterNumber: Int, verses: Seq[VerseRequest]) {
  validate()

  private def validate(): Unit = {
    val verseNumbers = verses.map(_.verseNumber).sorted
    val min = verseNumbers.headOption.getOrElse(-1)
    val max = verseNumbers.lastOption.getOrElse(-1)
    require(chapterNumber >= 1 && min >= 1 && max >= 1 && min <= max && verseNumbers.sum == (min to max).sum)
  }
}

case class Verse(verseNumber: Int, text: String)

// Verse number to search, if token range provided then get tokens within that range
case class VerseRequest(verseNumber: Int, tokenStart: Option[Int] = None, tokenEnd: Option[Int] = None) {
  require(
    tokenStart.getOrElse(1) >= 1 && (tokenEnd.getOrElse(-1) >= tokenStart.getOrElse(1) || tokenEnd.getOrElse(-1) <= -1),
    s"Invalid range (${tokenStart.getOrElse(1)}, ${tokenEnd.getOrElse(-1)})"
  )
}

case class VerseRange(verseNumber: Int, tokenRange: Option[List[Highlight]] = None)

case class Token(index: Int, locationIndex: Option[Int] = None)
case class Highlight(tokenStart: Token, tokenEnd: Token, color: Option[String] = None)

given ColumnTypeEncoder: Encoder[ColumnType] =
  (a: ColumnType) => Json.fromString(a.name)

given ColumnTypeDecoder: Decoder[ColumnType] =
  (c: HCursor) =>
    Try(ColumnType.valueOf(c.value.asString.get)) match
      case Failure(ex)    => exceptionToDecodingFailure(ex, c)
      case Success(value) => Right(value)

def toExampleRequest(path: Path): ExampleRequest = fromFile(path, fromString[ExampleRequest])
