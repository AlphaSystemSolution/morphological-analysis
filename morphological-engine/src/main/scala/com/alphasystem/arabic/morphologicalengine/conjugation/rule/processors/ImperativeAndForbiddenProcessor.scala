package com.alphasystem
package arabic
package morphologicalengine
package conjugation
package rule
package processors

import arabic.model.HiddenPronounStatus.*
import arabic.model.{ ArabicLetters, ArabicWord, DiacriticType, HiddenPronounStatus, SarfMemberType }
import arabic.morphologicalengine.conjugation.model.MorphologicalTermType
import conjugation.model.internal.RootWord

class ImperativeAndForbiddenProcessor extends RuleProcessor {

  override def applyRules(
    memberType: SarfMemberType,
    baseRootWord: RootWord,
    processingContext: ProcessingContext
  ): RootWord =
    if validateTypes(baseRootWord, Seq(MorphologicalTermType.Imperative, MorphologicalTermType.Forbidden)) then {
      val updatedWord =
        memberType match
          case status: HiddenPronounStatus => process(status, baseRootWord, processingContext)
          case _                           => baseRootWord.derivedWord

      if baseRootWord.derivedWord != updatedWord then processingContext.applyRule(getClass.getSimpleName)
      baseRootWord.copy(derivedWord = updatedWord)
    } else baseRootWord

  private def process(
    status: HiddenPronounStatus,
    baseRootWord: RootWord,
    processingContext: ProcessingContext
  ): ArabicWord = {
    val termType = baseRootWord.`type`
    val updatedWord = makeJazim(status, baseRootWord.thirdRadicalIndex, baseRootWord.derivedWord)
    if baseRootWord.`type` == MorphologicalTermType.Imperative then
      updatedWord.replaceLetter(0, ArabicLetters.LetterTatweel)
    else updatedWord
  }

  private def makeJazim(status: HiddenPronounStatus, thirdRadicalIndex: Int, word: ArabicWord) = {
    val lastLetterIndex = word.length - 1
    status match
      case SecondPersonMasculineSingular => word.replaceDiacritics(lastLetterIndex, DiacriticType.Sukun)

      case SecondPersonMasculineDual | SecondPersonFeminineDual =>
        word.replaceDiacriticsAndAppend(lastLetterIndex, Seq(DiacriticType.Fatha), ArabicLetters.LetterAlif)

      case SecondPersonMasculinePlural =>
        word.replaceDiacriticsAndAppend(
          lastLetterIndex,
          Seq(DiacriticType.Damma),
          ArabicLetters.WawWithSukun,
          ArabicLetters.LetterAlif
        )

      case SecondPersonFeminineSingular =>
        word.replaceDiacriticsAndAppend(lastLetterIndex, Seq(DiacriticType.Kasra), ArabicLetters.YaWithSukun)

      case SecondPersonFemininePlural =>
        word.replaceDiacriticsAndAppend(lastLetterIndex, Seq(DiacriticType.Sukun), ArabicLetters.NoonWithFatha)

      case _ => word
  }
}

object ImperativeAndForbiddenProcessor {
  def apply(): RuleProcessor = new ImperativeAndForbiddenProcessor()
}
