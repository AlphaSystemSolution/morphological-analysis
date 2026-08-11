package com.alphasystem
package arabic
package morphologicalengine
package conjugation
package transformer
package verb

import arabic.model.{ ArabicLetterType, ArabicLetters, ArabicWord, DiacriticType, HiddenPronounStatus, SarfMemberType }
import conjugation.model.internal.{ RootWord, VerbGroupType }
import morphologicalanalysis.morphology.model.{ ConversationType, GenderType }

class PresentTenseJussiveModeTransformer private[verb] (
  genderType: GenderType,
  conversationType: ConversationType)
    extends AbstractVerbTransformer(genderType, conversationType) {

  override protected def deriveSingularWord(rootWord: RootWord): (SarfMemberType, ArabicWord) = {
    val word = rootWord.derivedWord
    val thirdRadicalIndex = rootWord.thirdRadicalIndex

    (genderType, conversationType) match
      case (GenderType.Masculine, ConversationType.ThirdPerson) =>
        (HiddenPronounStatus.ThirdPersonMasculineSingular, word)

      case (GenderType.Masculine, ConversationType.SecondPerson) =>
        (HiddenPronounStatus.SecondPersonMasculineSingular, word.replaceLetter(0, ArabicLetterType.Ta))

      case (GenderType.Feminine, ConversationType.ThirdPerson) =>
        (HiddenPronounStatus.ThirdPersonFeminineSingular, word.replaceLetter(0, ArabicLetterType.Ta))

      case (GenderType.Feminine, ConversationType.SecondPerson) =>
        (
          HiddenPronounStatus.SecondPersonFeminineSingular,
          word
            .replaceDiacriticsAndAppend(
              thirdRadicalIndex,
              Seq(DiacriticType.Kasra),
              ArabicLetters.YaWithSukun
            )
            .replaceLetter(0, ArabicLetterType.Ta)
        )

      case (_, ConversationType.FirstPerson) =>
        (HiddenPronounStatus.FirstPersonSingular, word.replaceLetter(0, ArabicLetterType.Hamza))
  }

  override protected def deriveDualWord(rootWord: RootWord): Option[(SarfMemberType, ArabicWord)] = {
    val thirdRadicalIndex = rootWord.thirdRadicalIndex
    val thirdPersonMasculine = rootWord
      .derivedWord
      .replaceDiacriticsAndAppend(
        thirdRadicalIndex,
        Seq(DiacriticType.Fatha),
        ArabicLetters.LetterAlif
      )

    (genderType, conversationType) match
      case (GenderType.Masculine, ConversationType.ThirdPerson) =>
        Some(HiddenPronounStatus.ThirdPersonMasculineDual, thirdPersonMasculine)

      case (GenderType.Masculine, ConversationType.SecondPerson) =>
        Some(HiddenPronounStatus.SecondPersonMasculineDual, thirdPersonMasculine.replaceLetter(0, ArabicLetterType.Ta))

      case (GenderType.Feminine, ConversationType.ThirdPerson) =>
        Some(HiddenPronounStatus.ThirdPersonFeminineDual, thirdPersonMasculine.replaceLetter(0, ArabicLetterType.Ta))

      case (GenderType.Feminine, ConversationType.SecondPerson) =>
        Some(
          HiddenPronounStatus.SecondPersonFeminineDual,
          rootWord
            .derivedWord
            .removeLastLetter()
            .replaceDiacriticsAndAppend(
              thirdRadicalIndex,
              Seq(DiacriticType.Fatha),
              ArabicLetters.LetterAlif
            )
            .replaceLetter(0, ArabicLetterType.Ta)
        )
      case (_, ConversationType.FirstPerson) => None
  }

  override protected def derivePluralWord(rootWord: RootWord): (SarfMemberType, ArabicWord) = {
    val word = rootWord.derivedWord
    val thirdRadicalIndex = rootWord.thirdRadicalIndex

    val thirdPersonMasculine = word.replaceDiacriticsAndAppend(
      thirdRadicalIndex,
      Seq(DiacriticType.Damma),
      ArabicLetters.WawWithSukun,
      ArabicLetters.LetterAlif
    )

    val thirdPersonFeminine =
      word
        .replaceDiacriticsAndAppend(thirdRadicalIndex, Seq(DiacriticType.Sukun), ArabicLetters.NoonWithFatha)
        .replaceLetter(0, ArabicLetterType.Ya)

    (genderType, conversationType) match
      case (GenderType.Masculine, ConversationType.ThirdPerson) =>
        (HiddenPronounStatus.ThirdPersonMasculinePlural, thirdPersonMasculine)

      case (GenderType.Feminine, ConversationType.ThirdPerson) =>
        (HiddenPronounStatus.ThirdPersonFemininePlural, thirdPersonFeminine)

      case (GenderType.Masculine, ConversationType.SecondPerson) =>
        (HiddenPronounStatus.SecondPersonMasculinePlural, thirdPersonMasculine.replaceLetter(0, ArabicLetterType.Ta))

      case (GenderType.Feminine, ConversationType.SecondPerson) =>
        (
          HiddenPronounStatus.SecondPersonFemininePlural,
          word
            .removeLastLetter()
            .replaceDiacriticsAndAppend(thirdRadicalIndex, Seq(DiacriticType.Sukun), ArabicLetters.NoonWithFatha)
            .replaceLetter(0, ArabicLetterType.Ta)
        )

      case (_, ConversationType.FirstPerson) =>
        (HiddenPronounStatus.FirstPersonPlural, word.replaceLetter(0, ArabicLetterType.Noon))
  }
}

object PresentTenseJussiveModeTransformer {
  private def apply(
    genderType: GenderType,
    conversationType: ConversationType
  ): Transformer = new PresentTenseJussiveModeTransformer(genderType, conversationType)

  def apply(verbGroupType: VerbGroupType): Transformer =
    PresentTenseJussiveModeTransformer(verbGroupType.gender, verbGroupType.conversation)
}
