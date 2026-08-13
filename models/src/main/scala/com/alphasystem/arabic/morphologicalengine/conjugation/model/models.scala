package com.alphasystem
package arabic
package morphologicalengine
package conjugation
package model

import arabic.model.{ ArabicLetterType, ArabicWord, JussiveParticle, RootType, VerbType, WeakVerbType }

import java.lang.Enum
import java.util.UUID

case class RootLetters(
  firstRadical: ArabicLetterType,
  secondRadical: ArabicLetterType,
  thirdRadical: ArabicLetterType,
  fourthRadical: Option[ArabicLetterType] = None) {

  def arabicWord: ArabicWord = {
    val word = ArabicWord(firstRadical)
      .concatWithSpace(
        ArabicWord(secondRadical),
        ArabicWord(thirdRadical)
      )
    if fourthRadical.isDefined then word.concatWithSpace(ArabicWord(fourthRadical.get))
    else word
  }

  def stringValue: String = arabicWord.unicode

  def buckWalterString: String = {
    val prefix = s"${firstRadical.code}${secondRadical.code}${thirdRadical.code}"
    fourthRadical.map(l => s"$prefix${l.code}").getOrElse(prefix)
  }

  /** Returns the root letters as a string without spaces. Uses the label property of ArabicLetterType which returns the
    * unicode character.
    */
  def rawString: String = {
    val prefix = s"${firstRadical.label}${secondRadical.label}${thirdRadical.label}"
    fourthRadical.map(l => s"$prefix${l.label}").getOrElse(prefix)
  }
}

case class ConjugationInput(
  id: UUID = UUID.randomUUID(),
  namedTemplate: NamedTemplate,
  conjugationConfiguration: ConjugationConfiguration,
  rootLetters: RootLetters,
  translation: Option[String] = None,
  verbalNounCodes: Seq[String] = Seq.empty) {

  // provided for sorting by Alphabetically
  val rootLettersTuple: (ArabicLetterType, ArabicLetterType, ArabicLetterType, Option[ArabicLetterType]) =
    (rootLetters.firstRadical, rootLetters.secondRadical, rootLetters.thirdRadical, rootLetters.fourthRadical)
}

case class ConjugationConfiguration(
  skipRuleProcessing: Boolean = false,
  removePassiveLine: Boolean = false,
  jussiveParticle: Option[JussiveParticle] = None)

case class ConjugationHeader(
  rootLetters: RootLetters,
  chartMode: ChartMode,
  title: String,
  templateTypeLabel: String,
  weightLabel: String,
  verbTypeLabel: String)

case class ConjugationTuple(singular: String, plural: String, dual: Option[String] = None) {

  def isEmpty: Boolean = singular.isEmpty && plural.isEmpty
}

object ConjugationTuple {
  def apply(singular: String, plural: String, dual: Option[String] = None): ConjugationTuple =
    new ConjugationTuple(singular, plural, dual)

  def apply(singular: ArabicWord, plural: ArabicWord, dual: Option[ArabicWord]): ConjugationTuple =
    new ConjugationTuple(singular.label, plural.label, dual.map(_.label))
}

sealed trait ConjugationGroup

case class NounConjugationGroup(
  nominative: ConjugationTuple,
  accusative: ConjugationTuple,
  genitive: ConjugationTuple)
    extends ConjugationGroup

case class VerbConjugationGroup(
  masculineSecondPerson: ConjugationTuple,
  feminineSecondPerson: ConjugationTuple,
  masculineThirdPerson: Option[ConjugationTuple] = None,
  feminineThirdPerson: Option[ConjugationTuple] = None,
  firstPerson: Option[ConjugationTuple] = None)
    extends ConjugationGroup

case class AbbreviatedConjugation(
  pastTense: String,
  presentTense: String,
  activeParticiple: String,
  imperative: String,
  forbidden: String,
  pastPassiveTense: Option[String] = None,
  presentPassiveTense: Option[String] = None,
  passiveParticiple: Option[String] = None,
  verbalNouns: Seq[String] = Seq.empty[String],
  adverbs: Seq[String] = Seq.empty[String]) {

  val hasPassiveLine: Boolean =
    pastPassiveTense.isDefined || presentPassiveTense.isDefined || passiveParticiple.isDefined
}

case class DetailedConjugation(
  pastTense: VerbConjugationGroup,
  presentTense: VerbConjugationGroup,
  masculineActiveParticiple: NounConjugationGroup,
  feminineActiveParticiple: NounConjugationGroup,
  imperative: VerbConjugationGroup,
  forbidden: VerbConjugationGroup,
  pastPassiveTense: Option[VerbConjugationGroup] = None,
  presentPassiveTense: Option[VerbConjugationGroup] = None,
  masculinePassiveParticiple: Option[NounConjugationGroup] = None,
  femininePassiveParticiple: Option[NounConjugationGroup] = None,
  presentTenseJussive: Option[VerbConjugationGroup] = None,
  presentPassiveTenseJussive: Option[VerbConjugationGroup] = None,
  verbalNouns: Seq[NounConjugationGroup] = Seq.empty[NounConjugationGroup],
  adverbs: Seq[NounConjugationGroup] = Seq.empty[NounConjugationGroup])

case class MorphologicalChart(
  conjugationHeader: ConjugationHeader,
  abbreviatedConjugation: Option[AbbreviatedConjugation] = None,
  detailedConjugation: Option[DetailedConjugation] = None,
  translation: Option[String] = None)

case class ChartMode(
  template: NamedTemplate,
  rootType: RootType,
  verbType: VerbType,
  weakVerbType: Option[WeakVerbType])

enum OutputFormat extends Enum[OutputFormat] {

  case Unicode extends OutputFormat
  case Html extends OutputFormat
  case BuckWalter extends OutputFormat
}

abstract class ConjugationGroupPair[R <: ConjugationGroup, L <: ConjugationGroup](
  val rightTermType: MorphologicalTermType,
  val leftTermType: MorphologicalTermType,
  val rightTerm: R,
  val leftTerm: L)

case class NounConjugationGroupPair(
  rightType: MorphologicalTermType,
  leftType: MorphologicalTermType,
  right: NounConjugationGroup,
  left: NounConjugationGroup)
    extends ConjugationGroupPair[NounConjugationGroup, NounConjugationGroup](
      rightTermType = rightType,
      leftTermType = leftType,
      rightTerm = right,
      leftTerm = left
    )

case class VerbConjugationGroupPair(
  rightType: MorphologicalTermType,
  leftType: MorphologicalTermType,
  right: VerbConjugationGroup,
  left: VerbConjugationGroup)
    extends ConjugationGroupPair[VerbConjugationGroup, VerbConjugationGroup](
      rightTermType = rightType,
      leftTermType = leftType,
      rightTerm = right,
      leftTerm = left
    )
