package com.alphasystem
package arabic
package morphologicalanalysis
package morphology
package model
package incomplete_verb

import arabic.model.{ ArabicLetters, ArabicWord }

import java.lang.Enum

enum KanaForbidden(override val word: ArabicWord) extends Enum[KanaForbidden] with IncompleteVerbType {

  override val code: String = name()

  case SecondPersonMasculineSingular
      extends KanaForbidden(ArabicWord(ArabicLetters.LetterTa, ArabicLetters.KafWithDamma, ArabicLetters.NoonWithSukun))

  case SecondPersonMasculineDual
      extends KanaForbidden(
        ArabicWord(
          ArabicLetters.LetterTa,
          ArabicLetters.KafWithDamma,
          ArabicLetters.WawWithSukun,
          ArabicLetters.NoonWithFatha,
          ArabicLetters.LetterAlif
        )
      )

  case SecondPersonMasculinePlural
      extends KanaForbidden(
        ArabicWord(
          ArabicLetters.LetterTa,
          ArabicLetters.KafWithDamma,
          ArabicLetters.WawWithSukun,
          ArabicLetters.NoonWithDamma,
          ArabicLetters.WawWithSukun,
          ArabicLetters.LetterAlif
        )
      )

  case SecondPersonFeminineSingular
      extends KanaForbidden(
        ArabicWord(
          ArabicLetters.LetterTa,
          ArabicLetters.KafWithDamma,
          ArabicLetters.WawWithSukun,
          ArabicLetters.NoonWithKasra,
          ArabicLetters.LetterYa
        )
      )

  case SecondPersonFeminineDual
      extends KanaForbidden(
        ArabicWord(
          ArabicLetters.LetterTa,
          ArabicLetters.KafWithDamma,
          ArabicLetters.WawWithSukun,
          ArabicLetters.NoonWithFatha,
          ArabicLetters.LetterAlif
        )
      )

  case SecondPersonFemininePlural
      extends KanaForbidden(
        ArabicWord(ArabicLetters.LetterTa, ArabicLetters.KafWithDamma, ArabicLetters.NoonWithShaddaAndFatha)
      )
}

object KanaForbidden {

  private lazy val valueFromPropertiesMap = Map(
    (NumberType.Singular, GenderType.Masculine, ConversationType.SecondPerson) -> SecondPersonMasculineSingular,
    (NumberType.Dual, GenderType.Masculine, ConversationType.SecondPerson) -> SecondPersonMasculineDual,
    (NumberType.Plural, GenderType.Masculine, ConversationType.SecondPerson) -> SecondPersonMasculinePlural,
    (NumberType.Singular, GenderType.Feminine, ConversationType.SecondPerson) -> SecondPersonFeminineSingular,
    (NumberType.Dual, GenderType.Feminine, ConversationType.SecondPerson) -> SecondPersonFeminineDual,
    (NumberType.Plural, GenderType.Feminine, ConversationType.SecondPerson) -> SecondPersonFemininePlural
  )

  def fromProperties(
    numberType: NumberType,
    gender: GenderType,
    conversationType: ConversationType
  ): Option[KanaForbidden] = valueFromPropertiesMap.get((numberType, gender, conversationType))
}
