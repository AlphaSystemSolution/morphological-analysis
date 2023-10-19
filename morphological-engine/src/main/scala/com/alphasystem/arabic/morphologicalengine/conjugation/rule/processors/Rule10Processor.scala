package com.alphasystem
package arabic
package morphologicalengine
package conjugation
package rule
package processors

import arabic.model.{ ArabicLetterType, ArabicLetters, DiacriticType, HiddenPronounStatus, SarfMemberType }
import conjugation.model.MorphologicalTermType
import conjugation.model.internal.RootWord

class Rule10Processor extends RuleProcessor {

  import Rule10Processor.*

  override def applyRules(
    memberType: SarfMemberType,
    baseRootWord: RootWord,
    processingContext: ProcessingContext
  ): RootWord = {
    val wordStatus = processingContext.wordStatus
    if validateTypes(
        baseRootWord,
        invalidTerms = Seq(MorphologicalTermType.Imperative, MorphologicalTermType.Forbidden)
      )
    then {
      if wordStatus.defective then {
        var updatedWord = baseRootWord.derivedWord

        val thirdRadicalDiacritic = baseRootWord.thirdRadicalDiacritic
        val thirdRadicalIndex = baseRootWord.thirdRadicalIndex
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
          updatedWord = updatedWord
            .letterAt(thirdRadicalIndex)
            .flatMap(_.firstDiacritic)
            .map(diacriticType => updatedWord.replaceDiacritics(previousLetterIndex, diacriticType))
            .getOrElse(updatedWord)
            .replaceDiacritics(thirdRadicalIndex, DiacriticType.Sukun)
        }

        if wordStatus.thirdRadicalYa && previousLetterDiacritic.exists(_.isKasra) && nextLetterType.contains(
            ArabicLetterType.Waw
          )
        then
          updatedWord = updatedWord
            .letterAt(thirdRadicalIndex)
            .flatMap(_.firstDiacritic)
            .map(diacriticType => updatedWord.replaceDiacritics(previousLetterIndex, diacriticType))
            .getOrElse(updatedWord)
            .replaceLetter(thirdRadicalIndex, ArabicLetters.WawWithSukun)

        baseRootWord.copy(derivedWord = updatedWord)
      } else baseRootWord
    } else baseRootWord
  }
}

object Rule10Processor {

  private val AllowedTypes =
    Seq(
      HiddenPronounStatus.ThirdPersonMasculineSingular,
      HiddenPronounStatus.ThirdPersonFeminineSingular,
      HiddenPronounStatus.SecondPersonMasculineSingular,
      HiddenPronounStatus.FirstPersonSingular,
      HiddenPronounStatus.FirstPersonPlural
    )

  def apply(): RuleProcessor = new Rule10Processor()
}
