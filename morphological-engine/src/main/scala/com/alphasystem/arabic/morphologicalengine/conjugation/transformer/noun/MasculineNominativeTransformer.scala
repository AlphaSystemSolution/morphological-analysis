package com.alphasystem
package arabic
package morphologicalengine
package conjugation
package transformer
package noun

import arabic.model.{ ArabicLetters, ArabicWord, DiacriticType }
import AbstractNounTransformer.PluralType
import morphologicalanalysis.morphology.model.Flexibility
import conjugation.model.RootWord

class MasculineNominativeTransformer(flexibility: Flexibility, pluralType: PluralType)
    extends AbstractNounTransformer(flexibility, pluralType) {

  override protected def deriveDualWord(rootWord: RootWord): Option[ArabicWord] =
    flexibility match
      case Flexibility.FullyFlexible =>
        Some(
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

  override protected def derivePluralWord(rootWord: RootWord): ArabicWord =
    flexibility match
      case Flexibility.FullyFlexible =>
        pluralType match
          case PluralType.Default =>
            rootWord
              .derivedWord
              .replaceDiacriticsAndAppend(
                variableIndex,
                Seq(DiacriticType.Damma),
                ArabicLetters.WawWithSukun,
                ArabicLetters.NoonWithFatha
              )
          case PluralType.Feminine =>
            rootWord
              .derivedWord
              .replaceDiacriticsAndAppend(
                variableIndex,
                Seq(DiacriticType.Fatha),
                ArabicLetters.LetterAlif,
                ArabicLetters.TaWithDammatan
              )

      case _ => throw new RuntimeException("Not implemented yet")
}

object MasculineNominativeTransformer {
  def apply(
    flexibility: Flexibility = Flexibility.FullyFlexible,
    pluralType: PluralType = PluralType.Default
  ): Transformer = new MasculineNominativeTransformer(flexibility, pluralType)
}
