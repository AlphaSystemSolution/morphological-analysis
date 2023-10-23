package com.alphasystem
package arabic
package morphologicalengine
package conjugation
package transformer
package noun

import arabic.model.{ ArabicLetter, ArabicLetters, ArabicWord, DiacriticType, HiddenNounStatus, SarfMemberType }
import AbstractNounTransformer.PluralType
import conjugation.model.internal.RootWord
import morphologicalanalysis.morphology.model.Flexibility

class MasculineAccusativeTransformer(flexibility: Flexibility, pluralType: PluralType)
    extends AbstractNounTransformer(flexibility, pluralType) {

  override protected def deriveSingularWord(rootWord: RootWord): (SarfMemberType, ArabicWord) =
    flexibility match
      case Flexibility.FullyFlexible =>
        (
          HiddenNounStatus.AccusativeSingular,
          rootWord
            .derivedWord
            .replaceDiacriticsAndAppend(variableIndex, Seq(DiacriticType.Fathatan), ArabicLetters.LetterAlif)
        )

      case Flexibility.PartlyFlexible =>
        (
          HiddenNounStatus.AccusativeSingular,
          rootWord.derivedWord.replaceDiacritics(variableIndex, DiacriticType.Fatha)
        )

      case Flexibility.NonFlexible => (HiddenNounStatus.AccusativeSingular, rootWord.derivedWord)

  override protected def deriveDualWord(rootWord: RootWord): Option[(SarfMemberType, ArabicWord)] =
    flexibility match
      case Flexibility.FullyFlexible =>
        Some(
          HiddenNounStatus.AccusativeDual,
          rootWord
            .derivedWord
            .removeLastLetter()
            .replaceDiacriticsAndAppend(
              variableIndex,
              Seq(DiacriticType.Fatha),
              ArabicLetters.YaWithSukun,
              ArabicLetters.NoonWithKasra
            )
        )

      case _ => throw new RuntimeException("Not implemented yet")

  override protected def derivePluralWord(rootWord: RootWord): (SarfMemberType, ArabicWord) =
    flexibility match
      case Flexibility.FullyFlexible =>
        pluralType match
          case PluralType.Default =>
            (
              HiddenNounStatus.AccusativePlural,
              rootWord
                .derivedWord
                .removeLastLetter()
                .replaceDiacriticsAndAppend(
                  variableIndex,
                  Seq(DiacriticType.Kasra),
                  ArabicLetters.YaWithSukun,
                  ArabicLetters.NoonWithFatha
                )
            )

          case PluralType.Feminine =>
            (
              HiddenNounStatus.AccusativePlural,
              rootWord
                .derivedWord
                .removeLastLetter()
                .replaceDiacriticsAndAppend(
                  variableIndex,
                  Seq(DiacriticType.Fatha),
                  ArabicLetters.LetterAlif,
                  ArabicLetters.TaWithKasratan
                )
            )

      case _ => throw new RuntimeException("Not implemented yet")
}

object MasculineAccusativeTransformer {
  def apply(
    flexibility: Flexibility = Flexibility.FullyFlexible,
    pluralType: PluralType = PluralType.Default
  ): Transformer = new MasculineAccusativeTransformer(flexibility, pluralType)
}
