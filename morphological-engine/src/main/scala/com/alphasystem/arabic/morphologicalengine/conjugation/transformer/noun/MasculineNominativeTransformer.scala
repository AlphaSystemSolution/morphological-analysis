package com.alphasystem
package arabic
package morphologicalengine
package conjugation
package transformer
package noun

import arabic.model.{ ArabicLetters, ArabicWord, DiacriticType }
import conjugation.model.RootWord
import conjugation.rule.RuleProcessor

class MasculineNominativeTransformer(ruleProcessor: RuleProcessor) extends AbstractNounTransformer(ruleProcessor) {

  override protected def deriveDualWord(rootWord: RootWord): Option[ArabicWord] =
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

  override protected def derivePluralWord(rootWord: RootWord): ArabicWord =
    rootWord
      .derivedWord
      .replaceDiacriticsAndAppend(
        variableIndex,
        Seq(DiacriticType.Damma),
        ArabicLetters.WawWithSukun,
        ArabicLetters.NoonWithFatha
      )
}

object MasculineNominativeTransformer {
  def apply(ruleProcessor: RuleProcessor): Transformer = new MasculineNominativeTransformer(ruleProcessor)
}
