package com.alphasystem
package arabic
package morphologicalengine
package conjugation
package rule
package processors

import arabic.model.{ ArabicLetter, ArabicLetterType, DiacriticType, SarfMemberType }
import conjugation.model.MorphologicalTermType
import conjugation.model.internal.RootWord

class Rule20Processor extends RuleProcessor {

  override def applyRules(
    memberType: SarfMemberType,
    baseRootWord: RootWord,
    processingContext: ProcessingContext
  ): RootWord = {
    if validateTypes(
        baseRootWord,
        invalidTerms = Seq(
          MorphologicalTermType.VerbalNoun,
          MorphologicalTermType.NounOfPlaceAndTime,
          MorphologicalTermType.Imperative,
          MorphologicalTermType.Forbidden
        )
      )
    then {
      val wordStatus = processingContext.wordStatus
      var updatedWord = baseRootWord.derivedWord

      val indexOfWawAfter4thPositions = updatedWord
        .letters
        .zipWithIndex
        .find { case (letter, index) =>
          if index >= 3 then letter.letter == ArabicLetterType.Waw else false
        }
        .map(_._2)
        .getOrElse(-1)

      val diacriticsOfWaw = updatedWord.letterAt(indexOfWawAfter4thPositions).map(_.diacritics).getOrElse(Seq())
      val previousLetterIndex = indexOfWawAfter4thPositions - 1
      val previousLetterDiacritic = updatedWord.letterAt(previousLetterIndex).flatMap(_.firstDiacritic)
      val dammaOrSukun = previousLetterDiacritic.exists { diacriticType =>
        diacriticType == DiacriticType.Damma || diacriticType == DiacriticType.Sukun
      }

      if indexOfWawAfter4thPositions > -1 && !dammaOrSukun then {
        updatedWord =
          updatedWord.replaceLetter(indexOfWawAfter4thPositions, ArabicLetter(ArabicLetterType.Ya, diacriticsOfWaw*))
        if baseRootWord.derivedWord != updatedWord then processingContext.applyRule(getClass.getSimpleName)
        baseRootWord.copy(derivedWord = updatedWord)
      } else baseRootWord
    } else baseRootWord
  }
}

object Rule20Processor {
  def apply(): RuleProcessor = new Rule20Processor()
}
