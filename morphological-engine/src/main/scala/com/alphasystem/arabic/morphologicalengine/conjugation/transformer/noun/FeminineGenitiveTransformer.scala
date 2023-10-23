package com.alphasystem
package arabic
package morphologicalengine
package conjugation
package transformer
package noun

import arabic.model.{ ArabicLetter, ArabicLetters, ArabicWord, DiacriticType, HiddenNounStatus, SarfMemberType }
import conjugation.model.internal.RootWord

class FeminineGenitiveTransformer
    extends AbstractNounTransformer(
      variableIndexType = AbstractNounTransformer.VariableIndexType.LastLetter
    ) {

  override protected def deriveSingularWord(rootWord: RootWord): (SarfMemberType, ArabicWord) =
    if rootWord.isFeminine then
      (
        HiddenNounStatus.GenitiveSingular,
        rootWord.derivedWord.replaceDiacriticsAndAppend(variableIndex, Seq(DiacriticType.Kasratan))
      )
    else
      (
        HiddenNounStatus.GenitiveSingular,
        rootWord
          .derivedWord
          .replaceDiacriticsAndAppend(
            rootWord.thirdRadicalIndex,
            Seq(DiacriticType.Fatha),
            ArabicLetters.TaMarbutaWithKasratan
          )
      )

  override protected def deriveDualWord(rootWord: RootWord): Option[(SarfMemberType, ArabicWord)] =
    Some(
      (
        HiddenNounStatus.GenitiveDual,
        rootWord
          .derivedWord
          .removeLastLetterAndAppend(ArabicLetters.TaWithFatha, ArabicLetters.YaWithSukun, ArabicLetters.NoonWithKasra)
      )
    )

  override protected def derivePluralWord(rootWord: RootWord): (SarfMemberType, ArabicWord) =
    (
      HiddenNounStatus.GenitivePlural,
      rootWord.derivedWord.removeLastLetterAndAppend(ArabicLetters.LetterAlif, ArabicLetters.TaWithKasratan)
    )
}

object FeminineGenitiveTransformer {
  def apply(): Transformer = new FeminineGenitiveTransformer()
}
