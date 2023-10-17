package com.alphasystem
package arabic
package morphologicalengine
package conjugation
package rule

import arabic.model.{ ArabicLetter, ArabicLetterType, ArabicWord, DiacriticType, HiddenPronounStatus, SarfMemberType }
import conjugation.model.MorphologicalTermType
import conjugation.model.internal.RootWord

import scala.annotation.tailrec

package object processors {

  val FromThirdPersonFemininePluralToEnd: Seq[HiddenPronounStatus] =
    Seq(
      HiddenPronounStatus.ThirdPersonFemininePlural,
      HiddenPronounStatus.SecondPersonMasculineSingular,
      HiddenPronounStatus.SecondPersonMasculineDual,
      HiddenPronounStatus.SecondPersonMasculinePlural,
      HiddenPronounStatus.SecondPersonFeminineSingular,
      HiddenPronounStatus.SecondPersonFeminineDual,
      HiddenPronounStatus.SecondPersonFemininePlural,
      HiddenPronounStatus.FirstPersonSingular,
      HiddenPronounStatus.FirstPersonPlural
    )

  val HeavyLetters: Seq[ArabicLetterType] =
    Seq(
      ArabicLetterType.Ha,
      ArabicLetterType.Kha,
      ArabicLetterType.Ain,
      ArabicLetterType.Ghain,
      ArabicLetterType.Hha,
      ArabicLetterType.Hamza
    )

  def validateTypes(
    rootWord: RootWord,
    validTerms: Seq[MorphologicalTermType] = Seq.empty,
    invalidTerms: Seq[MorphologicalTermType] = Seq.empty
  ): Boolean = {
    val termType = rootWord.`type`
    (validTerms.nonEmpty && validTerms.contains(termType)) || (invalidTerms.nonEmpty && !invalidTerms.contains(
      termType
    ))
  }

  def validateHiddenPronounTypeMembers(memberType: SarfMemberType, allowedTypes: Seq[HiddenPronounStatus]): Boolean = {
    memberType match
      case status: HiddenPronounStatus => allowedTypes.contains(status)
      case _                           => false
  }

  def isMutaharik(
    maybeDiacriticType: Option[DiacriticType]
  ): Boolean =
    maybeDiacriticType.exists { diacriticType =>
      diacriticType.isFatha || diacriticType.isDamma || diacriticType.isKasra || diacriticType.isFathatan ||
      diacriticType.isDammatan || diacriticType.isKasratan
    }

  extension (src: ArabicWord) {

    def isMaddaExtra(morphologicalTermType: MorphologicalTermType): Boolean = {
      val index = maddaIndex
      if index > -1 && MorphologicalTermType.NounBasedTypes.contains(morphologicalTermType) then {
        val previousLetter = src.letterAt(index - 1)
        val previousLetterType = previousLetter.map(_.letter)
        previousLetterType.contains(ArabicLetterType.Waw) || previousLetterType.contains(ArabicLetterType.Ya)
      } else false
    }

    def maddaIndex: Int = maddaIndex(-1, 1, src.letters.headOption, src.letters.tail)

    @tailrec
    private def maddaIndex(
      index: Int,
      currentIndex: Int,
      previousLetter: Option[ArabicLetter],
      letters: Seq[ArabicLetter]
    ): Int = {
      if index > -1 || letters.isEmpty then index
      else {
        val currentLetter = letters.head
        val previousLetterDiacritic = previousLetter.flatMap(_.firstDiacritic)
        val currentLetterType = currentLetter.letter
        val alifMadda = previousLetterDiacritic.exists(_.isFatha) && currentLetterType == ArabicLetterType.Alif
        val wawMadda = previousLetterDiacritic.exists(_.isDamma) && currentLetterType == ArabicLetterType.Waw
        val yaMadda = previousLetterDiacritic.exists(_.isKasra) && currentLetterType == ArabicLetterType.Ya

        val updatedValue = if alifMadda || wawMadda || yaMadda then currentIndex else index
        maddaIndex(updatedValue, currentIndex + 1, Some(currentLetter), letters.tail)
      }
    }
  }
}
