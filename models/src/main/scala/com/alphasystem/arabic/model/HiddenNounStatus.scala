package com.alphasystem
package arabic
package model

import ArabicLetterType.*

import java.lang.Enum

enum HiddenNounStatus(
  override val word: ArabicWord,
  val numberLabel: ArabicWord)
    extends Enum[HiddenNounStatus]
    with SarfMemberType {

  case NominativeSingular
      extends HiddenNounStatus(
        ArabicWord(Meem, Ra, Fa, Waw, Ain),
        ArabicWord(Meem, Fa, Ra, Dal)
      )

  case NominativeDual
      extends HiddenNounStatus(
        ArabicWord(Meem, Ra, Fa, Waw, Ain),
        ArabicWord(Meem, Tha, Noon, AlifMaksura)
      )

  case NominativePlural
      extends HiddenNounStatus(
        ArabicWord(Meem, Ra, Fa, Waw, Ain),
        ArabicWord(Jeem, Meem, Ain)
      )

  case AccusativeSingular
      extends HiddenNounStatus(
        ArabicWord(Meem, Noon, Sad, Waw, Ba),
        ArabicWord(Meem, Fa, Ra, Dal)
      )

  case AccusativeDual
      extends HiddenNounStatus(
        ArabicWord(Meem, Noon, Sad, Waw, Ba),
        ArabicWord(Meem, Tha, Noon, AlifMaksura)
      )

  case AccusativePlural
      extends HiddenNounStatus(
        ArabicWord(Meem, Noon, Sad, Waw, Ba),
        ArabicWord(Jeem, Meem, Ain)
      )

  case GenitiveSingular
      extends HiddenNounStatus(
        ArabicWord(Meem, Jeem, Ra, Waw, Ra),
        ArabicWord(Meem, Fa, Ra, Dal)
      )

  case GenitiveDual
      extends HiddenNounStatus(
        ArabicWord(Meem, Jeem, Ra, Waw, Ra),
        ArabicWord(Meem, Tha, Noon, AlifMaksura)
      )

  case GenitivePlural
      extends HiddenNounStatus(
        ArabicWord(Meem, Jeem, Ra, Waw, Ra),
        ArabicWord(Meem, Tha, Noon, AlifMaksura)
      )

  override def termName: String = name
}
