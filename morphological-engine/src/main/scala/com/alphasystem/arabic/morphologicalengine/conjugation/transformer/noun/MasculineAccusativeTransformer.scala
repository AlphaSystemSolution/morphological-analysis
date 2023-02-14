package com.alphasystem
package arabic
package morphologicalengine
package conjugation
package transformer
package noun

import arabic.model.{ ArabicLetter, ArabicLetters, ArabicWord, DiacriticType }
import AbstractNounTransformer.PluralType
import conjugation.model.internal.RootWord
import morphologicalanalysis.morphology.model.Flexibility

class MasculineAccusativeTransformer(flexibility: Flexibility, pluralType: PluralType)
    extends AbstractNounTransformer(flexibility, pluralType) {

  override protected def deriveSingularWord(rootWord: RootWord): ArabicWord =
    flexibility match
      case Flexibility.FullyFlexible =>
        rootWord
          .derivedWord
          .replaceDiacriticsAndAppend(variableIndex, Seq(DiacriticType.Fathatan), ArabicLetters.LetterAlif)
      case Flexibility.PartlyFlexible => rootWord.derivedWord.replaceDiacritics(variableIndex, DiacriticType.Fatha)
      case Flexibility.NonFlexible    => rootWord.derivedWord

  override protected def deriveDualWord(rootWord: RootWord): Option[ArabicWord] =
    flexibility match
      case Flexibility.FullyFlexible => Some(derivePluralWord(rootWord))
      case _                         => throw new RuntimeException("Not implemented yet")

  override protected def derivePluralWord(rootWord: RootWord): ArabicWord =
    flexibility match
      case Flexibility.FullyFlexible =>
        pluralType match
          case PluralType.Default =>
            rootWord
              .derivedWord
              .removeLastLetter()
              .replaceDiacriticsAndAppend(
                variableIndex,
                Seq(DiacriticType.Fatha),
                ArabicLetters.YaWithSukun,
                ArabicLetters.NoonWithKasra
              )
          case PluralType.Feminine =>
            rootWord
              .derivedWord
              .removeLastLetter()
              .replaceDiacriticsAndAppend(
                variableIndex,
                Seq(DiacriticType.Fatha),
                ArabicLetters.LetterAlif,
                ArabicLetters.TaWithKasratan
              )

      case _ => throw new RuntimeException("Not implemented yet")
}

object MasculineAccusativeTransformer {
  def apply(
    flexibility: Flexibility = Flexibility.FullyFlexible,
    pluralType: PluralType = PluralType.Default
  ): Transformer = new MasculineAccusativeTransformer(flexibility, pluralType)
}
