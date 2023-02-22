package com.alphasystem
package arabic
package morphologicalengine
package conjugation
package rule
package processors

import arabic.model.{ ArabicLetter, ArabicLetterType, DiacriticType }
import conjugation.model.internal.{ RootWord, WordStatus }
import morphologicalengine.conjugation.model.{ MorphologicalTermType, NamedTemplate }

class ImperativeProcessor extends RuleProcessor {

  override def applyRules(baseRootWord: RootWord, processingContext: ProcessingContext): RootWord = {
    if baseRootWord.`type` == MorphologicalTermType.Imperative then {
      val imperativeLetter =
        deriveImperativeLetter(baseRootWord, processingContext.namedTemplate, processingContext.wordStatus)
      val word = baseRootWord.derivedWord.replaceLetter(0, imperativeLetter)
      baseRootWord.copy(derivedWord = word)
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

object ImperativeProcessor {
  def apply(): RuleProcessor = new ImperativeProcessor()
}
