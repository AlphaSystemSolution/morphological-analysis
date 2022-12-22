package com.alphasystem
package arabic
package morphologicalanalysis
package morphology
package model
package incomplete_verb

import arabic.model.{ ArabicLetters, ArabicWord }

import java.lang.Enum

enum KanaPresentTense(override val word: ArabicWord) extends Enum[KanaPresentTense] with IncompleteVerbType {

  override val code: String = name()

  case ThirdPersonMasculineSingular
      extends KanaPresentTense(
        ArabicWord(
          ArabicLetters.YaWithFatha,
          ArabicLetters.KafWithDamma,
          ArabicLetters.WawWithSukun,
          ArabicLetters.NoonWithDamma
        )
      )

  case ThirdPersonMasculineDual
      extends KanaPresentTense(
        ArabicWord(
          ArabicLetters.YaWithFatha,
          ArabicLetters.KafWithDamma,
          ArabicLetters.WawWithSukun,
          ArabicLetters.LetterNoon,
          ArabicLetters.LetterAlif,
          ArabicLetters.NoonWithKasra
        )
      )

  case ThirdPersonMasculinePlural
      extends KanaPresentTense(
        ArabicWord(
          ArabicLetters.YaWithFatha,
          ArabicLetters.KafWithDamma,
          ArabicLetters.WawWithSukun,
          ArabicLetters.NoonWithDamma,
          ArabicLetters.WawWithSukun,
          ArabicLetters.NoonWithFatha
        )
      )

  case ThirdPersonFeminineSingular
      extends KanaPresentTense(
        ArabicWord(
          ArabicLetters.TaWithFatha,
          ArabicLetters.KafWithDamma,
          ArabicLetters.WawWithSukun,
          ArabicLetters.NoonWithDamma
        )
      )

  case ThirdPersonFeminineDual
      extends KanaPresentTense(
        ArabicWord(
          ArabicLetters.TaWithFatha,
          ArabicLetters.KafWithDamma,
          ArabicLetters.WawWithSukun,
          ArabicLetters.LetterNoon,
          ArabicLetters.LetterAlif,
          ArabicLetters.NoonWithKasra
        )
      )

  case ThirdPersonFemininePlural
      extends KanaPresentTense(
        ArabicWord(
          ArabicLetters.YaWithFatha,
          ArabicLetters.KafWithDamma,
          ArabicLetters.NoonWithShaddaAndFatha
        )
      )

  case SecondPersonMasculineSingular
      extends KanaPresentTense(
        ArabicWord(
          ArabicLetters.TaWithFatha,
          ArabicLetters.KafWithDamma,
          ArabicLetters.WawWithSukun,
          ArabicLetters.NoonWithDamma
        )
      )

  case SecondPersonMasculineDual
      extends KanaPresentTense(
        ArabicWord(
          ArabicLetters.TaWithFatha,
          ArabicLetters.KafWithDamma,
          ArabicLetters.WawWithSukun,
          ArabicLetters.LetterNoon,
          ArabicLetters.LetterAlif,
          ArabicLetters.NoonWithKasra
        )
      )

  case SecondPersonMasculinePlural
      extends KanaPresentTense(
        ArabicWord(
          ArabicLetters.TaWithFatha,
          ArabicLetters.KafWithDamma,
          ArabicLetters.WawWithSukun,
          ArabicLetters.NoonWithDamma,
          ArabicLetters.WawWithSukun,
          ArabicLetters.NoonWithFatha
        )
      )

  case SecondPersonFeminineSingular
      extends KanaPresentTense(
        ArabicWord(
          ArabicLetters.TaWithFatha,
          ArabicLetters.KafWithDamma,
          ArabicLetters.WawWithSukun,
          ArabicLetters.NoonWithFatha,
          ArabicLetters.YaWithSukun,
          ArabicLetters.NoonWithFatha
        )
      )

  case SecondPersonFeminineDual
      extends KanaPresentTense(
        ArabicWord(
          ArabicLetters.TaWithFatha,
          ArabicLetters.KafWithDamma,
          ArabicLetters.WawWithSukun,
          ArabicLetters.LetterNoon,
          ArabicLetters.LetterAlif,
          ArabicLetters.NoonWithKasra
        )
      )

  case SecondPersonFemininePlural
      extends KanaPresentTense(
        ArabicWord(
          ArabicLetters.TaWithFatha,
          ArabicLetters.KafWithDamma,
          ArabicLetters.NoonWithShaddaAndFatha
        )
      )

  case FirstPersonSingular
      extends KanaPresentTense(
        ArabicWord(
          ArabicLetters.AlifHamzaAboveWithFatha,
          ArabicLetters.KafWithDamma,
          ArabicLetters.WawWithSukun,
          ArabicLetters.NoonWithDamma
        )
      )

  case FirstPersonPlural
      extends KanaPresentTense(
        ArabicWord(
          ArabicLetters.NoonWithFatha,
          ArabicLetters.KafWithDamma,
          ArabicLetters.WawWithSukun,
          ArabicLetters.NoonWithDamma
        )
      )
}

object KanaPresentTense {

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
  ): KanaPresentTense = valueFromPropertiesMap((numberType, gender, conversationType))
}
