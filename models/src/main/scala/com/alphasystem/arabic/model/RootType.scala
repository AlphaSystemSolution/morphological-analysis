package com.alphasystem.arabic.model

import com.alphasystem.arabic.model.ArabicLetterType.*

import java.lang.Enum

enum RootType(val label: ArabicWord) extends Enum[RootType] {

  case CONSONANT extends RootType(ArabicWord(SAD, HHA, YA, HHA))

  case WEAK extends RootType(ArabicWord(MEEM, AIN, TA, LAM))
}
