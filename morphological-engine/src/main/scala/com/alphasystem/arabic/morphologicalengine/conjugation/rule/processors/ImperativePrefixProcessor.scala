package com.alphasystem
package arabic
package morphologicalengine
package conjugation
package rule
package processors

import arabic.model.{ ArabicLetter, ArabicLetterType, DiacriticType, SarfMemberType }
import conjugation.model.internal.{ RootWord, WordStatus }
import morphologicalengine.conjugation.model.{ MorphologicalTermType, NamedTemplate }

class ImperativePrefixProcessor extends RuleProcessor {

  override def applyRules(
    memberType: SarfMemberType,
    baseRootWord: RootWord,
    processingContext: ProcessingContext
  ): RootWord = {
    if baseRootWord.`type` == MorphologicalTermType.Imperative then {
      val imperativeLetter =
        deriveImperativeLetter(baseRootWord, processingContext.namedTemplate, processingContext.wordStatus)
      val updatedWord = baseRootWord.derivedWord.replaceLetter(0, imperativeLetter)
      if baseRootWord.derivedWord != updatedWord then processingContext.applyRule(getClass.getSimpleName)
      baseRootWord.copy(derivedWord = updatedWord)
    } else baseRootWord
  }

  private def deriveImperativeLetter(rootWord: RootWord, namedTemplate: NamedTemplate, wordStatus: WordStatus) = {
    var imperativeLetter = ArabicLetterType.Hamza
    val rootLetter = rootWord.rootLetter
    val secondRadical = rootLetter.secondRadical
    val baseWord = rootWord.baseWord

    val secondRadicalDiacritics = baseWord.letters(secondRadical.index).diacritics
    val secondRadicalDiacritic = secondRadicalDiacritics.headOption.getOrElse(DiacriticType.Fatha)
    var imperativeDiacritic =
      if secondRadicalDiacritic == DiacriticType.Damma then DiacriticType.Damma else DiacriticType.Kasra

    val firstRadical = rootLetter.firstRadical
    val firstRadicalDiacritics = baseWord.letters(firstRadical.index).diacritics
    val firstRadicalDiacritic = firstRadicalDiacritics.headOption.getOrElse(DiacriticType.Sukun)

    if firstRadicalDiacritic != DiacriticType.Sukun && firstRadicalDiacritic != DiacriticType.Shadda then
      imperativeLetter = ArabicLetterType.Tatweel

    if wordStatus.firstRadicalHamza && NamedTemplate.FormICategoryAGroupUTemplate == namedTemplate then
      imperativeLetter = ArabicLetterType.Tatweel
    else if NamedTemplate.FormIVTemplate == namedTemplate then {
      imperativeLetter = ArabicLetterType.Hamza
      imperativeDiacritic = DiacriticType.Fatha
    }

    ArabicLetter(imperativeLetter, imperativeDiacritic)
  }
}

object ImperativePrefixProcessor {
  def apply(): RuleProcessor = new ImperativePrefixProcessor()
}
