package com.alphasystem
package arabic
package morphologicalengine
package conjugation
package transformer
package noun

import arabic.model.{ ArabicLetters, ArabicWord, DiacriticType }
import conjugation.model.RootWord
import conjugation.rule.RuleProcessor

class FeminineAccusativeTransformer(ruleProcessor: RuleProcessor)
    extends AbstractNounTransformer(ruleProcessor, AbstractNounTransformer.VariableIndexType.LastLetter) {

  override protected def deriveSingularWord(rootWord: RootWord): ArabicWord =
    if rootWord.isFeminine then
      rootWord.derivedWord.replaceDiacriticsAndAppend(variableIndex, Seq(DiacriticType.Fathatan))
    else
      rootWord
        .derivedWord
        .replaceDiacriticsAndAppend(
          rootWord.thirdRadicalIndex,
          Seq(DiacriticType.Fatha),
          ArabicLetters.TaMarbutaWithFathatan
        )

  override protected def deriveDualWord(rootWord: RootWord): Option[ArabicWord] =
    Some(
      rootWord
        .derivedWord
        .removeLastLetterAndAppend(ArabicLetters.TaWithFatha, ArabicLetters.YaWithSukun, ArabicLetters.NoonWithKasra)
    )

  override protected def derivePluralWord(rootWord: RootWord): ArabicWord =
    rootWord.derivedWord.removeLastLetterAndAppend(ArabicLetters.LetterAlif, ArabicLetters.TaWithKasratan)
}

object FeminineAccusativeTransformer {
  def apply(ruleProcessor: RuleProcessor): Transformer = new FeminineAccusativeTransformer(ruleProcessor)
}
