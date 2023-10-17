package com.alphasystem
package arabic
package morphologicalengine

import arabic.model.{ ArabicLetter, ArabicLetterType, ArabicWord, DiacriticType }
import morphologicalengine.conjugation.forms.NounSupport
import morphologicalengine.conjugation.forms.noun.VerbalNoun
import conjugation.model.OutputFormat.{ BuckWalter, Html, Unicode }
import conjugation.model.{ ConjugationInput, OutputFormat, RootLetters }

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

    def toValue(outputFormat: OutputFormat): String =
      outputFormat match
        case Unicode    => src.unicode.toString
        case Html       => src.htmlCode
        case BuckWalter => src.code.toString
  }

  extension (src: DiacriticType) {

    def isFatha: Boolean = src == DiacriticType.Fatha

    def isFathatan: Boolean = src == DiacriticType.Fathatan

    def isDamma: Boolean = src == DiacriticType.Damma

    def isDammatan: Boolean = src == DiacriticType.Dammatan

    def isKasra: Boolean = src == DiacriticType.Kasra

    def isKasratan: Boolean = src == DiacriticType.Kasratan

    def isSakin: Boolean = src == DiacriticType.Sukun

    def getHamzaReplacement: ArabicLetterType =
      src match
        case DiacriticType.Fatha => ArabicLetterType.AlifHamzaAbove
        case DiacriticType.Damma => ArabicLetterType.WawHamzaAbove
        case DiacriticType.Kasra => ArabicLetterType.YaHamzaAbove
        case _                   => ArabicLetterType.Hamza
  }

  extension (src: ProcessingContext) {

    def toRootLetters: RootLetters = {
      RootLetters(
        firstRadical = src.firstRadical,
        secondRadical = src.secondRadical,
        thirdRadical = src.thirdRadical,
        fourthRadical = src.fourthRadical
      )
    }
  }

  extension (src: ArabicWord) {
    def toValue(outputFormat: OutputFormat): String =
      outputFormat match
        case Unicode    => src.unicode
        case Html       => src.htmlCode
        case BuckWalter => src.code
  }

  extension (src: ConjugationInput) {
    def verbalNouns: Seq[NounSupport] = src.verbalNounCodes.flatMap(code => VerbalNoun.byCode.get(code))
  }
}
