package com.alphasystem
package arabic
package morphologicalengine
package conjugation
package rule
package processors

import arabic.model.{ ArabicLetter, ArabicLetterType, ArabicLetters, ArabicWord, DiacriticType }
import conjugation.model.internal.RootWord

class HamzaReplacementProcessor extends RuleProcessor {

  override def applyRules(baseRootWord: RootWord): RootWord = {
    val currentLetters = baseRootWord.derivedWord.letters

    var previousLetterType: Option[ArabicLetterType] = None
    var previousDiacritic: Option[DiacriticType] = None

    val updatedLetters =
      (currentLetters.sliding(2).to(List) :+ List(currentLetters.last)).foldLeft(Seq.empty[ArabicLetter]) {
        case (acc, currentList) =>
          val currentLetter = currentList.head
          val currentLetterType = currentLetter.letter
          val currentDiacritic = currentLetter.geDiacriticWithoutShadda

          val nextLetter = currentList.lastOption
          val nextLetterType = nextLetter.map(_.letter)

          val updatedLetter =
            if currentLetterType == ArabicLetterType.Hamza then {
              if previousLetterType.isEmpty then {
                // Hamza is first letter
                val hamzaReplacement =
                  if currentDiacritic.exists(_.isKasra) then ArabicLetterType.AlifHamzaBelow
                  else ArabicLetterType.AlifHamzaAbove
                ArabicLetter(hamzaReplacement, currentLetter.diacritics*)
              } // end of "previousLetterType.isEmpty"
              else {
                var hamzaReplacement: Option[ArabicLetterType] = None
                var hamzaDiacritic: Option[DiacriticType] = None
                val lastLetter = currentList.size == 1

                // either determine hamzaReplacement or hamzaDiacritic
                if previousLetterType.exists(_.isLongAlif) || nextLetterType.exists(_.isLongAlif) then
                  hamzaReplacement = Some(ArabicLetterType.YaHamzaAbove)
                else if previousDiacritic.contains(currentDiacritic) then hamzaDiacritic = currentDiacritic
                else if currentDiacritic.exists(_.isSakin) || previousDiacritic.exists(_.isSakin) then
                  hamzaDiacritic = if currentDiacritic.get.isSakin then previousDiacritic else currentDiacritic
                else if currentDiacritic != previousDiacritic then {
                  if currentDiacritic.exists(_.isKasra) || previousDiacritic.exists(_.isKasra) then
                    hamzaDiacritic = Some(DiacriticType.Kasra)
                  else if currentDiacritic.exists(_.isDamma) || previousDiacritic.exists(_.isDamma) then
                    hamzaDiacritic = Some(DiacriticType.Damma)
                  else if currentDiacritic.exists(_.isFatha) || previousDiacritic.exists(_.isFatha) then
                    hamzaDiacritic = Some(DiacriticType.Fatha)
                }

                // determine correct hamzaDiacritic if we are on the lat letter
                if lastLetter then {
                  if hamzaDiacritic.exists(_.isFatha) then {
                    if previousLetterType.exists(_.isYa) then hamzaDiacritic = Some(DiacriticType.Kasra)
                  } else if hamzaDiacritic.exists(_.isDamma) then hamzaDiacritic = Some(DiacriticType.Fatha)
                }

                if lastLetter && (previousLetterType.exists(_.isLongAlif) || previousDiacritic.exists(_.isSakin)) then {
                  // last letter is Hamza, if previous letter is long Alif or previous diacritic is sukun
                  // then no replacement for Hamza
                  currentLetter
                } else {
                  if hamzaDiacritic.isDefined && hamzaReplacement.isEmpty then
                    hamzaReplacement = Some(hamzaDiacritic.get.getHamzaReplacement)

                  // finally determine current letter
                  hamzaReplacement
                    .map(letter => ArabicLetter(letter, currentLetter.diacritics*))
                    .getOrElse(currentLetter)
                }

              } // end of "else" of "previousLetterType.isEmpty"
            } // end of "currentLetterType == ArabicLetterType.Hamza"
            else currentLetter

          previousLetterType = Some(currentLetterType)
          previousDiacritic = currentDiacritic

          acc :+ updatedLetter
      }

    val firstLetter = updatedLetters.head
    val secondLetter = updatedLetters(1)
    val remainingLetters = updatedLetters.drop(2)

    // if first two letters are Hamza's then replace accordingly
    val finalLetters =
      if firstLetter.letter.isHamza && secondLetter.letter.isHamza then {
        if firstLetter.geDiacriticWithoutShadda.exists(_.isFatha) then
          ArabicLetters.LetterAlifMaddah +: remainingLetters
        else if firstLetter.geDiacriticWithoutShadda.exists(_.isKasra) then
          Seq(firstLetter, ArabicLetters.YaWithSukun) ++ remainingLetters
        else if firstLetter.geDiacriticWithoutShadda.exists(_.isDamma) then
          Seq(firstLetter, ArabicLetters.WawWithSukun) ++ remainingLetters
        else updatedLetters
      } else updatedLetters

    baseRootWord.copy(derivedWord = ArabicWord(finalLetters*))
  }
}

object HamzaReplacementProcessor {
  def apply(): HamzaReplacementProcessor = new HamzaReplacementProcessor()
}
