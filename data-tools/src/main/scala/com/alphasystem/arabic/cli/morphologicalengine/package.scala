package com.alphasystem
package arabic
package cli

import arabic.morphologicalengine.generator.model.{ ChartConfiguration, ConjugationTemplate }
import arabic.morphologicalengine.conjugation.model.{ ConjugationConfiguration, ConjugationInput, RootLetters }
import io.circe.generic.semiauto.{ deriveDecoder, deriveEncoder }
import io.circe.{ Decoder, Encoder }

import java.nio.file.Path

package object morphologicalengine {

  given Decoder[SingleConjugation] = deriveDecoder
  given Encoder[SingleConjugation] = deriveEncoder
  given Decoder[SingleConjugationRequest] = deriveDecoder
  given Encoder[SingleConjugationRequest] = deriveEncoder
  given Decoder[PairedConjugationRequest] = deriveDecoder
  given Encoder[PairedConjugationRequest] = deriveEncoder
  given Decoder[PairedConjugation] = deriveDecoder
  given Encoder[PairedConjugation] = deriveEncoder
  given Decoder[ConjugationRequest] = deriveDecoder
  given Encoder[ConjugationRequest] = deriveEncoder
  given Decoder[RootLetters] = deriveDecoder
  given Encoder[RootLetters] = deriveEncoder
  given Decoder[ChartConfiguration] = deriveDecoder
  given Encoder[ChartConfiguration] = deriveEncoder
  given Decoder[ConjugationConfiguration] = deriveDecoder
  given Encoder[ConjugationConfiguration] = deriveEncoder
  given Decoder[ConjugationInput] = deriveDecoder
  given Encoder[ConjugationInput] = deriveEncoder
  given Decoder[ConjugationTemplate] = deriveDecoder
  given Encoder[ConjugationTemplate] = deriveEncoder
  given Decoder[DisplaySettings] = deriveDecoder
  given Encoder[DisplaySettings] = deriveEncoder

  private[cli] def toSingleConjugationRequest(path: Path): SingleConjugationRequest =
    fromFile(path, fromString[SingleConjugationRequest])

  private[cli] def toPairedConjugationRequest(path: Path): PairedConjugationRequest =
    fromFile(path, fromString[PairedConjugationRequest])

  private[cli] def toConjugationTemplate(path: Path): ConjugationTemplate =
    fromFile(path,  fromString[ConjugationTemplate])

}
