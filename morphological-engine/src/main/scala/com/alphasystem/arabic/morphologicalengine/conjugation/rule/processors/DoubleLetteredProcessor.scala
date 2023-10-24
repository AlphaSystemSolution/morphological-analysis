package com.alphasystem
package arabic
package morphologicalengine
package conjugation
package rule
package processors

import com.alphasystem.arabic.model.SarfMemberType
import com.alphasystem.arabic.morphologicalengine.conjugation.model.internal.RootWord

class DoubleLetteredProcessor extends RuleProcessor {

  override def applyRules(
    memberType: SarfMemberType,
    baseRootWord: RootWord,
    processingContext: ProcessingContext
  ): RootWord = {
    if processingContext.wordStatus.doubledLettered then {
      val word = baseRootWord.derivedWord
      val secondRadicalIndex = baseRootWord.rootLetter.secondRadical.index
      val firstLetterIndex = secondRadicalIndex

      // TODO: implement
      baseRootWord
    } else baseRootWord
  }
}
