package com.alphasystem
package arabic
package cli

import arabic.morphologicalengine.conjugation.forms.noun.{ NounSupportBase, VerbalNoun }
import io.circe.{ Decoder, Encoder }
import io.circe.generic.auto.*
import io.circe.yaml.v12.parser

import java.nio.file.Path
import scala.io.Source
import scala.util.{ Failure, Success, Using }

package object morphologicalengine {

  given nounSupportBaseEncoder: Encoder[NounSupportBase] =
    Encoder.encodeString.contramap[NounSupportBase](_.code)

  given nounSupportBaseDecoder: Decoder[NounSupportBase] =
    Decoder.decodeString.emap { code =>
      VerbalNoun.byCode.get(code) match {
        case Some(nounSupport) => Right(nounSupport.asInstanceOf[NounSupportBase])
        case None              => Left(s"Unknown NounSupportBase code: $code")
      }
    }

  def toSingleConjugationRequest(path: Path): SingleConjugationRequest =
    Using(Source.fromFile(path.toFile))(source => toSingleConjugationRequest(source.mkString)) match
      case Failure(ex)    => throw ex
      case Success(value) => value

  private[cli] def toSingleConjugationRequest(ymlString: String): SingleConjugationRequest =
    parser.parse(ymlString) match {
      case Left(ex) => throw ex
      case Right(value) =>
        value.as[SingleConjugationRequest] match {
          case Left(ex)     => throw ex
          case Right(value) => value
        }
    }
}
