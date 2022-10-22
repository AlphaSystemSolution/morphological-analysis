package com.alphasystem
package arabic
package morphologicalanalysis
package morphology
package model

import arabic.model.ArabicLetterType.*
import arabic.model.{ ArabicSupportEnum, ArabicWord }

import java.lang.Enum

enum NamedTag(override val word: ArabicWord) extends Enum[NamedTag] with ArabicSupportEnum {

  case NameOfAllah
      extends NamedTag(
        ArabicWord(
          Lam,
          Fa,
          Dtha,
          Space,
          Alif,
          Lam,
          Jeem,
          Lam,
          Alif,
          Lam,
          TaMarbuta
        )
      )

  override def code: String = name
}
