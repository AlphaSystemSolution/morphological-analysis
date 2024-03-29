package com.alphasystem
package arabic
package morphologicalengine
package conjugation
package rule
package processors

import arabic.model.{ ArabicLetter, ArabicLetterType, ArabicWord, SarfMemberType }
import conjugation.model.internal.RootWord

/** Instead of deleting letter we are replacing with Tatweel, remove every letter containing tatweel.
  */
class RemoveTatweel extends RuleProcessor {

  override def applyRules(
    memberType: SarfMemberType,
    baseRootWord: RootWord,
    processingContext: ProcessingContext
  ): RootWord = {
    val word = baseRootWord.derivedWord
    val letters =
      word.letters.foldLeft(Seq.empty[ArabicLetter]) { case (letters, letter) =>
        if letter.letter == ArabicLetterType.Tatweel then letters else letters :+ letter
      }
    val updatedWord = ArabicWord(letters*)
    if baseRootWord.derivedWord != updatedWord then processingContext.applyRule(getClass.getSimpleName)
    baseRootWord.copy(derivedWord = updatedWord)
  }
}

object RemoveTatweel {
  def apply(): RuleProcessor = new RemoveTatweel()
}
