package com.alphasystem
package arabic
package cli
package examples

import io.circe.{ Decoder, Encoder, HCursor, Json }
import io.circe.generic.auto.*

import java.nio.file.Path
import scala.util.{ Failure, Success, Try }

enum ColumnType extends Enum[ColumnType] {
  case Translation, Arabic, Other
}

case class ExampleRequest(examples: Seq[TableRequest])

case class TableRequest(tag: String, columns: String, rows: Seq[RowRequest])

case class RowRequest(inputs: Seq[Input])

case class Table(tag: String, columns: String, rows: Seq[Row])

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
case class VerseRequest(verseNumber: Int, tokenRange: Option[Bound] = None)

case class VerseRange(verseNumber: Int, tokenRange: Option[List[Highlight]] = None)

case class Bound(start: Int, end: Int) {
  require(start >= 1 && (end >= start || end <= -1), s"Invalid range ($start, $end)")
}

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
