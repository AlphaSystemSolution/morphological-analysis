package com.alphasystem
package arabic
package morphologicalengine
package conjugation
package transformer
package noun

import arabic.model.{ ArabicLetter, ArabicLetters, ArabicWord, DiacriticType }
import conjugation.model.RootWord
import conjugation.rule.RuleProcessor

class MasculineGenitiveTransformer(ruleProcessor: RuleProcessor) extends AbstractNounTransformer(ruleProcessor) {

  override protected def deriveSingularWord(rootWord: RootWord): ArabicWord =
    rootWord
      .derivedWord
      .replaceDiacriticsAndAppend(variableIndex, Seq(DiacriticType.Kasratan))

  override protected def deriveDualWord(rootWord: RootWord): Option[ArabicWord] = Some(derivePluralWord(rootWord))

  override protected def derivePluralWord(rootWord: RootWord): ArabicWord =
    rootWord
      .derivedWord
      .replaceDiacriticsAndAppend(
        variableIndex,
        Seq(DiacriticType.Kasra),
        ArabicLetters.YaWithSukun,
        ArabicLetters.NoonWithFatha
      )
}

object MasculineGenitiveTransformer {
  def apply(ruleProcessor: RuleProcessor): Transformer = new MasculineGenitiveTransformer(ruleProcessor)
}
