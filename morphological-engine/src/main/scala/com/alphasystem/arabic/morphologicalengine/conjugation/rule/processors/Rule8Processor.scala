package com.alphasystem
package arabic
package morphologicalengine
package conjugation
package rule
package processors

import arabic.model.*
import conjugation.model.*
import conjugation.model.internal.RootWord

class Rule8Processor extends RuleProcessor {

  override def applyRules(
    memberType: SarfMemberType,
    baseRootWord: RootWord,
    processingContext: ProcessingContext
  ): RootWord = {
    if validateTypes(
        baseRootWord,
        invalidTerms = Seq(MorphologicalTermType.VerbalNoun, MorphologicalTermType.NounOfPlaceAndTime)
      )
    then {
      val rootLetter = baseRootWord.rootLetter
      val morphologicalTermType = baseRootWord.`type`
      var updatedWord = baseRootWord.derivedWord
      val wordStatus = processingContext.wordStatus

      val weakLetterIndex =
        if wordStatus.hollow then baseRootWord.secondRadicalIndex else baseRootWord.thirdRadicalIndex
      val nextIndex = if weakLetterIndex >= updatedWord.length - 1 then -1 else weakLetterIndex + 1
      val nextLetter = updatedWord.letterAt(nextIndex)
      val nextLetterType = nextLetter.map(_.letter)
      val nextLetterDiacritic = nextLetter.flatMap(_.firstDiacritic)

      val alifOfTasnia =
        wordStatus.defective && morphologicalTermType.isVerbBasedType && nextLetterType.contains(ArabicLetterType.Alif)

      val maddaExtra = updatedWord.isMaddaExtra(morphologicalTermType)

      val baseCondition =
        isMutaharik(baseRootWord.secondRadicalDiacritic) || isMutaharik(
          baseRootWord.thirdRadicalDiacritic
        )

      val passiveParticipleType = validateTypes(
        baseRootWord,
        validTerms =
          Seq(MorphologicalTermType.PassiveParticipleMasculine, MorphologicalTermType.PassiveParticipleFeminine)
      )

      if !baseCondition || !wordStatus.weak || wordStatus.assimilated // condition 1
        || (wordStatus.hollow && wordStatus.doublyWeak) // condition 2
        || alifOfTasnia // condition 3
        || (maddaExtra && !passiveParticipleType) // condition 4
      then {
        baseRootWord
      } else {
        // if last letter needs to be replaced then use AlifMaksura
        val weakLetter = updatedWord.letterAt(weakLetterIndex)
        val weakLetterLetterType = weakLetter.map(_.letter)
        val weakLetterDiacritic = weakLetter.flatMap(_.firstDiacritic)
        var replacementLetter =
          if weakLetterIndex == updatedWord.length - 1 && weakLetterLetterType.contains(ArabicLetterType.Ya) then
            ArabicLetters.LetterAlifMaksura
          else ArabicLetters.LetterAlif

        val previousIndex = weakLetterIndex - 1
        val previousLetterDiacritic = updatedWord.letterAt(previousIndex).flatMap(_.firstDiacritic)

        if previousIndex > -1 then {
          if previousLetterDiacritic.exists(_.isSakin) then
            updatedWord = weakLetterDiacritic
              .map { diacriticType =>
                val dt =
                  if passiveParticipleType && weakLetterLetterType.contains(ArabicLetterType.Ya) then
                    DiacriticType.Kasra
                  else diacriticType
                updatedWord.replaceDiacritics(previousIndex, dt)
              }
              .getOrElse(updatedWord)
              .replaceDiacritics(weakLetterIndex, DiacriticType.Sukun)
        }

        if weakLetterDiacritic.exists(_.isFatha) then
          updatedWord = updatedWord.replaceLetter(weakLetterIndex, replacementLetter)

        if passiveParticipleType then {
          // for passive participle extra waw will be deleted instead of weak letter
          val extraWawIndex = baseRootWord.thirdRadicalIndex - 1
          updatedWord = updatedWord.replaceLetter(extraWawIndex, ArabicLetters.LetterTatweel)
        }

        if !passiveParticipleType && nextIndex > -1 then {
          val dammaOrKasrah = weakLetterDiacritic.exists { diacriticType =>
            diacriticType.isDamma || diacriticType.isKasra
          }
          val fatha = weakLetterDiacritic.exists(_.isFatha)

          if nextLetterDiacritic.exists(_.isSakin) && (dammaOrKasrah || fatha) then
            updatedWord = updatedWord.replaceLetter(weakLetterIndex, ArabicLetters.LetterTatweel)
        }

        if baseRootWord.derivedWord != updatedWord then processingContext.applyRule(getClass.getSimpleName)
        baseRootWord.copy(derivedWord = updatedWord)
      }
    } else baseRootWord
  }
}

object Rule8Processor {

  def apply(): RuleProcessor = new Rule8Processor()
}
