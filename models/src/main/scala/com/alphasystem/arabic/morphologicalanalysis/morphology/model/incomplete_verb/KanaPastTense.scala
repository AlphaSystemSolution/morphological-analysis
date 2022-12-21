package com.alphasystem
package arabic
package morphologicalanalysis
package morphology
package model
package incomplete_verb

import arabic.model.{ ArabicLetters, ArabicWord }

import java.lang.Enum

enum KanaPastTense(override val word: ArabicWord) extends Enum[KanaPastTense] with IncompleteVerbType {

  override val code: String = name()

  case ThirdPersonMasculineSingular
      extends KanaPastTense(ArabicWord(ArabicLetters.LetterKaf, ArabicLetters.LetterAlif, ArabicLetters.NoonWithFatha))

  case ThirdPersonMasculineDual
      extends KanaPastTense(
        ArabicWord(
          ArabicLetters.LetterKaf,
          ArabicLetters.LetterAlif,
          ArabicLetters.LetterNoon,
          ArabicLetters.LetterAlif
        )
      )

  case ThirdPersonMasculinePlural
      extends KanaPastTense(
        ArabicWord(
          ArabicLetters.LetterKaf,
          ArabicLetters.LetterAlif,
          ArabicLetters.NoonWithDamma,
          ArabicLetters.WawWithSukun,
          ArabicLetters.LetterAlif
        )
      )

  case ThirdPersonFeminineSingular
      extends KanaPastTense(
        ArabicWord(
          ArabicLetters.LetterKaf,
          ArabicLetters.LetterAlif,
          ArabicLetters.NoonWithFatha,
          ArabicLetters.TaWithSukun
        )
      )

  case ThirdPersonFeminineDual
      extends KanaPastTense(
        ArabicWord(
          ArabicLetters.LetterKaf,
          ArabicLetters.LetterAlif,
          ArabicLetters.NoonWithFatha,
          ArabicLetters.LetterTa,
          ArabicLetters.LetterAlif
        )
      )

  case ThirdPersonFemininePlural
      extends KanaPastTense(
        ArabicWord(
          ArabicLetters.KafWithDamma,
          ArabicLetters.NoonWithShaddaAndFatha
        )
      )

  case SecondPersonMasculineSingular
      extends KanaPastTense(
        ArabicWord(
          ArabicLetters.KafWithDamma,
          ArabicLetters.NoonWithSukun,
          ArabicLetters.TaWithFatha
        )
      )

  case SecondPersonMasculineDual
      extends KanaPastTense(
        ArabicWord(
          ArabicLetters.KafWithDamma,
          ArabicLetters.NoonWithSukun,
          ArabicLetters.TaWithDamma,
          ArabicLetters.LetterMeem,
          ArabicLetters.LetterAlif
        )
      )

  case SecondPersonMasculinePlural
      extends KanaPastTense(
        ArabicWord(
          ArabicLetters.KafWithDamma,
          ArabicLetters.NoonWithSukun,
          ArabicLetters.TaWithDamma,
          ArabicLetters.MeemWithSukun
        )
      )

  case SecondPersonFeminineSingular
      extends KanaPastTense(
        ArabicWord(ArabicLetters.KafWithDamma, ArabicLetters.NoonWithSukun, ArabicLetters.TaWithKasra)
      )

  case SecondPersonFeminineDual
      extends KanaPastTense(
        ArabicWord(
          ArabicLetters.KafWithDamma,
          ArabicLetters.NoonWithSukun,
          ArabicLetters.TaWithDamma,
          ArabicLetters.LetterMeem,
          ArabicLetters.LetterAlif
        )
      )

  case SecondPersonFemininePlural
      extends KanaPastTense(
        ArabicWord(
          ArabicLetters.KafWithDamma,
          ArabicLetters.NoonWithSukun,
          ArabicLetters.TaWithDamma,
          ArabicLetters.NoonWithShaddaAndFatha
        )
      )

  case FirstPersonSingular
      extends KanaPastTense(
        ArabicWord(ArabicLetters.KafWithDamma, ArabicLetters.NoonWithSukun, ArabicLetters.TaWithDamma)
      )

  case FirstPersonPlural
      extends KanaPastTense(
        ArabicWord(ArabicLetters.KafWithDamma, ArabicLetters.NoonWithShaddaAndDamma, ArabicLetters.LetterAlif)
      )
}

object KanaPastTense {

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
  ): KanaPastTense = valueFromPropertiesMap((numberType, gender, conversationType))
}
