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

class MasculineGenitiveTransformer(flexibility: Flexibility, pluralType: PluralType)
    extends AbstractNounTransformer(flexibility, pluralType) {

  override protected def deriveSingularWord(rootWord: RootWord): (SarfMemberType, ArabicWord) =
    flexibility match
      case Flexibility.FullyFlexible =>
        (
          HiddenNounStatus.GenitiveSingular,
          rootWord
            .derivedWord
            .replaceDiacriticsAndAppend(variableIndex, Seq(DiacriticType.Kasratan))
        )

      case Flexibility.PartlyFlexible =>
        (HiddenNounStatus.GenitiveSingular, rootWord.derivedWord.replaceDiacritics(variableIndex, DiacriticType.Fatha))

      case Flexibility.NonFlexible => (HiddenNounStatus.GenitiveSingular, rootWord.derivedWord)

  override protected def deriveDualWord(rootWord: RootWord): Option[(SarfMemberType, ArabicWord)] = Some(
    HiddenNounStatus.GenitiveDual,
    rootWord
      .derivedWord
      .replaceDiacriticsAndAppend(
        variableIndex,
        Seq(DiacriticType.Fatha),
        ArabicLetters.YaWithSukun,
        ArabicLetters.NoonWithKasra
      )
  )

  override protected def derivePluralWord(rootWord: RootWord): (SarfMemberType, ArabicWord) =
    flexibility match
      case Flexibility.FullyFlexible =>
        pluralType match
          case PluralType.Default =>
            (
              HiddenNounStatus.GenitivePlural,
              rootWord
                .derivedWord
                .replaceDiacriticsAndAppend(
                  variableIndex,
                  Seq(DiacriticType.Kasra),
                  ArabicLetters.YaWithSukun,
                  ArabicLetters.NoonWithFatha
                )
            )

          case PluralType.Feminine =>
            (
              HiddenNounStatus.GenitivePlural,
              rootWord
                .derivedWord
                .replaceDiacriticsAndAppend(
                  variableIndex,
                  Seq(DiacriticType.Fatha),
                  ArabicLetters.LetterAlif,
                  ArabicLetters.TaWithKasratan
                )
            )

      case _ => throw new RuntimeException("Not implemented yet")
}

object MasculineGenitiveTransformer {
  def apply(
    flexibility: Flexibility = Flexibility.FullyFlexible,
    pluralType: PluralType = PluralType.Default
  ): Transformer = new MasculineGenitiveTransformer(flexibility, pluralType)
}
