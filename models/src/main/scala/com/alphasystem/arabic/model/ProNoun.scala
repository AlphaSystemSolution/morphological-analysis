package com.alphasystem
package arabic
package model

import ArabicLetterType.*
import DiacriticType.*
import arabic.morphologicalanalysis.morphology.model.{ ConversationType, GenderType, NumberType }
import com.alphasystem.arabic.model.ProNoun.{
  FirstPersonSingular,
  ThirdPersonMasculineDual,
  ThirdPersonMasculinePlural,
  ThirdPersonMasculineSingular
}
import com.alphasystem.arabic.morphologicalanalysis.morphology.model.ConversationType.{
  FirstPerson,
  SecondPerson,
  ThirdPerson
}

import java.lang.Enum

enum ProNoun(
  override val word: ArabicWord,
  val number: NumberType,
  val gender: GenderType,
  val conversation: ConversationType)
    extends Enum[ProNoun]
    with ArabicSupportEnum {

  override val code: String = name()

  case ThirdPersonMasculineSingular
      extends ProNoun(
        word = ArabicWord(ArabicLetter(Ha, Damma), ArabicLetter(Waw, Fatha)),
        number = NumberType.Singular,
        gender = GenderType.Masculine,
        conversation = ConversationType.ThirdPerson
      )

  case ThirdPersonMasculineDual
      extends ProNoun(
        word = ArabicWord(ArabicLetter(Ha, Damma), ArabicLetter(Meem, Fatha), ArabicLetter(Alif)),
        number = NumberType.Dual,
        gender = GenderType.Masculine,
        conversation = ConversationType.ThirdPerson
      )

  case ThirdPersonMasculinePlural
      extends ProNoun(
        word = ArabicWord(ArabicLetter(Ha, Damma), ArabicLetter(Meem, Sukun)),
        number = NumberType.Plural,
        gender = GenderType.Masculine,
        conversation = ConversationType.ThirdPerson
      )

  case ThirdPersonFeminineSingular
      extends ProNoun(
        word = ArabicWord(ArabicLetter(Ha, Kasra), ArabicLetter(Ya, Fatha)),
        number = NumberType.Singular,
        gender = GenderType.Feminine,
        conversation = ConversationType.ThirdPerson
      )

  case ThirdPersonFeminineDual
      extends ProNoun(
        word = ArabicWord(ArabicLetter(Ha, Damma), ArabicLetter(Meem, Fatha), ArabicLetter(Alif)),
        number = NumberType.Dual,
        gender = GenderType.Feminine,
        conversation = ConversationType.ThirdPerson
      )

  case ThirdPersonFemininePlural
      extends ProNoun(
        word = ArabicWord(ArabicLetter(Ha, Damma), ArabicLetter(Noon, Shadda, Fatha)),
        number = NumberType.Plural,
        gender = GenderType.Feminine,
        conversation = ConversationType.ThirdPerson
      )

  case SecondPersonMasculineSingular
      extends ProNoun(
        word = ArabicWord(ArabicLetter(AlifHamzaAbove, Fatha), ArabicLetter(Noon, Sukun), ArabicLetter(Ta, Fatha)),
        number = NumberType.Singular,
        gender = GenderType.Masculine,
        conversation = ConversationType.SecondPerson
      )

  case SecondPersonMasculineDual
      extends ProNoun(
        word = ArabicWord(
          ArabicLetter(AlifHamzaAbove, Fatha),
          ArabicLetter(Noon, Sukun),
          ArabicLetter(Ta, Damma),
          ArabicLetter(Meem, Fatha),
          ArabicLetter(Alif)
        ),
        number = NumberType.Dual,
        gender = GenderType.Masculine,
        conversation = ConversationType.SecondPerson
      )

  case SecondPersonMasculinePlural
      extends ProNoun(
        word = ArabicWord(
          ArabicLetter(AlifHamzaAbove, Fatha),
          ArabicLetter(Noon, Sukun),
          ArabicLetter(Ta, Damma),
          ArabicLetter(Meem, Sukun)
        ),
        number = NumberType.Plural,
        gender = GenderType.Masculine,
        conversation = ConversationType.SecondPerson
      )

  case SecondPersonFeminineSingular
      extends ProNoun(
        word = ArabicWord(ArabicLetter(AlifHamzaAbove, Fatha), ArabicLetter(Noon, Sukun), ArabicLetter(Ta, Kasra)),
        number = NumberType.Singular,
        gender = GenderType.Feminine,
        conversation = ConversationType.SecondPerson
      )

  case SecondPersonFeminineDual
      extends ProNoun(
        word = ArabicWord(
          ArabicLetter(AlifHamzaAbove, Fatha),
          ArabicLetter(Noon, Sukun),
          ArabicLetter(Ta, Damma),
          ArabicLetter(Meem, Fatha),
          ArabicLetter(Alif)
        ),
        number = NumberType.Dual,
        gender = GenderType.Feminine,
        conversation = ConversationType.SecondPerson
      )

  case SecondPersonFemininePlural
      extends ProNoun(
        word = ArabicWord(
          ArabicLetter(AlifHamzaAbove, Fatha),
          ArabicLetter(Noon, Sukun),
          ArabicLetter(Ta, Damma),
          ArabicLetter(Noon, Shadda, Fatha)
        ),
        number = NumberType.Plural,
        gender = GenderType.Feminine,
        conversation = ConversationType.SecondPerson
      )

  case FirstPersonSingular
      extends ProNoun(
        word = ArabicWord(ArabicLetter(AlifHamzaAbove, Fatha), ArabicLetter(Noon, Fatha), ArabicLetter(Alif)),
        number = NumberType.Singular,
        gender = GenderType.Masculine,
        conversation = ConversationType.FirstPerson
      )

  case FirstPersonPlural
      extends ProNoun(
        word = ArabicWord(ArabicLetter(Noon, Fatha), ArabicLetter(Hha, Sukun), ArabicLetter(Noon, Damma)),
        number = NumberType.Plural,
        gender = GenderType.Masculine,
        conversation = ConversationType.FirstPerson
      )
}

