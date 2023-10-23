package com.alphasystem
package arabic
package morphologicalengine
package conjugation
package transformer
package noun

import arabic.model.{ ArabicLetters, ArabicWord, DiacriticType, HiddenNounStatus, SarfMemberType }
import conjugation.model.internal.RootWord

class FeminineNominativeTransformer
    extends AbstractNounTransformer(
      variableIndexType = AbstractNounTransformer.VariableIndexType.LastLetter
    ) {

  override protected def deriveSingularWord(rootWord: RootWord): (SarfMemberType, ArabicWord) =
    if rootWord.isFeminine then (HiddenNounStatus.NominativeSingular, rootWord.derivedWord)
    else
      (
        HiddenNounStatus.NominativeSingular,
        rootWord
          .derivedWord
          .replaceDiacriticsAndAppend(
            rootWord.thirdRadicalIndex,
            Seq(DiacriticType.Fatha),
            ArabicLetters.TaMarbutaWithDammatan
          )
      )

  override protected def deriveDualWord(rootWord: RootWord): Option[(SarfMemberType, ArabicWord)] =
    Some(
      HiddenNounStatus.NominativeDual,
      rootWord
        .derivedWord
        .removeLastLetterAndAppend(ArabicLetters.TaWithFatha, ArabicLetters.LetterAlif, ArabicLetters.NoonWithKasra)
    )

  override protected def derivePluralWord(rootWord: RootWord): (SarfMemberType, ArabicWord) =
    (
      HiddenNounStatus.NominativePlural,
      rootWord.derivedWord.removeLastLetterAndAppend(ArabicLetters.LetterAlif, ArabicLetters.TaWithDammatan)
    )
}

object FeminineNominativeTransformer {
  def apply(): Transformer = new FeminineNominativeTransformer()
}
