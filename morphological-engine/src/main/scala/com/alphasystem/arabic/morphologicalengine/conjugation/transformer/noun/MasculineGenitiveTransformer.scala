package com.alphasystem
package arabic
package morphologicalengine
package conjugation
package transformer
package noun

import arabic.model.{ ArabicLetter, ArabicLetters, ArabicWord, DiacriticType }
import morphologicalanalysis.morphology.model.Flexibility
import conjugation.model.RootWord
import conjugation.rule.RuleProcessor

class MasculineGenitiveTransformer(ruleProcessor: RuleProcessor, flexibility: Flexibility)
    extends AbstractNounTransformer(ruleProcessor, flexibility) {

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
        rootWord
          .derivedWord
          .replaceDiacriticsAndAppend(
            variableIndex,
            Seq(DiacriticType.Kasra),
            ArabicLetters.YaWithSukun,
            ArabicLetters.NoonWithFatha
          )
      case _ => throw new RuntimeException("Not implemented yet")
}

object MasculineGenitiveTransformer {
  def apply(ruleProcessor: RuleProcessor, flexibility: Flexibility = Flexibility.FullyFlexible): Transformer =
    new MasculineGenitiveTransformer(ruleProcessor, flexibility)
}
