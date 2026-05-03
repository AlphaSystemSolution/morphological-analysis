package com.alphasystem
package arabic
package cli
package morphologicalengine

import arabic.model.ProNoun
import arabic.morphologicalengine.conjugation.model.{ MorphologicalTermType, NamedTemplate, RootLetters }
import arabic.morphologicalengine.conjugation.model.MorphologicalTermType.*

case class SingleConjugationRequest(conjugations: Seq[SingleConjugation])

case class SingleConjugation(
  tag: String,
  morphologicalTermType: MorphologicalTermType,
  request: ConjugationRequest,
  settings: DisplaySettings,
  translations: Option[Map[ProNoun, String]] = None // so far only valid for verbs
)

case class PairedConjugationRequest(conjugations: Seq[PairedConjugation])

case class PairedConjugation(
  tag: String,
  settings: DisplaySettings,
  rightTerm: Option[MorphologicalTermType],
  leftTerm: Option[MorphologicalTermType],
  right: Option[ConjugationRequest],
  left: Option[ConjugationRequest]) {
  validate()

  private def validate(): Unit = {
    require(
      (rightTerm.isDefined && right.isDefined) || (leftTerm.isDefined && left.isDefined),
      "Either right or left request must be defined"
    )
    (rightTerm, leftTerm) match
      case (Some(r), Some(l)) => require(PairedConjugation.hasSimilarTypes(r, l), "Both right and left term should be of similar types")
      case _                  => // all other cases are validated
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
    PassiveParticipleFeminine
  )

  private def hasSimilarTypes(rightTerm: MorphologicalTermType, leftTerm: MorphologicalTermType) = {
    (tenseTerms.contains(rightTerm) && tenseTerms.contains(leftTerm)) ||
    (imperativeAndForbiddenTerms.contains(rightTerm) && imperativeAndForbiddenTerms.contains(rightTerm)) ||
      (nounTerms.contains(rightTerm) && nounTerms.contains(leftTerm))
  }
}

case class ConjugationRequest(
  namedTemplate: NamedTemplate,
  rootLetters: RootLetters,
  verbalNouns: Option[Seq[String]] = None)

case class DisplaySettings(
  showPronouns: Option[Boolean] = None, // valid for verbs only
  showNumbers: Option[Boolean] = None, // valid for both verbs and nouns, numbers header
  showGenders: Option[Boolean] = None, // valid for verbs only
  showConversationTypes: Option[Boolean] = None, // valid for verbs only
  showNounStatus: Option[Boolean] = None // valid for nouns only, nominative, accusative, and genitive
)