object ProNoun {

  private lazy val valueFromPropertiesMap = Map(
    (NumberType.Singular, GenderType.Masculine, ConversationType.ThirdPerson) -> ThirdPersonMasculineSingular,
    (NumberType.Dual, GenderType.Masculine, ConversationType.ThirdPerson) -> ThirdPersonMasculineDual,
    (NumberType.Plural, GenderType.Masculine, ConversationType.ThirdPerson) -> ThirdPersonMasculinePlural,
    (NumberType.Singular, GenderType.Feminine, ConversationType.ThirdPerson) -> ThirdPersonFeminineSingular,
    (NumberType.Dual, GenderType.Feminine, ConversationType.ThirdPerson) -> ThirdPersonFeminineDual,
    (NumberType.Plural, GenderType.Feminine, ConversationType.ThirdPerson) -> ThirdPersonFemininePlural,
    (NumberType.Singular, GenderType.Masculine, ConversationType.SecondPerson) -> SecondPersonMasculineSingular,
    (NumberType.Dual, GenderType.Masculine, ConversationType.SecondPerson) -> SecondPersonMasculineDual,
    (NumberType.Plural, GenderType.Masculine, ConversationType.SecondPerson) -> SecondPersonMasculinePlural,
    (NumberType.Singular, GenderType.Feminine, ConversationType.SecondPerson) -> SecondPersonFeminineSingular,
    (NumberType.Dual, GenderType.Feminine, ConversationType.SecondPerson) -> SecondPersonFeminineDual,
    (NumberType.Plural, GenderType.Feminine, ConversationType.SecondPerson) -> SecondPersonFemininePlural,
    (NumberType.Singular, GenderType.Masculine, ConversationType.FirstPerson) -> FirstPersonSingular,
    (NumberType.Dual, GenderType.Masculine, ConversationType.FirstPerson) -> FirstPersonPlural,
    (NumberType.Plural, GenderType.Masculine, ConversationType.FirstPerson) -> FirstPersonPlural,
    (NumberType.Singular, GenderType.Feminine, ConversationType.FirstPerson) -> FirstPersonSingular,
    (NumberType.Dual, GenderType.Feminine, ConversationType.FirstPerson) -> FirstPersonPlural,
    (NumberType.Plural, GenderType.Feminine, ConversationType.FirstPerson) -> FirstPersonPlural
  )

  def fromProperties(
    numberType: NumberType,
    gender: GenderType,
    conversationType: ConversationType
  ): ProNoun = valueFromPropertiesMap((numberType, gender, conversationType))
}
