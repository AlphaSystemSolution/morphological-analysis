package com.alphasystem
package arabic
package morphologicalengine
package conjugation
package transformer
package verb

import arabic.model.{ ArabicLetters, ArabicWord, DiacriticType, HiddenPronounStatus, SarfMemberType }
import conjugation.model.internal.{ RootWord, VerbGroupType }
import morphologicalanalysis.morphology.model.{ ConversationType, GenderType }

class PastTenseTransformer private (
  genderType: GenderType,
  conversationType: ConversationType)
    extends AbstractVerbTransformer(genderType, conversationType) {

  override protected def deriveSingularWord(rootWord: RootWord): (SarfMemberType, ArabicWord) = {
    val word = rootWord.derivedWord
    val thirdRadicalIndex = rootWord.thirdRadicalIndex

    (genderType, conversationType) match
      case (GenderType.Masculine, ConversationType.ThirdPerson) =>
        (HiddenPronounStatus.ThirdPersonMasculineSingular, word)

      case (GenderType.Feminine, ConversationType.ThirdPerson) =>
        (HiddenPronounStatus.ThirdPersonFeminineSingular, word.appendLetters(ArabicLetters.TaWithSukun))

      case (GenderType.Masculine, ConversationType.SecondPerson) =>
        (
          HiddenPronounStatus.SecondPersonMasculineSingular,
          word.replaceDiacriticsAndAppend(thirdRadicalIndex, Seq(DiacriticType.Sukun), ArabicLetters.TaWithFatha)
        )

      case (GenderType.Feminine, ConversationType.SecondPerson) =>
        (
          HiddenPronounStatus.SecondPersonFeminineSingular,
          word.replaceDiacriticsAndAppend(thirdRadicalIndex, Seq(DiacriticType.Sukun), ArabicLetters.TaWithKasra)
        )

      case (_, ConversationType.FirstPerson) =>
        (
          HiddenPronounStatus.FirstPersonSingular,
          word.replaceDiacriticsAndAppend(thirdRadicalIndex, Seq(DiacriticType.Sukun), ArabicLetters.TaWithDamma)
        )
  }

  override protected def deriveDualWord(rootWord: RootWord): Option[(SarfMemberType, ArabicWord)] = {
    val word = rootWord.derivedWord
    val lastLetterIndex = rootWord.lastLetterIndex

    (genderType, conversationType) match
      case (GenderType.Masculine, ConversationType.ThirdPerson) =>
        Some((HiddenPronounStatus.ThirdPersonMasculineDual, word.appendLetters(ArabicLetters.LetterAlif)))

      case (GenderType.Feminine, ConversationType.ThirdPerson) =>
        Some(
          (
            HiddenPronounStatus.ThirdPersonFeminineDual,
            word.replaceDiacriticsAndAppend(lastLetterIndex, Seq(DiacriticType.Fatha), ArabicLetters.LetterAlif)
          )
        )

      case (GenderType.Feminine, ConversationType.SecondPerson) =>
        Some(
          HiddenPronounStatus.SecondPersonFeminineDual,
          word.replaceDiacriticsAndAppend(
            lastLetterIndex,
            Seq(DiacriticType.Damma),
            ArabicLetters.MeemWithFatha,
            ArabicLetters.LetterAlif
          )
        )

      case (GenderType.Masculine, ConversationType.SecondPerson) =>
        Some(
          (
            HiddenPronounStatus.SecondPersonMasculineDual,
            word.replaceDiacriticsAndAppend(
              lastLetterIndex,
              Seq(DiacriticType.Damma),
              ArabicLetters.MeemWithFatha,
              ArabicLetters.LetterAlif
            )
          )
        )

      case (_, ConversationType.FirstPerson) => None
  }

  override protected def derivePluralWord(rootWord: RootWord): (SarfMemberType, ArabicWord) = {
    val word = rootWord.derivedWord
    val thirdRadicalIndex = rootWord.thirdRadicalIndex
    val lastLetterIndex = rootWord.lastLetterIndex

    (genderType, conversationType) match
      case (GenderType.Masculine, ConversationType.ThirdPerson) =>
        (
          HiddenPronounStatus.ThirdPersonMasculinePlural,
          word.replaceDiacriticsAndAppend(
            thirdRadicalIndex,
            Seq(DiacriticType.Damma),
            ArabicLetters.WawWithSukun,
            ArabicLetters.LetterAlif
          )
        )

      case (GenderType.Feminine, ConversationType.ThirdPerson) =>
        (
          HiddenPronounStatus.ThirdPersonFemininePlural,
          word
            .removeLastLetter()
            .replaceDiacriticsAndAppend(thirdRadicalIndex, Seq(DiacriticType.Sukun), ArabicLetters.NoonWithFatha)
        )

      case (GenderType.Masculine, ConversationType.SecondPerson) =>
        (
          HiddenPronounStatus.SecondPersonMasculinePlural,
          word.replaceDiacriticsAndAppend(
            lastLetterIndex,
            Seq(DiacriticType.Damma),
            ArabicLetters.MeemWithSukun
          )
        )

      case (GenderType.Feminine, ConversationType.SecondPerson) =>
        (
          HiddenPronounStatus.SecondPersonFemininePlural,
          word.replaceDiacriticsAndAppend(
            lastLetterIndex,
            Seq(DiacriticType.Damma),
            ArabicLetters.NoonWithShaddaAndFatha
          )
        )

      case (_, ConversationType.FirstPerson) =>
        (
          HiddenPronounStatus.FirstPersonPlural,
          word
            .removeLastLetter()
            .replaceDiacriticsAndAppend(
              thirdRadicalIndex,
              Seq(DiacriticType.Sukun),
              ArabicLetters.NoonWithFatha,
              ArabicLetters.LetterAlif
            )
        )
  }
}

object PastTenseTransformer {
  private def apply(
    genderType: GenderType,
    conversationType: ConversationType
  ): Transformer = new PastTenseTransformer(genderType, conversationType)

  def apply(verbGroupType: VerbGroupType): Transformer =
    PastTenseTransformer(verbGroupType.gender, verbGroupType.conversation)
}
