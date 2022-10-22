package com.alphasystem
package arabic
package model

import ArabicLetterType.*
import DiacriticType.*

import java.lang.Enum

enum HiddenPronounStatus(
  val conversationLabel: ArabicWord,
  val genderLabel: ArabicWord,
  val numberLabel: ArabicWord,
  val proNoun: ProNoun)
    extends Enum[HiddenPronounStatus]
    with SarfMemberType {

  case ThirdPersonMasculineSingular
      extends HiddenPronounStatus(
        ArabicWord(Ghain, Alif, YaHamzaAbove, Ba),
        ArabicWord(Meem, Thal, Kaf, Ra),
        ArabicWord(Meem, Fa, Ra, Dal),
        ProNoun.ThirdPersonMasculineSingular
      )

  case ThirdPersonMasculineDual
      extends HiddenPronounStatus(
        ArabicWord(Ghain, Alif, YaHamzaAbove, Ba),
        ArabicWord(Meem, Thal, Kaf, Ra),
        ArabicWord(Meem, Tha, Noon, AlifMaksura),
        ProNoun.ThirdPersonMasculineDual
      )

  case ThirdPersonMasculinePlural
      extends HiddenPronounStatus(
        ArabicWord(Ghain, Alif, YaHamzaAbove, Ba),
        ArabicWord(Meem, Thal, Kaf, Ra),
        ArabicWord(Jeem, Meem, Ain),
        ProNoun.ThirdPersonMasculinePlural
      )

  case ThirdPersonFeminineSingular
      extends HiddenPronounStatus(
        ArabicWord(Ghain, Alif, YaHamzaAbove, Ba),
        ArabicWord(Meem, WawHamzaAbove, Noon, Tha),
        ArabicWord(Meem, Fa, Ra, Dal),
        ProNoun.ThirdPersonFeminineSingular
      )

  case ThirdPersonFeminineDual
      extends HiddenPronounStatus(
        ArabicWord(Ghain, Alif, YaHamzaAbove, Ba),
        ArabicWord(Meem, WawHamzaAbove, Noon, Tha),
        ArabicWord(Meem, Tha, Noon, AlifMaksura),
        ProNoun.ThirdPersonFeminineDual
      )

  case ThirdPersonFemininePlural
      extends HiddenPronounStatus(
        ArabicWord(Ghain, Alif, YaHamzaAbove, Ba),
        ArabicWord(Meem, WawHamzaAbove, Noon, Tha),
        ArabicWord(Jeem, Meem, Ain),
        ProNoun.ThirdPersonFemininePlural
      )

  case SecondPersonMasculineSingular
      extends HiddenPronounStatus(
        ArabicWord(Meem, Kha, Alif, Tta, Ba),
        ArabicWord(Meem, Thal, Kaf, Ra),
        ArabicWord(Meem, Fa, Ra, Dal),
        ProNoun.SecondPersonMasculineSingular
      )

  case SecondPersonMasculineDual
      extends HiddenPronounStatus(
        ArabicWord(Meem, Kha, Alif, Tta, Ba),
        ArabicWord(Meem, Thal, Kaf, Ra),
        ArabicWord(Meem, Tha, Noon, AlifMaksura),
        ProNoun.SecondPersonMasculineDual
      )

  case SecondPersonMasculinePlural
      extends HiddenPronounStatus(
        ArabicWord(Meem, Kha, Alif, Tta, Ba),
        ArabicWord(Meem, Thal, Kaf, Ra),
        ArabicWord(Jeem, Meem, Ain),
        ProNoun.SecondPersonMasculinePlural
      )

  case SecondPersonFeminineSingular
      extends HiddenPronounStatus(
        ArabicWord(Meem, Kha, Alif, Tta, Ba),
        ArabicWord(Meem, WawHamzaAbove, Noon, Tha),
        ArabicWord(Meem, Fa, Ra, Dal),
        ProNoun.SecondPersonFeminineSingular
      )

  case SecondPersonFeminineDual
      extends HiddenPronounStatus(
        ArabicWord(Meem, Kha, Alif, Tta, Ba),
        ArabicWord(Meem, WawHamzaAbove, Noon, Tha),
        ArabicWord(Meem, Tha, Noon, AlifMaksura),
        ProNoun.SecondPersonFeminineDual
      )

  case SecondPersonFemininePlural
      extends HiddenPronounStatus(
        ArabicWord(Meem, Kha, Alif, Tta, Ba),
        ArabicWord(Meem, WawHamzaAbove, Noon, Tha),
        ArabicWord(Jeem, Meem, Ain),
        ProNoun.SecondPersonFemininePlural
      )

  case FirstPersonSingular
      extends HiddenPronounStatus(
        ArabicWord(Meem, Ta, Kaf, Lam, Meem),
        ArabicWord(Space),
        ArabicWord(Meem, Fa, Ra, Dal),
        ProNoun.FirstPersonSingular
      )

  case FirstPersonPlural
      extends HiddenPronounStatus(
        ArabicWord(Meem, Ta, Kaf, Lam, Meem),
        ArabicWord(Space),
        ArabicWord(Jeem, Meem, Ain),
        ProNoun.FirstPersonPlural
      )

  override val word: ArabicWord =
    conversationLabel.concatWithSpace(genderLabel, numberLabel)

  override def termName: String = toString
}
