package com.alphasystem
package arabic
package morphologicalengine

import arabic.model.{ ArabicLetter, ArabicLetterType, DiacriticType }

package object conjugation {

  extension (src: ArabicLetter) {

    def geDiacriticWithoutShadda: Option[DiacriticType] = src.diacritics.filterNot(_ == DiacriticType.Shadda).headOption
  }

  extension (src: ArabicLetterType) {

    def isLongAlif: Boolean = src == ArabicLetterType.Alif || src == ArabicLetterType.AlifMaddah

    def isYa: Boolean = src == ArabicLetterType.Ya

    def isHamza: Boolean =
      src match
        case ArabicLetterType.Hamza | ArabicLetterType.AlifHamzaAbove | ArabicLetterType.WawHamzaAbove |
            ArabicLetterType.AlifHamzaBelow | ArabicLetterType.YaHamzaAbove =>
          true
        case _ => false
  }

  extension (src: DiacriticType) {

    def isFatha: Boolean = src == DiacriticType.Fatha

    def isDamma: Boolean = src == DiacriticType.Damma

    def isKasra: Boolean = src == DiacriticType.Kasra

    def isSakin: Boolean = src == DiacriticType.Sukun

    def getHamzaReplacement: ArabicLetterType =
      src match
        case DiacriticType.Fatha => ArabicLetterType.AlifHamzaAbove
        case DiacriticType.Damma => ArabicLetterType.WawHamzaAbove
        case DiacriticType.Kasra => ArabicLetterType.YaHamzaAbove
        case _                   => ArabicLetterType.Hamza
  }
}
