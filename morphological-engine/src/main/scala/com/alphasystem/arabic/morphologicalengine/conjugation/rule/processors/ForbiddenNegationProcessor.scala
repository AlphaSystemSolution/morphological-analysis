package com.alphasystem
package arabic
package morphologicalengine
package conjugation
package rule
package processors

import arabic.model.{ ArabicLetterType, ArabicWord, SarfMemberType }
import com.alphasystem.arabic.morphologicalengine.conjugation.model.MorphologicalTermType
import conjugation.model.internal.RootWord

class ForbiddenNegationProcessor extends RuleProcessor {

  private val negationPrefix = ArabicWord(ArabicLetterType.Lam, ArabicLetterType.Alif)

  override def applyRules(
    memberType: SarfMemberType,
    baseRootWord: RootWord,
    processingContext: ProcessingContext
  ): RootWord =
    if baseRootWord.`type` == MorphologicalTermType.Forbidden then {
      val word = baseRootWord.derivedWord
      baseRootWord.copy(derivedWord = negationPrefix.concatWithSpace(word))
    } else baseRootWord
}

object ForbiddenNegationProcessor {
  def apply(): RuleProcessor = new ForbiddenNegationProcessor()
}
