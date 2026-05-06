package com.alphasystem
package arabic
package cli
package asciidoc

import io.circe.generic.auto.*
import io.circe.{ Decoder, Encoder, HCursor, Json }

import java.nio.file.Path
import scala.util.{ Failure, Success, Try }

package object v2 {

  given ColumnTypeEncoder: Encoder[ColumnType] =
    (a: ColumnType) => Json.fromString(a.name)

  given ColumnTypeDecoder: Decoder[ColumnType] =
    (c: HCursor) =>
      Try(ColumnType.valueOf(c.value.asString.get)) match
        case Failure(ex)    => exceptionToDecodingFailure(ex, c)
        case Success(value) => Right(value)

  def toRequest(path: Path): ExampleRequest = fromFile(path, fromString[ExampleRequest])
}
