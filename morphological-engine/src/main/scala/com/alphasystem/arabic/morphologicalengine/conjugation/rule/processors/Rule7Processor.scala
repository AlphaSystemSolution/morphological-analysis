package com.alphasystem
package arabic
package morphologicalengine
package conjugation
package rule
package processors

import arabic.model.*
import conjugation.model.*
import conjugation.model.internal.RootWord

/** if Waw or Ya with harkah is preceded by a Fatha, the Waw or Ya will be changed into Alif.
  *
  * The following conditions must be met:
  *
  *   1. The Waw or Ya must not be in place of first radical.
  *   1. The Waw or Ya must not be in place of second radical in a doubly weak word.
  *   1. The Waw or Ya must not come before Alif of tasnia.
  *   1. The Waw or Ya must not come before extra madd (madd zaida).
  */
class Rule7Processor extends RuleProcessor {

  override def applyRules(
    memberType: SarfMemberType,
    baseRootWord: RootWord,
    processingContext: ProcessingContext
  ): RootWord = {
    val wordStatus = processingContext.wordStatus
    val indexOfWeakLetter =
      if wordStatus.hollow then baseRootWord.secondRadicalIndex
      else if wordStatus.defective then baseRootWord.thirdRadicalIndex
      else -1

    if validateTypes(
        baseRootWord,
        invalidTerms = Seq(MorphologicalTermType.VerbalNoun, MorphologicalTermType.NounOfPlaceAndTime)
      ) && indexOfWeakLetter > -1
    then {
      val rootLetter = baseRootWord.rootLetter
      val morphologicalTermType = baseRootWord.`type`
      var updatedWord = baseRootWord.derivedWord
      val nextIndex = if indexOfWeakLetter >= updatedWord.length - 1 then -1 else indexOfWeakLetter + 1
      val nextLetter = updatedWord.letterAt(nextIndex)
      val nextLetterType = nextLetter.map(_.letter)
      val nextLetterDiacritic = nextLetter.flatMap(_.firstDiacritic)

      val alifOfTasnia =
        wordStatus.defective && morphologicalTermType.isVerbBasedType && nextLetterType.contains(ArabicLetterType.Alif)

      val maddaExtra = updatedWord.isMaddaExtra(morphologicalTermType)

      val previousIndex = indexOfWeakLetter - 1
      val previousLetterDiacritic = updatedWord.letterAt(previousIndex).flatMap(_.firstDiacritic)

      val baseCondition =
        ((wordStatus.hollow && isMutaharik(baseRootWord.secondRadicalDiacritic)) ||
          (wordStatus.defective && isMutaharik(baseRootWord.thirdRadicalDiacritic))) &&
          previousLetterDiacritic.exists(_.isFatha)

      if !baseCondition || !wordStatus.weak || wordStatus.assimilated // condition 1
        || (wordStatus.hollow && wordStatus.doublyWeak) // condition 2
        || alifOfTasnia // condition 3
        || maddaExtra // condition 4
      then {
        baseRootWord
      } else {
        // if last letter needs to be replaced then use AlifMaksura
        val weakLetterLetterType = updatedWord.letterAt(indexOfWeakLetter).map(_.letter)
        var replacementLetter =
          if indexOfWeakLetter == updatedWord.length - 1 && weakLetterLetterType.contains(ArabicLetterType.Ya) then
            ArabicLetters.LetterAlifMaksura
          else ArabicLetters.LetterAlif

        if nextIndex > -1 then {
          val pastFeminineType =
            morphologicalTermType == MorphologicalTermType.PastTense && Seq(
              HiddenPronounStatus.ThirdPersonFeminineSingular,
              HiddenPronounStatus.ThirdPersonFeminineDual
            ).contains(memberType) && nextLetterType.contains(ArabicLetterType.Ta)

          if nextLetterDiacritic.exists(_.isSakin) || pastFeminineType then
            replacementLetter = ArabicLetters.LetterTatweel // 7.2 & 7.3
        }
        updatedWord = updatedWord.replaceLetter(indexOfWeakLetter, replacementLetter)

        if morphologicalTermType == MorphologicalTermType.PastTense && validateHiddenPronounTypeMembers(
            memberType,
            FromThirdPersonFemininePluralToEnd
          )
        then {
          if wordStatus.secondRadicalWaw then {
            updatedWord = updatedWord.replaceDiacritics(baseRootWord.firstRadicalIndex, DiacriticType.Damma) // 7.4
            processingContext.pastTenseHasTransformed = true
          }

          if wordStatus.secondRadicalYa || baseRootWord.secondRadicalDiacritic.exists(_.isKasra) then {
            updatedWord = updatedWord.replaceDiacritics(baseRootWord.firstRadicalIndex, DiacriticType.Kasra) // 7.5
            processingContext.pastTenseHasTransformed = true
          }
        }

        if baseRootWord.derivedWord != updatedWord then processingContext.applyRule(getClass.getSimpleName)
        baseRootWord.copy(derivedWord = updatedWord)
      }
    } else baseRootWord
  }
}

object Rule7Processor {

  def apply(): RuleProcessor = new Rule7Processor()
}
