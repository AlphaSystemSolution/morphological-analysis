package com.alphasystem
package arabic
package morphologicalanalysis
package morphology
package model

import arabic.model.ArabicLetterType.*
import arabic.model.{ ArabicLetters, ArabicSupportEnum, ArabicWord }

import java.lang.Enum
enum RelationshipType(override val word: ArabicWord) extends Enum[RelationshipType] with ArabicSupportEnum {

  case NONE extends RelationshipType(ArabicLetters.WORD_SPACE)

  case MUDAF extends RelationshipType(ArabicWord(MEEM, DDAD, ALIF, FA))

  case MUDAF_ILAIH
      extends RelationshipType(
        ArabicWord(MEEM, DDAD, ALIF, FA, SPACE, ALIF_HAMZA_BELOW, LAM, YA, HA)
      )

  case IDAFAH
      extends RelationshipType(
        ArabicWord(ALIF_HAMZA_BELOW, DDAD, ALIF, FA, TA_MARBUTA)
      )

  case DOUBLE_IDAFAH
      extends RelationshipType(
        ArabicWord(ALIF_HAMZA_BELOW, DDAD, ALIF, FA, TA, ALIF, NOON)
      )

  case MOWSOOF extends RelationshipType(ArabicWord(MEEM, WAW, SAD, WAW, FA))

  case SIFAH extends RelationshipType(ArabicWord(SAD, FA, TA_MARBUTA))

  case POINTING_WORD
      extends RelationshipType(
        ArabicWord(MEEM, SHEEN, ALIF, RA, SPACE, ALIF_HAMZA_BELOW, LAM, YA, HA)
      )

  case JAR_MAJROOR
      extends RelationshipType(
        ArabicWord(JEEM, ALIF, RA, SPACE, WAW, SPACE, MEEM, JEEM, RA, WAW, RA)
      )

  case MAJROOR extends RelationshipType(ArabicWord(MEEM, JEEM, RA, WAW, RA))

  case SILLAH extends RelationshipType(ArabicWord(SAD, LAM, TA_MARBUTA))

  case MUBTADA extends RelationshipType(ArabicWord(MEEM, BA, TA, DAL, ALIF_HAMZA_ABOVE))

  case KHABAR extends RelationshipType(ArabicWord(KHA, BA, RA))

  case MUTALIQ extends RelationshipType(ArabicWord(MEEM, TA, AIN, LAM, QAF))

  case MUTALIQ_TO_KHABAR
      extends RelationshipType(
        ArabicWord(MEEM, TA, AIN, LAM, QAF, SPACE, BA, LAM, KHA, BA, RA)
      )

  case MAATOOF extends RelationshipType(ArabicWord(MEEM, AIN, TTA, WAW, FA))

  case ISM
      extends RelationshipType(
        ArabicWord(ALIF_HAMZA_BELOW, SEEN, MEEM)
      )

  case FORBIDDEN
      extends RelationshipType(
        ArabicWord(FA, AIN, LAM, SPACE, NOON, HA, YA)
      )

  case FAIIL extends RelationshipType(ArabicWord(FA, ALIF, AIN, LAM))

  case ALTERNATE_DOER
      extends RelationshipType(
        ArabicWord(NOON, ALIF, YA_HAMZA_ABOVE, BA, SPACE, FA, ALIF, AIN, LAM)
      )

  case MAFOOL extends RelationshipType(ArabicWord(MEEM, FA, AIN, WAW, LAM))

  case MAFOOL_BHI
      extends RelationshipType(
        ArabicWord(MEEM, FA, AIN, WAW, LAM, SPACE, BA, HA)
      )

  case MAFOOL_FIHI
      extends RelationshipType(
        ArabicWord(MEEM, FA, AIN, WAW, LAM, SPACE, FA, YA, HA)
      )

  case MAFOOL_LAHU
      extends RelationshipType(
        ArabicWord(MEEM, FA, AIN, WAW, LAM, SPACE, LAM, HA)
      )

  case MAFOOL_HALL
      extends RelationshipType(
        ArabicWord(MEEM, FA, AIN, WAW, LAM, SPACE, HHA, ALIF, LAM)
      )

  case MAFOOL_MUTLIQ
      extends RelationshipType(
        ArabicWord(MEEM, FA, AIN, WAW, LAM, SPACE, MEEM, TTA, LAM, QAF)
      )

  case TAWKEED extends RelationshipType(ArabicWord(TA, WAW, KAF, YA, DAL))

  case MUNADI extends RelationshipType(ArabicWord(MEEM, NOON, ALIF, DAL, YA))

  case NOUN_BASED_SENTENCE
      extends RelationshipType(
        ArabicWord(
          JEEM,
          MEEM,
          LAM,
          TA_MARBUTA,
          SPACE,
          ALIF_HAMZA_BELOW,
          SEEN,
          MEEM,
          YA,
          TA_MARBUTA
        )
      )

  case VERB_BASED_SENTENCE
      extends RelationshipType(
        ArabicWord(
          JEEM,
          MEEM,
          LAM,
          TA_MARBUTA,
          SPACE,
          FA,
          AIN,
          LAM,
          YA,
          TA_MARBUTA
        )
      )

  case HAL_SENTENCE
      extends RelationshipType(
        ArabicWord(
          JEEM,
          MEEM,
          LAM,
          TA_MARBUTA,
          SPACE,
          HHA,
          ALIF,
          LAM,
          YA,
          TA_MARBUTA
        )
      )

  case QUOTATION_SENTENCE
      extends RelationshipType(
        ArabicWord(MEEM, QAF, WAW, LAM, SPACE, ALIF, LAM, QAF, WAW, LAM)
      )

  case CONDITION extends RelationshipType(ArabicWord(SHEEN, RA, TTA))

  case ANSWER_TO_CONDITION
      extends RelationshipType(
        ArabicWord(JEEM, WAW, ALIF, BA, SPACE, ALIF, LAM, SHEEN, RA, TTA)
      )

  case _KAF extends RelationshipType(ArabicWord(KAF, ALIF, FA))

  override val code: String = name
}
