package com.alphasystem
package arabic
package examples_generator

import io.circe.{Decoder, Encoder, HCursor, Json}
import io.circe.generic.auto.*

import java.nio.file.Path
import scala.util.{Failure, Success, Try}

package object model {

  given ColumnTypeEncoder: Encoder[ColumnType] =
    (a: ColumnType) => Json.fromString(a.name)

  given ColumnTypeDecoder: Decoder[ColumnType] =
    (c: HCursor) =>
      Try(ColumnType.valueOf(c.value.asString.get)) match
        case Failure(ex)    => exceptionToDecodingFailure(ex, c)
        case Success(value) => Right(value)

  given FrameEncoder: Encoder[Frame] =
    (a: Frame) => Json.fromString(a.name)

  given FrameDecoder: Decoder[Frame] =
    (c: HCursor) =>
      Try(Frame.valueOf(c.value.asString.get)) match
        case Failure(ex)    => exceptionToDecodingFailure(ex, c)
        case Success(value) => Right(value)

  given GridEncoder: Encoder[Grid] =
    (a: Grid) => Json.fromString(a.name)

  given GridDecoder: Decoder[Grid] =
    (c: HCursor) =>
      Try(Grid.valueOf(c.value.asString.get)) match
        case Failure(ex)    => exceptionToDecodingFailure(ex, c)
        case Success(value) => Right(value)

  given HorizontalAlignmentEncoder: Encoder[HorizontalAlignment] =
    (a: HorizontalAlignment) => Json.fromString(a.name)

  given HorizontalAlignmentDecoder: Decoder[HorizontalAlignment] =
    (c: HCursor) =>
      Try(HorizontalAlignment.valueOf(c.value.asString.get)) match
        case Failure(ex)    => exceptionToDecodingFailure(ex, c)
        case Success(value) => Right(value)

  given VerticalAlignmentEncoder: Encoder[VerticalAlignment] =
    (a: VerticalAlignment) => Json.fromString(a.name)

  given VerticalAlignmentDecoder: Decoder[VerticalAlignment] =
    (c: HCursor) =>
      Try(VerticalAlignment.valueOf(c.value.asString.get)) match
        case Failure(ex)    => exceptionToDecodingFailure(ex, c)
        case Success(value) => Right(value)

  given ColorEncoder: Encoder[Color] =
    (a: Color) => Json.fromString(a.name)

  given ColorDecoder: Decoder[Color] =
    (c: HCursor) =>
      Try(Color.valueOf(c.value.asString.get)) match
        case Failure(ex)    => exceptionToDecodingFailure(ex, c)
        case Success(value) => Right(value)

  def fromExampleData(exampleData: ExampleData): String = toYaml(exampleData)

  def toExampleData(path: Path): ExampleData = fromFile(path)
}
