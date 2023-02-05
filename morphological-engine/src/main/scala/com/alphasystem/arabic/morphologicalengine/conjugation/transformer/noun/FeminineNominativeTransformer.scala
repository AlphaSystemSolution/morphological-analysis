package com.alphasystem
package arabic
package morphologicalengine
package conjugation
package transformer
package noun

import arabic.model.{ ArabicLetters, ArabicWord, DiacriticType }
import morphologicalanalysis.morphology.model.Flexibility
import conjugation.model.RootWord
import conjugation.rule.RuleProcessor

class FeminineNominativeTransformer(ruleProcessor: RuleProcessor, flexibility: Flexibility)
    extends AbstractNounTransformer(ruleProcessor, flexibility, AbstractNounTransformer.VariableIndexType.LastLetter) {

  override protected def deriveSingularWord(rootWord: RootWord): ArabicWord =
    if rootWord.isFeminine then rootWord.derivedWord
    else
      rootWord
        .derivedWord
        .replaceDiacriticsAndAppend(
          rootWord.thirdRadicalIndex,
          Seq(DiacriticType.Fatha),
          ArabicLetters.TaMarbutaWithDammatan
        )

  override protected def deriveDualWord(rootWord: RootWord): Option[ArabicWord] =
    Some(
      rootWord
        .derivedWord
        .removeLastLetterAndAppend(ArabicLetters.TaWithFatha, ArabicLetters.LetterAlif, ArabicLetters.NoonWithKasra)
    )

  override protected def derivePluralWord(rootWord: RootWord): ArabicWord =
    rootWord.derivedWord.removeLastLetterAndAppend(ArabicLetters.LetterAlif, ArabicLetters.TaWithDammatan)
}

object FeminineNominativeTransformer {
  def apply(ruleProcessor: RuleProcessor, flexibility: Flexibility = Flexibility.FullyFlexible): Transformer =
    new FeminineNominativeTransformer(ruleProcessor, flexibility)
}