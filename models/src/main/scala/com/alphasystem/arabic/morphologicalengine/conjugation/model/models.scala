package com.alphasystem
package arabic
package morphologicalengine
package conjugation
package model

import arabic.model.{ RootType, VerbType, WeakVerbType }
import morphologicalanalysis.morphology.model.{ ConversationType, GenderType }

import java.lang.Enum

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
  masculinePassiveParticiple: Option[VerbConjugationGroup] = None,
  femininePassiveParticiple: Option[VerbConjugationGroup] = None,
  verbalNouns: Seq[NounConjugationGroup] = Seq.empty[NounConjugationGroup],
  adverbs: Seq[NounConjugationGroup] = Seq.empty[NounConjugationGroup])

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
