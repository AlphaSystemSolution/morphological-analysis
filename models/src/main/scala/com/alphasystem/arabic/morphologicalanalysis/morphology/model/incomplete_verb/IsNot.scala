package com.alphasystem
package arabic
package morphologicalanalysis
package morphology
package model
package incomplete_verb

import arabic.model.{ ArabicLetters, ArabicWord }

import java.lang.Enum
enum IsNot(override val word: ArabicWord) extends Enum[IsNot] with IncompleteVerbType {

  override val code: String = name()

  case ThirdPersonMasculineSingular
      extends IsNot(ArabicWord(ArabicLetters.LamWithFatha, ArabicLetters.YaWithSukun, ArabicLetters.SeenWithFatha))

  case ThirdPersonMasculineDual
      extends IsNot(
        ArabicWord(
          ArabicLetters.LamWithFatha,
          ArabicLetters.YaWithSukun,
          ArabicLetters.LetterSeen,
          ArabicLetters.LetterAlif
        )
      )

  case ThirdPersonMasculinePlural
      extends IsNot(
        ArabicWord(
          ArabicLetters.LamWithFatha,
          ArabicLetters.YaWithSukun,
          ArabicLetters.SeenWithDamma,
          ArabicLetters.WawWithSukun,
          ArabicLetters.LetterAlif
        )
      )

  case ThirdPersonFeminineSingular
      extends IsNot(
        ArabicWord(
          ArabicLetters.LamWithFatha,
          ArabicLetters.YaWithSukun,
          ArabicLetters.SeenWithFatha,
          ArabicLetters.TaWithSukun
        )
      )

  case ThirdPersonFeminineDual
      extends IsNot(
        ArabicWord(
          ArabicLetters.LamWithFatha,
          ArabicLetters.YaWithSukun,
          ArabicLetters.SeenWithFatha,
          ArabicLetters.LetterTa,
          ArabicLetters.LetterAlif
        )
      )

  case ThirdPersonFemininePlural
      extends IsNot(ArabicWord(ArabicLetters.LamWithFatha, ArabicLetters.SeenWithSukun, ArabicLetters.NoonWithFatha))

  case SecondPersonMasculineSingular
      extends IsNot(ArabicWord(ArabicLetters.LamWithFatha, ArabicLetters.SeenWithSukun, ArabicLetters.TaWithFatha))

  case SecondPersonMasculineDual
      extends IsNot(
        ArabicWord(
          ArabicLetters.LamWithFatha,
          ArabicLetters.SeenWithSukun,
          ArabicLetters.TaWithDamma,
          ArabicLetters.LetterMeem,
          ArabicLetters.LetterAlif
        )
      )

  case SecondPersonMasculinePlural
      extends IsNot(
        ArabicWord(
          ArabicLetters.LamWithFatha,
          ArabicLetters.SeenWithSukun,
          ArabicLetters.TaWithDamma,
          ArabicLetters.MeemWithSukun
        )
      )

  case SecondPersonFeminineSingular
      extends IsNot(ArabicWord(ArabicLetters.LamWithFatha, ArabicLetters.SeenWithSukun, ArabicLetters.TaWithKasra))

  case SecondPersonFeminineDual
      extends IsNot(
        ArabicWord(
          ArabicLetters.LamWithFatha,
          ArabicLetters.SeenWithSukun,
          ArabicLetters.TaWithDamma,
          ArabicLetters.LetterMeem,
          ArabicLetters.LetterAlif
        )
      )

  case SecondPersonFemininePlural
      extends IsNot(
        ArabicWord(
          ArabicLetters.LamWithFatha,
          ArabicLetters.SeenWithSukun,
          ArabicLetters.TaWithDamma,
          ArabicLetters.NoonWithShaddaAndFatha
        )
      )

  case FirstPersonSingular
      extends IsNot(ArabicWord(ArabicLetters.LamWithFatha, ArabicLetters.SeenWithSukun, ArabicLetters.TaWithDamma))

  case FirstPersonPlural
      extends IsNot(
        ArabicWord(
          ArabicLetters.LamWithFatha,
          ArabicLetters.SeenWithSukun,
          ArabicLetters.LetterNoon,
          ArabicLetters.LetterAlif
        )
      )
}

object IsNot {

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
  ): IsNot = valueFromPropertiesMap((numberType, gender, conversationType))
}
