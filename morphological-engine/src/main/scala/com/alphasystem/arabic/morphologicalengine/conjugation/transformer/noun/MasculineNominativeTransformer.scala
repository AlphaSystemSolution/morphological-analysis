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

class MasculineNominativeTransformer(ruleProcessor: RuleProcessor, flexibility: Flexibility)
    extends AbstractNounTransformer(ruleProcessor, flexibility) {

  override protected def deriveDualWord(rootWord: RootWord): Option[ArabicWord] =
    flexibility match
      case Flexibility.FullyFlexible =>
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
      case _ => throw new RuntimeException("Not implemented yet")

  override protected def derivePluralWord(rootWord: RootWord): ArabicWord =
    flexibility match
      case Flexibility.FullyFlexible =>
        rootWord
          .derivedWord
          .replaceDiacriticsAndAppend(
            variableIndex,
            Seq(DiacriticType.Damma),
            ArabicLetters.WawWithSukun,
            ArabicLetters.NoonWithFatha
          )
      case _ => throw new RuntimeException("Not implemented yet")
}

object MasculineNominativeTransformer {
  def apply(ruleProcessor: RuleProcessor, flexibility: Flexibility = Flexibility.FullyFlexible): Transformer =
    new MasculineNominativeTransformer(ruleProcessor, flexibility)
}
