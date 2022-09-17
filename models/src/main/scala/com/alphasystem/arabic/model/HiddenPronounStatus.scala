package com.alphasystem.arabic.model

import com.alphasystem.arabic.model.ArabicLetterType.*
import com.alphasystem.arabic.model.DiacriticType.*

enum HiddenPronounStatus(
  val conversationLabel: ArabicWord,
  val genderLabel: ArabicWord,
  val numberLabel: ArabicWord,
  val proNoun: ProNoun)
    extends SarfMemberType {

  case THIRD_PERSON_MASCULINE_SINGULAR
      extends HiddenPronounStatus(
        ArabicWord(GHAIN, ALIF, YA_HAMZA_ABOVE, BA),
        ArabicWord(MEEM, THAL, KAF, RA),
        ArabicWord(MEEM, FA, RA, DAL),
        ProNoun.THIRD_PERSON_MASCULINE_SINGULAR
      )

  case THIRD_PERSON_MASCULINE_DUAL
      extends HiddenPronounStatus(
        ArabicWord(GHAIN, ALIF, YA_HAMZA_ABOVE, BA),
        ArabicWord(MEEM, THAL, KAF, RA),
        ArabicWord(MEEM, THA, NOON, ALIF_MAKSURA),
        ProNoun.THIRD_PERSON_MASCULINE_DUAL
      )

  case THIRD_PERSON_MASCULINE_PLURAL
      extends HiddenPronounStatus(
        ArabicWord(GHAIN, ALIF, YA_HAMZA_ABOVE, BA),
        ArabicWord(MEEM, THAL, KAF, RA),
        ArabicWord(JEEM, MEEM, AIN),
        ProNoun.THIRD_PERSON_MASCULINE_PLURAL
      )

  case THIRD_PERSON_FEMININE_SINGULAR
      extends HiddenPronounStatus(
        ArabicWord(GHAIN, ALIF, YA_HAMZA_ABOVE, BA),
        ArabicWord(MEEM, WAW_HAMZA_ABOVE, NOON, THA),
        ArabicWord(MEEM, FA, RA, DAL),
        ProNoun.THIRD_PERSON_FEMININE_SINGULAR
      )

  case THIRD_PERSON_FEMININE_DUAL
      extends HiddenPronounStatus(
        ArabicWord(GHAIN, ALIF, YA_HAMZA_ABOVE, BA),
        ArabicWord(MEEM, WAW_HAMZA_ABOVE, NOON, THA),
        ArabicWord(MEEM, THA, NOON, ALIF_MAKSURA),
        ProNoun.THIRD_PERSON_FEMININE_DUAL
      )

  case THIRD_PERSON_FEMININE_PLURAL
      extends HiddenPronounStatus(
        ArabicWord(GHAIN, ALIF, YA_HAMZA_ABOVE, BA),
        ArabicWord(MEEM, WAW_HAMZA_ABOVE, NOON, THA),
        ArabicWord(JEEM, MEEM, AIN),
        ProNoun.THIRD_PERSON_FEMININE_PLURAL
      )

  case SECOND_PERSON_MASCULINE_SINGULAR
      extends HiddenPronounStatus(
        ArabicWord(MEEM, KHA, ALIF, TTA, BA),
        ArabicWord(MEEM, THAL, KAF, RA),
        ArabicWord(MEEM, FA, RA, DAL),
        ProNoun.SECOND_PERSON_MASCULINE_SINGULAR
      )

  case SECOND_PERSON_MASCULINE_DUAL
      extends HiddenPronounStatus(
        ArabicWord(MEEM, KHA, ALIF, TTA, BA),
        ArabicWord(MEEM, THAL, KAF, RA),
        ArabicWord(MEEM, THA, NOON, ALIF_MAKSURA),
        ProNoun.SECOND_PERSON_MASCULINE_DUAL
      )

  case SECOND_PERSON_MASCULINE_PLURAL
      extends HiddenPronounStatus(
        ArabicWord(MEEM, KHA, ALIF, TTA, BA),
        ArabicWord(MEEM, THAL, KAF, RA),
        ArabicWord(JEEM, MEEM, AIN),
        ProNoun.SECOND_PERSON_MASCULINE_PLURAL
      )

  case SECOND_PERSON_FEMININE_SINGULAR
      extends HiddenPronounStatus(
        ArabicWord(MEEM, KHA, ALIF, TTA, BA),
        ArabicWord(MEEM, WAW_HAMZA_ABOVE, NOON, THA),
        ArabicWord(MEEM, FA, RA, DAL),
        ProNoun.SECOND_PERSON_FEMININE_SINGULAR
      )

  case SECOND_PERSON_FEMININE_DUAL
      extends HiddenPronounStatus(
        ArabicWord(MEEM, KHA, ALIF, TTA, BA),
        ArabicWord(MEEM, WAW_HAMZA_ABOVE, NOON, THA),
        ArabicWord(MEEM, THA, NOON, ALIF_MAKSURA),
        ProNoun.SECOND_PERSON_FEMININE_DUAL
      )

  case SECOND_PERSON_FEMININE_PLURAL
      extends HiddenPronounStatus(
        ArabicWord(MEEM, KHA, ALIF, TTA, BA),
        ArabicWord(MEEM, WAW_HAMZA_ABOVE, NOON, THA),
        ArabicWord(JEEM, MEEM, AIN),
        ProNoun.SECOND_PERSON_FEMININE_PLURAL
      )

  case FIRST_PERSON_SINGULAR
      extends HiddenPronounStatus(
        ArabicWord(MEEM, TA, KAF, LAM, MEEM),
        ArabicWord(SPACE),
        ArabicWord(MEEM, FA, RA, DAL),
        ProNoun.FIRST_PERSON_SINGULAR
      )

  case FIRST_PERSON_PLURAL
      extends HiddenPronounStatus(
        ArabicWord(MEEM, TA, KAF, LAM, MEEM),
        ArabicWord(SPACE),
        ArabicWord(JEEM, MEEM, AIN),
        ProNoun.FIRST_PERSON_PLURAL
      )

  override def label: ArabicWord =
    conversationLabel.concatWithSpace(genderLabel, numberLabel)

  override def termName: String = toString
}
