package com.alphasystem
package arabic
package morphologicalanalysis
package morphology
package model
package incomplete_verb

import arabic.model.{ ArabicLetters, ArabicWord }

import java.lang.Enum

enum PresentTenseKana(override val word: ArabicWord) extends Enum[PresentTenseKana] with IncompleteVerbType {

  override val code: String = name()

  case ThirdPersonMasculineSingular
      extends PresentTenseKana(
        ArabicWord(
          ArabicLetters.LetterYa,
          ArabicLetters.LetterKaf,
          ArabicLetters.NoonWithDamma
        )
      )

  case ThirdPersonMasculineDual
      extends PresentTenseKana(
        ArabicWord(
          ArabicLetters.LetterYa,
          ArabicLetters.LetterKaf,
          ArabicLetters.LetterNoon,
          ArabicLetters.LetterAlif,
          ArabicLetters.LetterNoon
        )
      )

  case ThirdPersonMasculinePlural
      extends PresentTenseKana(
        ArabicWord(
          ArabicLetters.LetterYa,
          ArabicLetters.LetterKaf,
          ArabicLetters.LetterNoon,
          ArabicLetters.LetterWaw,
          ArabicLetters.LetterNoon
        )
      )

  case ThirdPersonFeminineSingular
      extends PresentTenseKana(
        ArabicWord(
          ArabicLetters.LetterTa,
          ArabicLetters.LetterKaf,
          ArabicLetters.NoonWithDamma
        )
      )

  case ThirdPersonFeminineDual
      extends PresentTenseKana(
        ArabicWord(
          ArabicLetters.LetterTa,
          ArabicLetters.LetterKaf,
          ArabicLetters.LetterNoon,
          ArabicLetters.LetterAlif,
          ArabicLetters.LetterNoon
        )
      )

  case ThirdPersonFemininePlural
      extends PresentTenseKana(
        ArabicWord(
          ArabicLetters.LetterYa,
          ArabicLetters.LetterKaf,
          ArabicLetters.NoonWithFatha
        )
      )

  case SecondPersonMasculineSingular
      extends PresentTenseKana(
        ArabicWord(
          ArabicLetters.LetterTa,
          ArabicLetters.LetterKaf,
          ArabicLetters.NoonWithDamma
        )
      )

  case SecondPersonMasculineDual
      extends PresentTenseKana(
        ArabicWord(
          ArabicLetters.LetterTa,
          ArabicLetters.LetterKaf,
          ArabicLetters.LetterNoon,
          ArabicLetters.LetterAlif,
          ArabicLetters.LetterNoon
        )
      )

  case SecondPersonMasculinePlural
      extends PresentTenseKana(
        ArabicWord(
          ArabicLetters.LetterTa,
          ArabicLetters.LetterKaf,
          ArabicLetters.LetterNoon,
          ArabicLetters.LetterWaw,
          ArabicLetters.LetterNoon
        )
      )

  case SecondPersonFeminineSingular
      extends PresentTenseKana(
        ArabicWord(
          ArabicLetters.LetterTa,
          ArabicLetters.LetterKaf,
          ArabicLetters.LetterNoon,
          ArabicLetters.LetterYa,
          ArabicLetters.LetterNoon
        )
      )

  case SecondPersonFeminineDual
      extends PresentTenseKana(
        ArabicWord(
          ArabicLetters.LetterTa,
          ArabicLetters.LetterKaf,
          ArabicLetters.LetterNoon,
          ArabicLetters.LetterAlif,
          ArabicLetters.LetterNoon
        )
      )

  case SecondPersonFemininePlural
      extends PresentTenseKana(
        ArabicWord(
          ArabicLetters.LetterTa,
          ArabicLetters.LetterKaf,
          ArabicLetters.NoonWithFatha
        )
      )

  case FirstPersonSingular
      extends PresentTenseKana(ArabicWord(ArabicLetters.LetterTa, ArabicLetters.LetterKaf, ArabicLetters.NoonWithDamma))

  case FirstPersonPlural
      extends PresentTenseKana(
        ArabicWord(
          ArabicLetters.LetterNoon,
          ArabicLetters.LetterKaf,
          ArabicLetters.LetterNoon,
          ArabicLetters.LetterWaw,
          ArabicLetters.LetterNoon
        )
      )
}

object PresentTenseKana {

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
  ): PresentTenseKana = valueFromPropertiesMap((numberType, gender, conversationType))
}
