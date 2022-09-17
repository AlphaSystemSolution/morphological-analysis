package com.alphasystem.arabic.model

import com.alphasystem.arabic.model.ArabicLetterType.*

enum RootType(val label: ArabicWord) {

  case CONSONANT extends RootType(ArabicWord(SAD, HHA, YA, HHA))

  case WEAK extends RootType(ArabicWord(MEEM, AIN, TA, LAM))
}
