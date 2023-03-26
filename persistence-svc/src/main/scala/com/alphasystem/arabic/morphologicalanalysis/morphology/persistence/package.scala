package com.alphasystem
package arabic
package morphologicalanalysis
package morphology

import cats.syntax.functor.*
import morphology.model.WordType
import io.circe.*
import io.circe.Decoder.Result
import io.circe.DecodingFailure.Reason
import io.circe.generic.auto.*
import io.circe.syntax.*

import scala.util.{ Failure, Success, Try }

package object persistence {

  given WordTypeEncoder: Encoder[WordType] = (a: WordType) => Json.fromString(a.name)

  given WordTypeDecoder: Decoder[WordType] =
    (c: HCursor) =>
      Try(WordType.valueOf(c.value.asString.get)) match
        case Failure(ex)    => exceptionToDecodingFailure(ex, c)
        case Success(value) => Right(value)
}
