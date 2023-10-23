package com.alphasystem
package arabic
package morphologicalengine
package conjugation
package transformer
package noun

import arabic.model.{ ArabicLetters, ArabicWord, DiacriticType, HiddenNounStatus, SarfMemberType }
import AbstractNounTransformer.PluralType
import conjugation.model.internal.RootWord
import morphologicalanalysis.morphology.model.Flexibility

class MasculineNominativeTransformer(flexibility: Flexibility, pluralType: PluralType)
    extends AbstractNounTransformer(flexibility, pluralType) {

  override protected def deriveDualWord(rootWord: RootWord): Option[(SarfMemberType, ArabicWord)] =
    flexibility match
      case Flexibility.FullyFlexible =>
        Some(
          HiddenNounStatus.NominativeDual,
          rootWord
            .derivedWord
            .replaceDiacriticsAndAppend(
              variableIndex,
              Seq(DiacriticType.Fatha),
              ArabicLetters.LetterAlif,
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
              HiddenNounStatus.NominativePlural,
              rootWord
                .derivedWord
                .replaceDiacriticsAndAppend(
                  variableIndex,
                  Seq(DiacriticType.Damma),
                  ArabicLetters.WawWithSukun,
                  ArabicLetters.NoonWithFatha
                )
            )

          case PluralType.Feminine =>
            (
              HiddenNounStatus.NominativePlural,
              rootWord
                .derivedWord
                .replaceDiacriticsAndAppend(
                  variableIndex,
                  Seq(DiacriticType.Fatha),
                  ArabicLetters.LetterAlif,
                  ArabicLetters.TaWithDammatan
                )
            )

      case _ => throw new RuntimeException("Not implemented yet")
}

object MasculineNominativeTransformer {
  def apply(
    flexibility: Flexibility = Flexibility.FullyFlexible,
    pluralType: PluralType = PluralType.Default
  ): Transformer = new MasculineNominativeTransformer(flexibility, pluralType)
}
