package com.alphasystem.arabic.model

import com.alphasystem.arabic.model.ArabicLetterType.*

enum HiddenNounStatus(
  override val label: ArabicWord,
  val numberLabel: ArabicWord)
    extends SarfMemberType {

  case NOMINATIVE_SINGULAR
      extends HiddenNounStatus(
        ArabicWord(MEEM, RA, FA, WAW, AIN),
        ArabicWord(MEEM, FA, RA, DAL)
      )

  case NOMINATIVE_DUAL
      extends HiddenNounStatus(
        ArabicWord(MEEM, RA, FA, WAW, AIN),
        ArabicWord(MEEM, THA, NOON, ALIF_MAKSURA)
      )

  case NOMINATIVE_PLURAL
      extends HiddenNounStatus(
        ArabicWord(MEEM, RA, FA, WAW, AIN),
        ArabicWord(JEEM, MEEM, AIN)
      )

  case ACCUSATIVE_SINGULAR
      extends HiddenNounStatus(
        ArabicWord(MEEM, NOON, SAD, WAW, BA),
        ArabicWord(MEEM, FA, RA, DAL)
      )

  case ACCUSATIVE_DUAL
      extends HiddenNounStatus(
        ArabicWord(MEEM, NOON, SAD, WAW, BA),
        ArabicWord(MEEM, THA, NOON, ALIF_MAKSURA)
      )

  case ACCUSATIVE_PLURAL
      extends HiddenNounStatus(
        ArabicWord(MEEM, NOON, SAD, WAW, BA),
        ArabicWord(JEEM, MEEM, AIN)
      )

  case GENITIVE_SINGULAR
      extends HiddenNounStatus(
        ArabicWord(MEEM, JEEM, RA, WAW, RA),
        ArabicWord(MEEM, FA, RA, DAL)
      )

  case GENITIVE_DUAL
      extends HiddenNounStatus(
        ArabicWord(MEEM, JEEM, RA, WAW, RA),
        ArabicWord(MEEM, THA, NOON, ALIF_MAKSURA)
      )

  case GENITIVE_PLURAL
      extends HiddenNounStatus(
        ArabicWord(MEEM, JEEM, RA, WAW, RA),
        ArabicWord(MEEM, THA, NOON, ALIF_MAKSURA)
      )

  // override def toLabel: ArabicWord = label.concatWithSpace(numberLabel)

  override def termName: String = toString
}
