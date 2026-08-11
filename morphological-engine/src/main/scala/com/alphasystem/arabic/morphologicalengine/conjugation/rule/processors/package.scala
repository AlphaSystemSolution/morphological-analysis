package com.alphasystem
package arabic
package morphologicalengine
package conjugation
package rule

import arabic.model.{
  ArabicLetter,
  ArabicLetterType,
  ArabicWord,
  DiacriticType,
  HiddenPronounStatus,
  JussiveParticle,
  SarfMemberType
}
import conjugation.model.MorphologicalTermType
import conjugation.model.internal.RootWord
import conjugation.ProcessingContext

import scala.annotation.tailrec

package object processors {

  val SecondPersonTypes: Seq[HiddenPronounStatus] =
    Seq(
      HiddenPronounStatus.SecondPersonMasculineSingular,
      HiddenPronounStatus.SecondPersonFeminineSingular,
      HiddenPronounStatus.SecondPersonMasculineDual,
      HiddenPronounStatus.SecondPersonFeminineDual,
      HiddenPronounStatus.SecondPersonMasculinePlural,
      HiddenPronounStatus.SecondPersonFemininePlural
    )

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
    val hasValidTerms = validTerms.isEmpty || validTerms.contains(termType)
    val hasInvalidTerms = invalidTerms.isEmpty || !invalidTerms.contains(termType)
    if !hasValidTerms then hasValidTerms
    else if hasInvalidTerms then hasInvalidTerms
    else false
  }

  def validateHiddenPronounTypeMembers(memberType: SarfMemberType, allowedTypes: Seq[HiddenPronounStatus]): Boolean = {
    memberType match
      case status: HiddenPronounStatus => allowedTypes.contains(status)
      case _                           => false
  }

  /** Determines whether the given [[RootWord]] should be treated as a command/imperative form: either a genuine
    * [[MorphologicalTermType.Imperative]] term, or a [[MorphologicalTermType.PresentTenseJussive]] term used with the
    * [[JussiveParticle.LamOfCommand]] particle for a second person member (e.g. "لِتَفْعَلْ"), which is grammatically
    * transformed into the plain imperative (e.g. "اِفْعَلْ") rather than keeping the jussive person prefix and
    * prepending the particle.
    */
  def isCommandFormType(
    baseRootWord: RootWord,
    memberType: SarfMemberType,
    processingContext: ProcessingContext
  ): Boolean =
    baseRootWord.`type` == MorphologicalTermType.Imperative ||
      (baseRootWord.`type` == MorphologicalTermType.PresentTenseJussive &&
        processingContext.jussiveParticle.contains(JussiveParticle.LamOfCommand) &&
        validateHiddenPronounTypeMembers(memberType, SecondPersonTypes))

  def isMutaharik(
    maybeDiacriticType: Option[DiacriticType]
  ): Boolean =
    maybeDiacriticType.exists { diacriticType =>
      diacriticType.isFatha || diacriticType.isDamma || diacriticType.isKasra || diacriticType.isFathatan ||
      diacriticType.isDammatan || diacriticType.isKasratan
    }

  extension (src: ArabicWord) {

    def indexOf(letterType: ArabicLetterType): Int =
      src
        .letters
        .zipWithIndex
        .find { case (letter, index) =>
          letter.letter == letterType
        }
        .map(_._2)
        .getOrElse(-1)

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
