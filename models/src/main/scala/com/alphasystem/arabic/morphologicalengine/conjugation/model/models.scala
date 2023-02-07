package com.alphasystem
package arabic
package morphologicalengine
package conjugation
package model

import arabic.model.{ ArabicLetterType, ArabicSupportEnum, ArabicWord, RootType, VerbType, WeakVerbType }
import morphologicalanalysis.morphology.model.{ ConversationType, Flexibility, GenderType }

import java.lang.Enum
import java.util.UUID

case class RootLetter(letter: ArabicLetterType, index: Int)

case class RootLetters(
  firstRadical: RootLetter,
  secondRadical: RootLetter,
  thirdRadical: RootLetter,
  fourthRadical: Option[RootLetter] = None)

trait RootWordSupport extends ArabicSupportEnum {

  val rootWord: RootWord

  override lazy val word: ArabicWord = rootWord.derivedWord
}

trait VerbSupport extends RootWordSupport

trait NounSupport extends RootWordSupport {

  val feminine: Boolean
  val flexibility: Flexibility
}

case class ConjugationTuple(singular: String, plural: String, dual: Option[String] = None)

sealed trait ConjugationGroup(val id: String, val termType: MorphologicalTermType)

case class NounConjugationGroup(
  override val id: String = UUID.randomUUID().toString,
  override val termType: MorphologicalTermType,
  nominative: ConjugationTuple,
  accusative: ConjugationTuple,
  genitive: ConjugationTuple)
    extends ConjugationGroup(id, termType)

case class VerbConjugationGroup(
  override val id: String = UUID.randomUUID().toString,
  override val termType: MorphologicalTermType,
  masculineSecondPerson: ConjugationTuple,
  feminineSecondPerson: ConjugationTuple,
  masculineThirdPerson: Option[ConjugationTuple] = None,
  feminineThirdPerson: Option[ConjugationTuple] = None,
  firstPerson: Option[ConjugationTuple] = None)
    extends ConjugationGroup(id, termType)

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
