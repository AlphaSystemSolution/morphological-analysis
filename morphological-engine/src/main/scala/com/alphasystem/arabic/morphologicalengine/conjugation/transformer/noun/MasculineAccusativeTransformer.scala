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

class MasculineAccusativeTransformer(ruleProcessor: RuleProcessor, flexibility: Flexibility)
    extends AbstractNounTransformer(ruleProcessor, flexibility) {

  override protected def deriveSingularWord(rootWord: RootWord): ArabicWord =
    flexibility match
      case Flexibility.FullyFlexible =>
        rootWord
          .derivedWord
          .replaceDiacriticsAndAppend(variableIndex, Seq(DiacriticType.Fathatan), ArabicLetters.LetterAlif)
      case Flexibility.PartlyFlexible => rootWord.derivedWord.replaceDiacritics(variableIndex, DiacriticType.Fatha)
      case Flexibility.NonFlexible    => rootWord.derivedWord

  override protected def deriveDualWord(rootWord: RootWord): Option[ArabicWord] =
    flexibility match
      case Flexibility.FullyFlexible => Some(derivePluralWord(rootWord))
      case _                         => throw new RuntimeException("Not implemented yet")

  override protected def derivePluralWord(rootWord: RootWord): ArabicWord =
    flexibility match
      case Flexibility.FullyFlexible =>
        rootWord
          .derivedWord
          .removeLastLetter()
          .replaceDiacriticsAndAppend(
            variableIndex,
            Seq(DiacriticType.Fatha),
            ArabicLetters.YaWithSukun,
            ArabicLetters.NoonWithKasra
          )
      case _ => throw new RuntimeException("Not implemented yet")
}

object MasculineAccusativeTransformer {
  def apply(ruleProcessor: RuleProcessor, flexibility: Flexibility = Flexibility.FullyFlexible): Transformer =
    new MasculineAccusativeTransformer(ruleProcessor, flexibility)
}
