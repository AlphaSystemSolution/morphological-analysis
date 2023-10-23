package com.alphasystem
package arabic
package morphologicalengine
package conjugation
package transformer
package noun

import arabic.model.{ ArabicLetters, ArabicWord, DiacriticType, HiddenNounStatus, SarfMemberType }
import conjugation.model.internal.RootWord

class FeminineAccusativeTransformer
    extends AbstractNounTransformer(
      variableIndexType = AbstractNounTransformer.VariableIndexType.LastLetter
    ) {

  override protected def deriveSingularWord(rootWord: RootWord): (SarfMemberType, ArabicWord) =
    if rootWord.isFeminine then
      (
        HiddenNounStatus.AccusativeSingular,
        rootWord.derivedWord.replaceDiacriticsAndAppend(variableIndex, Seq(DiacriticType.Fathatan))
      )
    else
      (
        HiddenNounStatus.AccusativeSingular,
        rootWord
          .derivedWord
          .replaceDiacriticsAndAppend(
            rootWord.thirdRadicalIndex,
            Seq(DiacriticType.Fatha),
            ArabicLetters.TaMarbutaWithFathatan
          )
      )

  override protected def deriveDualWord(rootWord: RootWord): Option[(SarfMemberType, ArabicWord)] =
    Some(
      (
        HiddenNounStatus.AccusativeDual,
        rootWord
          .derivedWord
          .removeLastLetterAndAppend(ArabicLetters.TaWithFatha, ArabicLetters.YaWithSukun, ArabicLetters.NoonWithKasra)
      )
    )

  override protected def derivePluralWord(rootWord: RootWord): (SarfMemberType, ArabicWord) =
    (
      HiddenNounStatus.AccusativePlural,
      rootWord.derivedWord.removeLastLetterAndAppend(ArabicLetters.LetterAlif, ArabicLetters.TaWithKasratan)
    )
}

object FeminineAccusativeTransformer {
  def apply(): Transformer = new FeminineAccusativeTransformer()
}
