package com.alphasystem
package arabic
package morphologicalengine
package conjugation
package rule
package processors

import arabic.model.{ ArabicLetter, ArabicLetterType, DiacriticType, SarfMemberType }
import conjugation.model.NamedTemplate
import conjugation.model.internal.RootWord

class FormVIIIProcessor extends RuleProcessor {

  import FormVIIIProcessor.*

  override def applyRules(
    memberType: SarfMemberType,
    baseRootWord: RootWord,
    processingContext: ProcessingContext
  ): RootWord = {
    if processingContext.namedTemplate == NamedTemplate.FormVIIITemplate then {
      var updatedWord = baseRootWord.derivedWord

      val firstRadicalIndex = baseRootWord.firstRadicalIndex
      val diacriticsOfFirstRadical = updatedWord.letterAt(firstRadicalIndex).map(_.diacritics).getOrElse(Seq())
      val indexOfTa = firstRadicalIndex + 1
      val diacriticsOfTa = updatedWord.letterAt(indexOfTa).map(_.diacritics).getOrElse(Seq())
      val firstRadicalLetterType = baseRootWord.firstRadicalLetterType

      // first radical changes
      if firstRadicalLetterType.exists(letter => FirstRadicalGroup1.contains(letter)) then {
        updatedWord = updatedWord.replaceLetter(indexOfTa, ArabicLetter(ArabicLetterType.Dal, diacriticsOfTa*))
        if firstRadicalLetterType.contains(ArabicLetterType.Thal) then
          updatedWord =
            updatedWord.replaceLetter(firstRadicalIndex, ArabicLetter(ArabicLetterType.Dal, diacriticsOfFirstRadical*))
      }

      if firstRadicalLetterType.exists(letter => FirstRadicalGroup2.contains(letter)) then {
        updatedWord = updatedWord.replaceLetter(indexOfTa, ArabicLetter(ArabicLetterType.Tta, diacriticsOfTa*))
        if firstRadicalLetterType.contains(ArabicLetterType.Dtha) then
          updatedWord =
            updatedWord.replaceLetter(firstRadicalIndex, ArabicLetter(ArabicLetterType.Tta, diacriticsOfFirstRadical*))
      }

      if firstRadicalLetterType.contains(ArabicLetterType.Tha) then
        updatedWord = updatedWord.replaceLetter(indexOfTa, ArabicLetter(ArabicLetterType.Tha, diacriticsOfTa*))

      // second radical changes
      val secondRadicalIndex = baseRootWord.secondRadicalIndex
      val secondRadicalLetterType = baseRootWord.secondRadicalLetterType
      if secondRadicalLetterType.exists(letter => SecondRadicalGroup.contains(letter)) then {
        updatedWord = updatedWord
          .replaceLetter(indexOfTa, ArabicLetter(secondRadicalLetterType.get, DiacriticType.Sukun))
          .replaceDiacritics(firstRadicalIndex, diacriticsOfTa*)

        val indexOfHamza = updatedWord.indexOf(ArabicLetterType.Hamza)
        if indexOfHamza > -1 then updatedWord = updatedWord.removeLetterAndAppend(indexOfHamza)
      }

      if baseRootWord.derivedWord != updatedWord then processingContext.applyRule(getClass.getSimpleName)
      baseRootWord.copy(derivedWord = updatedWord)
    } else baseRootWord
  }

}

object FormVIIIProcessor {

  private val FirstRadicalGroup1 =
    Seq(ArabicLetterType.Dal, ArabicLetterType.Thal, ArabicLetterType.Zain, ArabicLetterType.Ta)
  private val FirstRadicalGroup2 =
    Seq(ArabicLetterType.Sad, ArabicLetterType.Ddad, ArabicLetterType.Tta, ArabicLetterType.Dtha)
  private val SecondRadicalGroup =
    Seq(
      ArabicLetterType.Ta,
      ArabicLetterType.Tha,
      ArabicLetterType.Jeem,
      ArabicLetterType.Zain,
      ArabicLetterType.Dal,
      ArabicLetterType.Thal,
      ArabicLetterType.Seen,
      ArabicLetterType.Sheen,
      ArabicLetterType.Sad,
      ArabicLetterType.Ddad,
      ArabicLetterType.Tta,
      ArabicLetterType.Dtha
    )

  def apply(): RuleProcessor = new FormVIIIProcessor()
}
