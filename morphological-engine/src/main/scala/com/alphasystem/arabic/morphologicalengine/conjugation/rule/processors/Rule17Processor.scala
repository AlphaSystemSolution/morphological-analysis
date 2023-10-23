package com.alphasystem
package arabic
package morphologicalengine
package conjugation
package rule
package processors

import arabic.model.{ ArabicLetter, ArabicLetterType, SarfMemberType }
import conjugation.model.MorphologicalTermType
import conjugation.model.internal.RootWord

class Rule17Processor extends RuleProcessor {

  override def applyRules(
    memberType: SarfMemberType,
    baseRootWord: RootWord,
    processingContext: ProcessingContext
  ): RootWord = {
    val wordStatus = processingContext.wordStatus
    if validateTypes(
        baseRootWord,
        Seq(MorphologicalTermType.ActiveParticipleMasculine, MorphologicalTermType.ActiveParticipleFeminine)
      ) && wordStatus.hollow && processingContext.pastTenseHasTransformed
    then {
      var updatedWord = baseRootWord.derivedWord
      val secondRadicalLetters = updatedWord.letterAt(baseRootWord.secondRadicalIndex)
      val secondRadicalDiacritics = secondRadicalLetters.map(_.diacritics).getOrElse(Seq())
      updatedWord = updatedWord.replaceLetter(
        baseRootWord.secondRadicalIndex,
        ArabicLetter(ArabicLetterType.Hamza, secondRadicalDiacritics*)
      )

      if baseRootWord.derivedWord != updatedWord then processingContext.applyRule(getClass.getSimpleName)
      baseRootWord.copy(derivedWord = updatedWord)
    } else baseRootWord
  }
}

object Rule17Processor {
  def apply(): RuleProcessor = new Rule17Processor()
}
