package com.alphasystem
package arabic
package morphologicalengine
package conjugation
package transformer
package verb

import arabic.model.{ ArabicLetters, ArabicLetterType, ArabicWord, DiacriticType }
import conjugation.model.internal.{ RootWord, VerbGroupType }
import morphologicalanalysis.morphology.model.{ ConversationType, GenderType }

class PresentTenseTransformer private[verb] (
  genderType: GenderType,
  conversationType: ConversationType)
    extends AbstractVerbTransformer(genderType, conversationType) {

  override protected def deriveSingularWord(rootWord: RootWord): ArabicWord = {
    val word = rootWord.derivedWord
    val thirdRadicalIndex = rootWord.thirdRadicalIndex

    (genderType, conversationType) match
      case (GenderType.Masculine, ConversationType.ThirdPerson) => word
      case (GenderType.Feminine, ConversationType.ThirdPerson) |
          (GenderType.Masculine, ConversationType.SecondPerson) =>
        word.replaceLetter(0, ArabicLetterType.Ta)
      case (GenderType.Feminine, ConversationType.SecondPerson) =>
        word
          .replaceDiacriticsAndAppend(
            thirdRadicalIndex,
            Seq(DiacriticType.Kasra),
            ArabicLetters.YaWithSukun,
            ArabicLetters.NoonWithFatha
          )
          .replaceLetter(0, ArabicLetterType.Ta)
      case (_, ConversationType.FirstPerson) =>
        word.replaceLetter(0, ArabicLetterType.Hamza)
  }

  override protected def deriveDualWord(rootWord: RootWord): Option[ArabicWord] = {
    val thirdRadicalIndex = rootWord.thirdRadicalIndex
    val thirdPersonMasculine = rootWord
      .derivedWord
      .replaceDiacriticsAndAppend(
        thirdRadicalIndex,
        Seq(DiacriticType.Fatha),
        ArabicLetters.LetterAlif,
        ArabicLetters.NoonWithKasra
      )
    (genderType, conversationType) match
      case (GenderType.Masculine, ConversationType.ThirdPerson) => Some(thirdPersonMasculine)
      case (GenderType.Feminine, ConversationType.ThirdPerson) |
          (GenderType.Masculine, ConversationType.SecondPerson) =>
        Some(thirdPersonMasculine.replaceLetter(0, ArabicLetterType.Ta))
      case (GenderType.Feminine, ConversationType.SecondPerson) =>
        Some(
          rootWord
            .derivedWord
            .removeLastLetter()
            .removeLastLetter()
            .replaceDiacriticsAndAppend(
              thirdRadicalIndex,
              Seq(DiacriticType.Fatha),
              ArabicLetters.LetterAlif,
              ArabicLetters.NoonWithKasra
            )
            .replaceLetter(0, ArabicLetterType.Ta)
        )
      case (_, ConversationType.FirstPerson) => None
  }

  override protected def derivePluralWord(rootWord: RootWord): ArabicWord = {
    val word = rootWord.derivedWord
    val thirdRadicalIndex = rootWord.thirdRadicalIndex

    val thirdPersonMasculine = word.replaceDiacriticsAndAppend(
      thirdRadicalIndex,
      Seq(DiacriticType.Damma),
      ArabicLetters.WawWithSukun,
      ArabicLetters.NoonWithFatha
    )

    val thirdPersonFeminine =
      word
        .replaceDiacriticsAndAppend(thirdRadicalIndex, Seq(DiacriticType.Sukun), ArabicLetters.NoonWithFatha)
        .replaceLetter(0, ArabicLetterType.Ya)

    (genderType, conversationType) match
      case (GenderType.Masculine, ConversationType.ThirdPerson) => thirdPersonMasculine
      case (GenderType.Feminine, ConversationType.ThirdPerson)  => thirdPersonFeminine
      case (GenderType.Masculine, ConversationType.SecondPerson) =>
        thirdPersonMasculine.replaceLetter(0, ArabicLetterType.Ta)
      case (GenderType.Feminine, ConversationType.SecondPerson) =>
        word
          .removeLastLetter()
          .removeLastLetter()
          .replaceDiacriticsAndAppend(thirdRadicalIndex, Seq(DiacriticType.Sukun), ArabicLetters.NoonWithFatha)
          .replaceLetter(0, ArabicLetterType.Ta)
      case (_, ConversationType.FirstPerson) => word.replaceLetter(0, ArabicLetterType.Noon)
  }
}

object PresentTenseTransformer {
  private def apply(
    genderType: GenderType,
    conversationType: ConversationType
  ): Transformer = new PresentTenseTransformer(genderType, conversationType)

  def apply(verbGroupType: VerbGroupType): Transformer =
    PresentTenseTransformer(verbGroupType.gender, verbGroupType.conversation)
}
