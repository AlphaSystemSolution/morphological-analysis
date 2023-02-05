package com.alphasystem
package arabic
package morphologicalengine
package conjugation
package transformer
package noun

import arabic.model.{ ArabicLetter, ArabicLetters, ArabicWord, DiacriticType }
import conjugation.model.RootWord
import conjugation.rule.RuleProcessor

class MasculineAccusativeTransformer(ruleProcessor: RuleProcessor) extends AbstractNounTransformer(ruleProcessor) {

  override protected def deriveSingularWord(rootWord: RootWord): ArabicWord =
    rootWord
      .derivedWord
      .replaceDiacriticsAndAppend(variableIndex, Seq(DiacriticType.Fathatan), ArabicLetters.LetterAlif)

  override protected def deriveDualWord(rootWord: RootWord): Option[ArabicWord] = Some(derivePluralWord(rootWord))

  override protected def derivePluralWord(rootWord: RootWord): ArabicWord =
    rootWord
      .derivedWord
      .removeLastLetter()
      .replaceDiacriticsAndAppend(
        variableIndex,
        Seq(DiacriticType.Fatha),
        ArabicLetters.YaWithSukun,
        ArabicLetters.NoonWithKasra
      )
}

object MasculineAccusativeTransformer {
  def apply(ruleProcessor: RuleProcessor): Transformer = new MasculineAccusativeTransformer(ruleProcessor)
}
