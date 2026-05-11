package com.alphasystem
package arabic
package cli
package morphologicalengine

import arabic.morphologicalengine.generator.model.{ ChartConfiguration, ConjugationTemplate }
import io.circe.{ Decoder, Encoder }
import io.circe.generic.semiauto.{ deriveDecoder, deriveEncoder }

import java.nio.file.Path

import arabic.model.ProNoun
import arabic.morphologicalengine.conjugation.model.{
  ConjugationConfiguration,
  ConjugationInput,
  MorphologicalTermType,
  NamedTemplate,
  RootLetters
}
import arabic.morphologicalengine.conjugation.model.MorphologicalTermType.*

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
  fromFile(path, fromString[ConjugationTemplate])

case class SingleConjugationRequest(conjugations: Seq[SingleConjugation])

case class SingleConjugation(tag: String, settings: DisplaySettings, request: ConjugationRequest)

case class PairedConjugationRequest(conjugations: Seq[PairedConjugation])

case class PairedConjugation(
  tag: String,
  settings: DisplaySettings,
  right: Option[ConjugationRequest],
  left: Option[ConjugationRequest]) {
  validate()

  import PairedConjugation.*

  private def validate(): Unit = {
    require(right.isDefined || left.isDefined, "Either right or left request must be defined")
    (left, right) match
      case (Some(l), Some(r)) =>
        require(
          hasSimilarTypes(l.morphologicalTermType, r.morphologicalTermType),
          "Both right and left term should be of similar types"
        )
      case _ => // all other cases are validated
  }
}

object PairedConjugation {
  private val tenseTerms = Seq(PastTense, PresentTense, PastPassiveTense, PresentPassiveTense)
  private val imperativeAndForbiddenTerms = Seq(Imperative, Forbidden)
  private val nounTerms = Seq(
    VerbalNoun,
    ActiveParticipleMasculine,
    ActiveParticipleFeminine,
    PassiveParticipleMasculine,
    PassiveParticipleFeminine,
    NounOfPlaceAndTime
  )

  def hasSimilarTypes(leftTerm: MorphologicalTermType, rightTerm: MorphologicalTermType): Boolean = {
    (tenseTerms.contains(rightTerm) && tenseTerms.contains(leftTerm)) ||
    (imperativeAndForbiddenTerms.contains(rightTerm) && imperativeAndForbiddenTerms.contains(leftTerm)) ||
    (nounTerms.contains(rightTerm) && nounTerms.contains(leftTerm))
  }

  def isTenseTerm(term: MorphologicalTermType): Boolean = tenseTerms.contains(term)
  def isImperativeOrForbidden(term: MorphologicalTermType): Boolean = imperativeAndForbiddenTerms.contains(term)
  def isNounTerm(term: MorphologicalTermType): Boolean = nounTerms.contains(term)
}

case class ConjugationRequest(
  morphologicalTermType: MorphologicalTermType,
  namedTemplate: NamedTemplate,
  rootLetters: RootLetters,
  verbalNouns: Option[Seq[String]] = None,
  translations: Option[Map[ProNoun, String]] = None // so far only valid for verbs
) {

  def toConjugationInput: ConjugationInput =
    ConjugationInput(
      namedTemplate = namedTemplate,
      conjugationConfiguration = ConjugationConfiguration(),
      rootLetters = rootLetters,
      translation = None,
      verbalNounCodes = verbalNouns.getOrElse(Seq.empty)
    )
}

case class DisplaySettings(
  showPronouns: Option[Boolean] = None, // valid for verbs only
  showNumbers: Option[Boolean] = None, // valid for both verbs and nouns, numbers header
  showGenders: Option[Boolean] = None, // valid for verbs only
  showConversationTypes: Option[Boolean] = None, // valid for verbs only
  showNounStatus: Option[Boolean] = None, // valid for nouns only, nominative, accusative, and genitive
  showTermTypeCaption: Option[Boolean] = None, // valid only for detailed chart
  tableWidth: Option[Int] = None // valid only for single conjugation tables
)
