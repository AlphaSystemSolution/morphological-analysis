package com.alphasystem.arabic.model

import java.lang.Enum

enum DiacriticType(val code: Char, val unicode: Char)
    extends Enum[DiacriticType]
    with ArabicCharacter(code, unicode) {

  case FATHATAN extends DiacriticType('F', '\u064B')

  case DAMMATAN extends DiacriticType('N', '\u064C')

  case KASRATAN extends DiacriticType('K', '\u064D')

  case FATHA extends DiacriticType('a', '\u064E')

  case DAMMA extends DiacriticType('u', '\u064F')

  case KASRA extends DiacriticType('i', '\u0650')

  case SHADDA extends DiacriticType('~', '\u0651')

  case SUKUN extends DiacriticType('o', '\u0652')

  case MADDAH extends DiacriticType('^', '\u0653')

  case ALIF_KHAN_JAREEYA extends DiacriticType('`', '\u0670')

  override def htmlCode: String = toHtmlCodeString(unicode)
}
