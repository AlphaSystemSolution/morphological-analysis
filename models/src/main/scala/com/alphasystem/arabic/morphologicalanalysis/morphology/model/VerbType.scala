package com.alphasystem
package arabic
package morphologicalanalysis
package morphology
package model

import arabic.model.ArabicLetterType.*
import arabic.model.{ ArabicSupportEnum, ArabicWord }

import java.lang.Enum

enum VerbType(override val code: String, override val word: ArabicWord) extends Enum[VerbType] with ArabicSupportEnum {

  case Perfect extends VerbType("Perfect", ArabicWord(Meem, Alif, Ddad))

  case Imperfect extends VerbType("Imperfect", ArabicWord(Meem, Ddad, Alif, Ra, Ain))

  case PerfectPassive
      extends VerbType(
        "Perfect passive",
        ArabicWord(
          Meem,
          Alif,
          Ddad,
          Space,
          Meem,
          Ba,
          Noon,
          Ya,
          Space,
          Alif,
          Lam,
          Lam,
          Meem,
          Jeem,
          Ha,
          Waw,
          Lam
        )
      )

  case ImperfectPassive
      extends VerbType(
        "Imperfect passive",
        ArabicWord(
          Meem,
          Ddad,
          Alif,
          Ra,
          Ain,
          Space,
          Meem,
          Ba,
          Noon,
          Ya,
          Space,
          Alif,
          Lam,
          Lam,
          Meem,
          Jeem,
          Ha,
          Waw,
          Lam
        )
      )

  case Command extends VerbType("Imperative", ArabicWord(AlifHamzaAbove, Meem, Ra))

  case Forbidden extends VerbType("Forbidden", ArabicWord(Noon, Ha, Ya))
}
