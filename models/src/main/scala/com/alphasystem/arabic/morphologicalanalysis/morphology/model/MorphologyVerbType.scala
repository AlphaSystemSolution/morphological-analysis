package com.alphasystem
package arabic
package morphologicalanalysis
package morphology
package model

import arabic.model.ArabicLetterType.*
import arabic.model.{ ArabicSupportEnum, ArabicWord }

import java.lang.Enum

enum MorphologyVerbType(override val code: String, override val word: ArabicWord)
    extends Enum[MorphologyVerbType]
    with ArabicSupportEnum {

  case Perfect extends MorphologyVerbType("Perfect", ArabicWord(Meem, Alif, Ddad))

  case Imperfect extends MorphologyVerbType("Imperfect", ArabicWord(Meem, Ddad, Alif, Ra, Ain))

  case PerfectPassive
      extends MorphologyVerbType(
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
      extends MorphologyVerbType(
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

  case Imperative extends MorphologyVerbType("Imperative", ArabicWord(AlifHamzaAbove, Meem, Ra))

  case Forbidden extends MorphologyVerbType("Forbidden", ArabicWord(Noon, Ha, Ya))
}
