package com.alphasystem
package arabic
package morphologicalengine
package conjugation
package model

import arabic.model.{ ArabicLetterType, ArabicWord, RootType, VerbType, WeakVerbType }

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
  removePassiveLine: Boolean = false)

case class ConjugationHeader(
  rootLetters: RootLetters,
  chartMode: ChartMode,
  title: String,
  templateTypeLabel: String,
  weightLabel: String,
  verbTypeLabel: String)

case class ConjugationTuple(singular: String, plural: String, dual: Option[String] = None)

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
