package com.alphasystem
package arabic
package morphologicalengine
package conjugation
package transformer
package noun

import arabic.model.{ ArabicLetter, ArabicLetters, ArabicWord, DiacriticType }
import AbstractNounTransformer.PluralType
import morphologicalanalysis.morphology.model.Flexibility
import conjugation.model.RootWord

class MasculineGenitiveTransformer(flexibility: Flexibility, pluralType: PluralType)
    extends AbstractNounTransformer(flexibility, pluralType) {

  override protected def deriveSingularWord(rootWord: RootWord): ArabicWord =
    flexibility match
      case Flexibility.FullyFlexible =>
        rootWord
          .derivedWord
          .replaceDiacriticsAndAppend(variableIndex, Seq(DiacriticType.Kasratan))
      case Flexibility.PartlyFlexible => rootWord.derivedWord.replaceDiacritics(variableIndex, DiacriticType.Fatha)
      case Flexibility.NonFlexible    => rootWord.derivedWord

  override protected def deriveDualWord(rootWord: RootWord): Option[ArabicWord] = Some(derivePluralWord(rootWord))

  override protected def derivePluralWord(rootWord: RootWord): ArabicWord =
    flexibility match
      case Flexibility.FullyFlexible =>
        pluralType match
          case PluralType.Default =>
            rootWord
              .derivedWord
              .replaceDiacriticsAndAppend(
                variableIndex,
                Seq(DiacriticType.Kasra),
                ArabicLetters.YaWithSukun,
                ArabicLetters.NoonWithFatha
              )
          case PluralType.Feminine =>
            rootWord
              .derivedWord
              .replaceDiacriticsAndAppend(
                variableIndex,
                Seq(DiacriticType.Fatha),
                ArabicLetters.LetterAlif,
                ArabicLetters.TaWithKasratan
              )

      case _ => throw new RuntimeException("Not implemented yet")
}

object MasculineGenitiveTransformer {
  def apply(
    flexibility: Flexibility = Flexibility.FullyFlexible,
    pluralType: PluralType = PluralType.Default
  ): Transformer = new MasculineGenitiveTransformer(flexibility, pluralType)
}
