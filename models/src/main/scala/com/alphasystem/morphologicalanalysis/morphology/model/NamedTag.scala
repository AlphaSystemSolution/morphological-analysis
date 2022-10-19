package com.alphasystem
package morphologicalanalysis
package morphology
package model

import arabic.model.ArabicLetterType.*
import arabic.model.{ ArabicSupportEnum, ArabicWord }

import java.lang.Enum

enum NamedTag(override val word: ArabicWord) extends Enum[NamedTag] with ArabicSupportEnum {

  case NAME_OF_ALLAH
      extends NamedTag(
        ArabicWord(
          LAM,
          FA,
          DTHA,
          SPACE,
          ALIF,
          LAM,
          JEEM,
          LAM,
          ALIF,
          LAM,
          TA_MARBUTA
        )
      )

  override def code: String = name
}
