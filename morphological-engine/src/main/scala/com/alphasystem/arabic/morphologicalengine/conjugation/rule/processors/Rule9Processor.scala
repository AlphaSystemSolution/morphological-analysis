package com.alphasystem
package arabic
package morphologicalengine
package conjugation
package rule
package processors

import arabic.model.{ ArabicLetters, DiacriticType, SarfMemberType }
import conjugation.model.MorphologicalTermType
import conjugation.model.internal.RootWord

class Rule9Processor extends RuleProcessor {

  override def applyRules(
    memberType: SarfMemberType,
    baseRootWord: RootWord,
    processingContext: ProcessingContext
  ): RootWord = {
    val wordStatus = processingContext.wordStatus
    if validateTypes(
        baseRootWord,
        Seq(MorphologicalTermType.PastPassiveTense)
      )
    then {
      if wordStatus.hollow then {
        var updatedWord = baseRootWord.derivedWord
        val weakLetterIndex = baseRootWord.secondRadicalIndex
        val weakLetter = updatedWord.letterAt(weakLetterIndex)
        val weakLetterDiacritic = weakLetter.flatMap(_.firstDiacritic).getOrElse(DiacriticType.Sukun)
        val previousIndex = weakLetterIndex - 1
        val previousLetterDiacritic = updatedWord.letterAt(previousIndex).flatMap(_.firstDiacritic)

        if previousIndex > -1 then {
          updatedWord = updatedWord
            .replaceDiacritics(previousIndex, weakLetterDiacritic)
            .replaceLetter(weakLetterIndex, ArabicLetters.YaWithSukun)
        }

        val diacriticForWeakSecondRadicalWaw = processingContext.diacriticForWeakSecondRadicalWaw
        val bool = validateHiddenPronounTypeMembers(memberType, FromThirdPersonFemininePluralToEnd)
        if diacriticForWeakSecondRadicalWaw.exists(_.isFatha) && bool then
          updatedWord = updatedWord.replaceDiacritics(baseRootWord.firstRadicalIndex, DiacriticType.Damma)

        if processingContext.wordStatus.secondRadicalYa || (processingContext
            .wordStatus
            .secondRadicalWaw && diacriticForWeakSecondRadicalWaw.exists(_.isKasra) && bool)
        then updatedWord = updatedWord.replaceDiacritics(baseRootWord.firstRadicalIndex, DiacriticType.Kasra)

        if baseRootWord.derivedWord != updatedWord then processingContext.applyRule(getClass.getSimpleName)
        baseRootWord.copy(derivedWord = updatedWord)
      } else baseRootWord
    } else baseRootWord
  }
}

object Rule9Processor {
  def apply(): RuleProcessor = new Rule9Processor()
}
