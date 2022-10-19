package com.alphasystem
package arabic
package model

import ArabicLetterType.*

import java.lang.Enum

enum RootType(val label: ArabicWord) extends Enum[RootType] {

  case CONSONANT extends RootType(ArabicWord(SAD, HHA, YA, HHA))

  case WEAK extends RootType(ArabicWord(MEEM, AIN, TA, LAM))
}
