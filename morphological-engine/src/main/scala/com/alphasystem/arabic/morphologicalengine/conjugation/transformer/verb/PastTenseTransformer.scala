package com.alphasystem
package arabic
package morphologicalengine
package conjugation
package transformer
package verb

import arabic.model.{ ArabicLetters, ArabicWord, DiacriticType }
import conjugation.model.internal.{ RootWord, VerbGroupType }
import morphologicalanalysis.morphology.model.{ ConversationType, GenderType }

class PastTenseTransformer private (
  genderType: GenderType,
  conversationType: ConversationType)
    extends AbstractVerbTransformer(genderType, conversationType) {

  override protected def deriveSingularWord(rootWord: RootWord): ArabicWord = {
    val word = rootWord.derivedWord
    val thirdRadicalIndex = rootWord.thirdRadicalIndex

    (genderType, conversationType) match
      case (GenderType.Masculine, ConversationType.ThirdPerson) => word
      case (GenderType.Feminine, ConversationType.ThirdPerson)  => word.appendLetters(ArabicLetters.TaWithSukun)
      case (GenderType.Masculine, ConversationType.SecondPerson) =>
        word.replaceDiacriticsAndAppend(thirdRadicalIndex, Seq(DiacriticType.Sukun), ArabicLetters.TaWithFatha)
      case (GenderType.Feminine, ConversationType.SecondPerson) =>
        word.replaceDiacriticsAndAppend(thirdRadicalIndex, Seq(DiacriticType.Sukun), ArabicLetters.TaWithKasra)
      case (_, ConversationType.FirstPerson) =>
        word.replaceDiacriticsAndAppend(thirdRadicalIndex, Seq(DiacriticType.Sukun), ArabicLetters.TaWithDamma)
  }

  override protected def deriveDualWord(rootWord: RootWord): Option[ArabicWord] = {
    val word = rootWord.derivedWord
    val lastLetterIndex = rootWord.lastLetterIndex

    (genderType, conversationType) match
      case (GenderType.Masculine, ConversationType.ThirdPerson) => Some(word.appendLetters(ArabicLetters.LetterAlif))
      case (GenderType.Feminine, ConversationType.ThirdPerson) =>
        Some(
          word.replaceDiacriticsAndAppend(lastLetterIndex, Seq(DiacriticType.Fatha), ArabicLetters.LetterAlif)
        )
      case (GenderType.Masculine, ConversationType.SecondPerson) |
          (GenderType.Feminine, ConversationType.SecondPerson) =>
        Some(
          word.replaceDiacriticsAndAppend(
            lastLetterIndex,
            Seq(DiacriticType.Damma),
            ArabicLetters.MeemWithFatha,
            ArabicLetters.LetterAlif
          )
        )
      case (_, ConversationType.FirstPerson) => None
  }

  override protected def derivePluralWord(rootWord: RootWord): ArabicWord = {
    val word = rootWord.derivedWord
    val thirdRadicalIndex = rootWord.thirdRadicalIndex
    val lastLetterIndex = rootWord.lastLetterIndex

    (genderType, conversationType) match
      case (GenderType.Masculine, ConversationType.ThirdPerson) =>
        word.replaceDiacriticsAndAppend(
          thirdRadicalIndex,
          Seq(DiacriticType.Damma),
          ArabicLetters.WawWithSukun,
          ArabicLetters.LetterAlif
        )
      case (GenderType.Feminine, ConversationType.ThirdPerson) =>
        word
          .removeLastLetter()
          .replaceDiacriticsAndAppend(thirdRadicalIndex, Seq(DiacriticType.Sukun), ArabicLetters.NoonWithFatha)
      case (GenderType.Masculine, ConversationType.SecondPerson) =>
        word.replaceDiacriticsAndAppend(
          lastLetterIndex,
          Seq(DiacriticType.Damma),
          ArabicLetters.MeemWithSukun
        )
      case (GenderType.Feminine, ConversationType.SecondPerson) =>
        word.replaceDiacriticsAndAppend(
          lastLetterIndex,
          Seq(DiacriticType.Damma),
          ArabicLetters.NoonWithShaddaAndFatha
        )
      case (_, ConversationType.FirstPerson) =>
        word
          .removeLastLetter()
          .replaceDiacriticsAndAppend(
            thirdRadicalIndex,
            Seq(DiacriticType.Sukun),
            ArabicLetters.NoonWithFatha,
            ArabicLetters.LetterAlif
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
