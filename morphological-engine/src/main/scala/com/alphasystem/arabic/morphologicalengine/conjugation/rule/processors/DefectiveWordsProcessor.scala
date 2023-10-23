package com.alphasystem
package arabic
package morphologicalengine
package conjugation
package rule
package processors

import arabic.model.{
  ArabicLetterType,
  ArabicLetter,
  ArabicLetters,
  DiacriticType,
  HiddenPronounStatus,
  SarfMemberType
}
import conjugation.model.MorphologicalTermType
import conjugation.model.internal.RootWord

// Rule 10, 11, and 12
class DefectiveWordsProcessor extends RuleProcessor {

  import DefectiveWordsProcessor.*

  override def applyRules(
    memberType: SarfMemberType,
    baseRootWord: RootWord,
    processingContext: ProcessingContext
  ): RootWord = {
    val wordStatus = processingContext.wordStatus
    if wordStatus.defective then {
      var updatedWord = baseRootWord.derivedWord

      val thirdRadicalIndex = baseRootWord.thirdRadicalIndex
      val thirdRadicalLetter = updatedWord.letterAt(thirdRadicalIndex)
      val thirdRadicalDiacritic = thirdRadicalLetter.flatMap(_.firstDiacritic)
      val previousLetterIndex = thirdRadicalIndex - 1
      val previousLetter = updatedWord.letterAt(previousLetterIndex)
      val previousLetterDiacritic = previousLetter.flatMap(_.firstDiacritic)
      if baseRootWord.`type` == MorphologicalTermType.PresentTense &&
        validateHiddenPronounTypeMembers(memberType, AllowedTypes)
      then {
        if previousLetterDiacritic.exists { diacriticType =>
            diacriticType.isDamma || diacriticType.isKasra
          }
        then updatedWord = updatedWord.replaceDiacritics(thirdRadicalIndex, DiacriticType.Sukun)

        if previousLetterDiacritic.exists(_.isFatha) then
          updatedWord = updatedWord.replaceLetter(thirdRadicalIndex, ArabicLetters.LetterAlifMaksura)
      }

      val nextLetterIndex = thirdRadicalIndex + 1
      val nextLetter = updatedWord.letterAt(nextLetterIndex)
      val nextLetterType = nextLetter.map(_.letter)
      val nextLetterDiacritic = nextLetter.flatMap(_.firstDiacritic)

      if wordStatus.thirdRadicalWaw && previousLetterDiacritic.exists(_.isDamma) && nextLetterType.contains(
          ArabicLetterType.Waw
        )
      then updatedWord = updatedWord.replaceDiacritics(thirdRadicalIndex, DiacriticType.Sukun)

      if wordStatus.thirdRadicalYa && previousLetterDiacritic.exists(_.isKasra) && nextLetterType.contains(
          ArabicLetterType.Ya
        )
      then updatedWord = updatedWord.replaceDiacritics(thirdRadicalIndex, DiacriticType.Sukun)

      if wordStatus.thirdRadicalWaw && previousLetterDiacritic.exists(_.isDamma) && nextLetterType.contains(
          ArabicLetterType.Ya
        )
      then {
        updatedWord = thirdRadicalDiacritic
          .map(diacriticType => updatedWord.replaceDiacritics(previousLetterIndex, diacriticType))
          .getOrElse(updatedWord)
          .replaceDiacritics(thirdRadicalIndex, DiacriticType.Sukun)
      }

      if wordStatus.thirdRadicalYa && previousLetterDiacritic.exists(_.isKasra) && nextLetterType.contains(
          ArabicLetterType.Waw
        )
      then
        updatedWord = thirdRadicalDiacritic
          .map(diacriticType => updatedWord.replaceDiacritics(previousLetterIndex, diacriticType))
          .getOrElse(updatedWord)
          .replaceLetter(thirdRadicalIndex, ArabicLetters.WawWithSukun)

      // Rule 11
      val thirdRadicalDiacritics = thirdRadicalLetter.map(_.diacritics)
      if wordStatus.thirdRadicalWaw && previousLetterDiacritic.exists(_.isKasra) then
        updatedWord = updatedWord.replaceLetter(
          thirdRadicalIndex,
          ArabicLetter(ArabicLetterType.Ya, thirdRadicalDiacritics.getOrElse(Seq())*)
        )

      // Rule 12
      if wordStatus.thirdRadicalYa && previousLetterDiacritic.exists(_.isDamma) then
        updatedWord = updatedWord.replaceLetter(
          thirdRadicalIndex,
          ArabicLetter(ArabicLetterType.Waw, thirdRadicalDiacritics.getOrElse(Seq())*)
        )

      if baseRootWord.derivedWord != updatedWord then processingContext.applyRule(getClass.getSimpleName)
      baseRootWord.copy(derivedWord = updatedWord)
    } else baseRootWord
  }
}

object DefectiveWordsProcessor {

  private val AllowedTypes =
    Seq(
      HiddenPronounStatus.ThirdPersonMasculineSingular,
      HiddenPronounStatus.ThirdPersonFeminineSingular,
      HiddenPronounStatus.SecondPersonMasculineSingular,
      HiddenPronounStatus.FirstPersonSingular,
      HiddenPronounStatus.FirstPersonPlural
    )

  def apply(): RuleProcessor = new DefectiveWordsProcessor()
}
