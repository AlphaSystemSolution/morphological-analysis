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
      val updatedWord = baseRootWord.derivedWord.prependLetters(imperativeLetter)
      if baseRootWord.derivedWord != updatedWord then processingContext.applyRule(getClass.getSimpleName)
      baseRootWord.copy(derivedWord = updatedWord)
    } else baseRootWord
  }

  private def deriveImperativeLetter(rootWord: RootWord, namedTemplate: NamedTemplate, wordStatus: WordStatus) = {
    var imperativeLetter = ArabicLetterType.Hamza
    val rootLetter = rootWord.rootLetter
    val secondRadical = rootLetter.secondRadical
    val baseWord = rootWord.derivedWord

    val secondRadicalDiacritic = baseWord.letters(secondRadical.index).firstDiacritic.getOrElse(DiacriticType.Fatha)
    var imperativeDiacritics =
      if secondRadicalDiacritic == DiacriticType.Damma then Seq(DiacriticType.Damma) else Seq(DiacriticType.Kasra)

    val firstLetterDiacritic = baseWord
      .letters
      .filterNot(_.letter == ArabicLetterType.Tatweel)
      .headOption
      .flatMap(_.firstDiacritic)
      .getOrElse(DiacriticType.Sukun)

    if firstLetterDiacritic != DiacriticType.Sukun then {
      imperativeLetter = ArabicLetterType.Tatweel
      imperativeDiacritics = Seq.empty[DiacriticType]
    }

    if wordStatus.firstRadicalHamza && NamedTemplate.FormICategoryAGroupUTemplate == namedTemplate then
      imperativeLetter = ArabicLetterType.Tatweel
    else if NamedTemplate.FormIVTemplate == namedTemplate then {
      imperativeLetter = ArabicLetterType.Hamza
      imperativeDiacritics = Seq(DiacriticType.Fatha)
    }

    ArabicLetter(imperativeLetter, imperativeDiacritics*)
  }
}

object ImperativePrefixProcessor {
  def apply(): RuleProcessor = new ImperativePrefixProcessor()
}
