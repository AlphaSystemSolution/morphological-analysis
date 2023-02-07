package com.alphasystem
package arabic
package morphologicalengine
package conjugation
package model

import arabic.model.{ ArabicLetterType, ArabicSupportEnum, ArabicWord, RootType, VerbType, WeakVerbType }
import morphologicalanalysis.morphology.model.{ ConversationType, GenderType }

import java.lang.Enum

case class RootLetter(letter: ArabicLetterType, index: Int)

case class RootLetters(
  firstRadical: RootLetter,
  secondRadical: RootLetter,
  thirdRadical: RootLetter,
  fourthRadical: Option[RootLetter] = None)

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

enum VerbGroupType(val gender: GenderType, val conversation: ConversationType) {

  case ThirdPersonMasculine extends VerbGroupType(GenderType.Masculine, ConversationType.ThirdPerson)
  case ThirdPersonFeminine extends VerbGroupType(GenderType.Feminine, ConversationType.ThirdPerson)
  case SecondPersonMasculine extends VerbGroupType(GenderType.Masculine, ConversationType.SecondPerson)
  case SecondPersonFeminine extends VerbGroupType(GenderType.Feminine, ConversationType.SecondPerson)
  case FirstPerson extends VerbGroupType(GenderType.Masculine, ConversationType.FirstPerson)
}
