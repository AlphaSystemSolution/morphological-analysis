package com.alphasystem
package arabic
package morphologicalengine
package conjugation
package model

import arabic.model.{ RootType, VerbType, WeakVerbType }
import morphologicalanalysis.morphology.model.{ ConversationType, GenderType }

import java.lang.Enum

case class RootLetters(
  firstRadical: String,
  secondRadical: String,
  thirdRadical: String,
  fourthRadical: Option[String] = None)

case class ConjugationHeader(
  rootLetters: RootLetters,
  chartMode: ChartMode,
  baseWord: String,
  pastTenseRoot: String,
  presentTenseRoot: String,
  verbalNounRoot: String,
  translation: String,
  title: String,
  typeLabel1: String,
  typeLabel2: String,
  typeLabel3: String)

case class ConjugationTuple(singular: String, plural: String, dual: Option[String] = None)

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
  passiveTense: Option[String] = None,
  masculinePassiveParticiple: Option[String] = None,
  verbalNouns: Seq[String] = Seq.empty[String],
  adverbs: Seq[String] = Seq.empty[String])

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
  detailedConjugation: Option[DetailedConjugation] = None)

case class ConjugationConfiguration(
  skipRuleProcessing: Boolean = false,
  removePassiveLine: Boolean = false,
  skipAbbreviatedConjugation: Boolean = false,
  skipDetailedConjugation: Boolean = false,
  removeAdverbs: Boolean = false) {
  require(Seq(skipAbbreviatedConjugation, skipDetailedConjugation).count(_ == false) != 2)
}

case class ChartMode(
  template: NamedTemplate,
  rootType: RootType,
  verbType: VerbType,
  weakVerbType: WeakVerbType)

enum OutputFormat extends Enum[OutputFormat] {

  case Unicode extends OutputFormat
  case Html extends OutputFormat
  case BuckWalter extends OutputFormat
  case Stream extends OutputFormat
}
