package com.alphasystem
package arabic
package cli

import arabic.morphologicalengine.conjugation.forms.noun.{NounSupportBase, VerbalNoun}
import io.circe.{Decoder, Encoder}
import io.circe.generic.auto.*
import io.circe.yaml.v12.parser

package object morphologicalengine {

  given nounSupportBaseEncoder: Encoder[NounSupportBase] = 
    Encoder.encodeString.contramap[NounSupportBase](_.code)

  given nounSupportBaseDecoder: Decoder[NounSupportBase] = 
    Decoder.decodeString.emap { code =>
      VerbalNoun.byCode.get(code) match {
        case Some(nounSupport) => Right(nounSupport.asInstanceOf[NounSupportBase])
        case None => Left(s"Unknown NounSupportBase code: $code")
      }
    }

  def toSingleConjugation(ymlString: String): SingleConjugation =
    parser.parse(ymlString) match {
      case Left(ex) => throw ex
      case Right(value) =>
        value.as[SingleConjugation] match {
          case Left(ex) => throw ex
          case Right(value) => value
        }
    }
}
