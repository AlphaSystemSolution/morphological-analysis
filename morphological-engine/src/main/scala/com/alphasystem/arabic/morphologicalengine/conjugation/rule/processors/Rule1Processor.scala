package com.alphasystem
package arabic
package morphologicalengine
package conjugation
package rule
package processors

import arabic.model.{ ArabicLetterType, ArabicLetters, SarfMemberType }
import conjugation.model.MorphologicalTermType
import conjugation.model.internal.RootWord

/**   1. The Waw that appears between sign of imperfect tense which has Fatha and the second radical which has Kasrah
  *      falls off.
  *   1. Every Waw that comes between sign of imperfect tense which has Fatha and second radical falls off on the
  *      condition that either the second or third radical is one of throat letter
  */
class Rule1Processor extends RuleProcessor {

  override def applyRules(
    memberType: SarfMemberType,
    baseRootWord: RootWord,
    processingContext: ProcessingContext
  ): RootWord = {
    if validateTypes(
        baseRootWord,
        Seq(
          MorphologicalTermType.PresentTense,
          MorphologicalTermType.PresentPassiveTense,
          MorphologicalTermType.Imperative,
          MorphologicalTermType.Forbidden
        )
      )
    then {
      var updatedWord = baseRootWord.derivedWord
      val wordStatus = processingContext.wordStatus
      val maybeDiacriticOfImperfectSign = updatedWord.firstLetter.flatMap(_.firstDiacritic)

      if wordStatus.firstRadicalWaw && maybeDiacriticOfImperfectSign.exists(_.isFatha) then {
        val diacriticOfImperfectSign = maybeDiacriticOfImperfectSign.get

        val rootLetter = baseRootWord.rootLetter
        val secondRadicalLetterType = baseRootWord.secondRadicalLetterType.getOrElse(ArabicLetterType.Tatweel)
        val maybeSecondRadicalDiacritic = baseRootWord.secondRadicalDiacritic

        val thirdRadicalLetterType = baseRootWord.thirdRadicalLetterType.getOrElse(ArabicLetterType.Tatweel)

        val containsHeavyLetters =
          HeavyLetters.contains(secondRadicalLetterType) || HeavyLetters.contains(thirdRadicalLetterType)

        val condition1 = maybeSecondRadicalDiacritic.exists(_.isKasra)
        val condition2 = maybeSecondRadicalDiacritic.exists(_.isFatha) && containsHeavyLetters
        updatedWord = if condition1 || condition2 then {
          updatedWord.replaceLetter(rootLetter.firstRadical.index, ArabicLetters.LetterTatweel)
        } else updatedWord

        if baseRootWord.derivedWord != updatedWord then processingContext.applyRule(getClass.getSimpleName)
        baseRootWord.copy(derivedWord = updatedWord)
      } else baseRootWord

    } else baseRootWord
  }
}

object Rule1Processor {
  def apply(): RuleProcessor = new Rule1Processor()
}
