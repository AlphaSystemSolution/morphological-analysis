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
        Seq(MorphologicalTermType.PresentTense, MorphologicalTermType.PresentPassiveTense)
      )
    then {
      var derivedWord = baseRootWord.derivedWord
      val wordStatus = processingContext.wordStatus
      val maybeDiacriticOfImperfectSign = derivedWord.firstLetter.flatMap(_.firstDiacritic)

      if wordStatus.firstRadicalWaw && isFatha(maybeDiacriticOfImperfectSign) then {
        val diacriticOfImperfectSign = maybeDiacriticOfImperfectSign.get

        val rootLetter = baseRootWord.rootLetter
        val secondRadical = rootLetter.secondRadical
        val maybeSecondRadicalLetter = derivedWord.letterAt(secondRadical.index)
        val secondRadicalLetterType = maybeSecondRadicalLetter.map(_.letter).getOrElse(ArabicLetterType.Tatweel)
        val maybeSecondRadicalDiacritic = maybeSecondRadicalLetter.flatMap(_.firstDiacritic)

        val thirdRadical = rootLetter.thirdRadical
        val maybeThirdRadicalLetter = derivedWord.letterAt(thirdRadical.index)
        val thirdRadicalLetterType = maybeThirdRadicalLetter.map(_.letter).getOrElse(ArabicLetterType.Tatweel)

        val containsHeavyLetters =
          HeavyLetters.contains(secondRadicalLetterType) || HeavyLetters.contains(thirdRadicalLetterType)

        val condition1 = isKasra(maybeSecondRadicalDiacritic)
        val condition2 = isFatha(maybeSecondRadicalDiacritic) && containsHeavyLetters
        derivedWord = if condition1 || condition2 then {
          derivedWord.replaceLetter(rootLetter.firstRadical.index, ArabicLetters.LetterTatweel)
        } else derivedWord

        baseRootWord.copy(derivedWord = derivedWord)
      } else baseRootWord

    } else baseRootWord
  }
}

object Rule1Processor {
  def apply(): RuleProcessor = new Rule1Processor()
}
