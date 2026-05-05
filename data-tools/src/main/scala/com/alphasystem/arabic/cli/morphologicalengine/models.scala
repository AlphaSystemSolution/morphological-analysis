package com.alphasystem
package arabic
package cli
package morphologicalengine

import arabic.model.ProNoun
import arabic.morphologicalengine.conjugation.model.{
  ConjugationConfiguration,
  ConjugationInput,
  MorphologicalTermType,
  NamedTemplate,
  RootLetters
}
import arabic.morphologicalengine.conjugation.model.MorphologicalTermType.*

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
