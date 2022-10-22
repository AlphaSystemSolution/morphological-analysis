package com.alphasystem
package arabic
package model

import ArabicLetterType.*

import java.lang.Enum

enum RootType(val label: ArabicWord) extends Enum[RootType] {

  case Consonant extends RootType(ArabicWord(Sad, Hha, Ya, Hha))

  case Weak extends RootType(ArabicWord(Meem, Ain, Ta, Lam))
}
