package com.alphasystem.morphologicalanalysis.morphology.model

import com.alphasystem.arabic.model.ArabicLetterType.*
import com.alphasystem.arabic.model.{ ArabicSupportEnum, ArabicWord }

enum NamedTag(override val label: ArabicWord) extends ArabicSupportEnum {

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
