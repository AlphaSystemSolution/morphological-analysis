package com.alphasystem.morphologicalanalysis.morphology.model

import com.alphasystem.arabic.model.ArabicLetterType.*
import com.alphasystem.arabic.model.{ ArabicSupport, ArabicWord }

enum SarfTermType(override val label: ArabicWord) extends ArabicSupport {

  case PAST_TENSE
      extends SarfTermType(
        ArabicWord(FA, AIN, LAM, SPACE, MEEM, ALIF, DDAD, YA)
      )

  case PRESENT_TENSE
      extends SarfTermType(
        ArabicWord(FA, AIN, LAM, SPACE, MEEM, DDAD, ALIF, RA, AIN)
      )

  case VERBAL_NOUN extends SarfTermType(ArabicWord(MEEM, SAD, DAL, RA))

  case ACTIVE_PARTICIPLE_MASCULINE
      extends SarfTermType(
        ArabicWord(
          ALIF_HAMZA_BELOW,
          SEEN,
          MEEM,
          SPACE,
          FA,
          ALIF,
          AIN,
          LAM,
          SPACE,
          MEEM,
          THAL,
          KAF,
          RA
        )
      )

  case ACTIVE_PARTICIPLE_FEMININE
      extends SarfTermType(
        ArabicWord(
          ALIF_HAMZA_BELOW,
          SEEN,
          MEEM,
          SPACE,
          FA,
          ALIF,
          AIN,
          LAM,
          SPACE,
          MEEM,
          WAW_HAMZA_ABOVE,
          NOON,
          THA
        )
      )

  case PAST_PASSIVE_TENSE
      extends SarfTermType(
        ArabicWord(
          FA,
          AIN,
          LAM,
          SPACE,
          MEEM,
          ALIF,
          DDAD,
          YA,
          SPACE,
          MEEM,
          BA,
          NOON,
          YA,
          SPACE,
          AIN,
          LAM,
          ALIF_MAKSURA,
          SPACE,
          ALIF,
          LAM,
          MEEM,
          JEEM,
          HA,
          WAW,
          LAM
        )
      )

  case PRESENT_PASSIVE_TENSE
      extends SarfTermType(
        ArabicWord(
          FA,
          AIN,
          LAM,
          SPACE,
          MEEM,
          DDAD,
          ALIF,
          RA,
          AIN,
          SPACE,
          MEEM,
          BA,
          NOON,
          YA,
          SPACE,
          AIN,
          LAM,
          ALIF_MAKSURA,
          SPACE,
          ALIF,
          LAM,
          MEEM,
          JEEM,
          HA,
          WAW,
          LAM
        )
      )

  case PASSIVE_PARTICIPLE_MASCULINE
      extends SarfTermType(
        ArabicWord(
          ALIF_HAMZA_BELOW,
          SEEN,
          MEEM,
          SPACE,
          MEEM,
          FA,
          AIN,
          WAW,
          LAM,
          SPACE,
          MEEM,
          THAL,
          KAF,
          RA
        )
      )

  case PASSIVE_PARTICIPLE_FEMININE
      extends SarfTermType(
        ArabicWord(
          ALIF_HAMZA_BELOW,
          SEEN,
          MEEM,
          SPACE,
          MEEM,
          FA,
          AIN,
          WAW,
          LAM,
          SPACE,
          MEEM,
          WAW_HAMZA_ABOVE,
          NOON,
          THA
        )
      )

  case IMPERATIVE extends SarfTermType(ArabicWord(ALIF_HAMZA_ABOVE, MEEM, RA))

  case FORBIDDING extends SarfTermType(ArabicWord(NOON, HA, YA))

  case NOUN_OF_PLACE_AND_TIME extends SarfTermType(ArabicWord(DTHA, RA, FA))
}
