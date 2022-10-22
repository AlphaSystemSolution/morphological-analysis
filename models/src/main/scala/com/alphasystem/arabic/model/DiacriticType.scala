package com.alphasystem
package arabic
package model

import java.lang.Enum

enum DiacriticType(val code: Char, val unicode: Char) extends Enum[DiacriticType] with ArabicCharacter(code, unicode) {

  case Fathatan extends DiacriticType('F', '\u064B')

  case Dammatan extends DiacriticType('N', '\u064C')

  case Kasratan extends DiacriticType('K', '\u064D')

  case Fatha extends DiacriticType('a', '\u064E')

  case Damma extends DiacriticType('u', '\u064F')

  case Kasra extends DiacriticType('i', '\u0650')

  case Shadda extends DiacriticType('~', '\u0651')

  case Sukun extends DiacriticType('o', '\u0652')

  case Maddah extends DiacriticType('^', '\u0653')

  case AlifKhanJareeya extends DiacriticType('`', '\u0670')

  override def htmlCode: String = toHtmlCodeString(unicode)
}

object DiacriticType {

  lazy val CodesMap: Map[Char, DiacriticType] =
    DiacriticType.values.groupBy(_.code).map { case (c, types) =>
      c -> types.head
    }

  lazy val UnicodesMap: Map[Char, DiacriticType] =
    DiacriticType.values.groupBy(_.unicode).map { case (c, types) =>
      c -> types.head
    }
}
