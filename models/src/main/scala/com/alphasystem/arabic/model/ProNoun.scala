package com.alphasystem.arabic.model

import com.alphasystem.arabic.model.ArabicLetterType.*
import com.alphasystem.arabic.model.DiacriticType.*

enum ProNoun(val label: ArabicWord) {

  case THIRD_PERSON_MASCULINE_SINGULAR
      extends ProNoun(
        ArabicWord(
          ArabicLetter(HA, DAMMA),
          ArabicLetter(WAW, FATHA)
        )
      )

  case THIRD_PERSON_MASCULINE_DUAL
      extends ProNoun(
        ArabicWord(
          ArabicLetter(HA, DAMMA),
          ArabicLetter(MEEM, FATHA),
          ArabicLetter(ALIF)
        )
      )

  case THIRD_PERSON_MASCULINE_PLURAL
      extends ProNoun(
        ArabicWord(
          ArabicLetter(HA, DAMMA),
          ArabicLetter(MEEM, SUKUN)
        )
      )

  case THIRD_PERSON_FEMININE_SINGULAR
      extends ProNoun(
        ArabicWord(
          ArabicLetter(HA, KASRA),
          ArabicLetter(YA, FATHA)
        )
      )

  case THIRD_PERSON_FEMININE_DUAL
      extends ProNoun(
        ArabicWord(
          ArabicLetter(HA, DAMMA),
          ArabicLetter(MEEM, FATHA),
          ArabicLetter(ALIF)
        )
      )

  case THIRD_PERSON_FEMININE_PLURAL
      extends ProNoun(
        ArabicWord(
          ArabicLetter(HA, DAMMA),
          ArabicLetter(NOON, SHADDA, FATHA)
        )
      )

  case SECOND_PERSON_MASCULINE_SINGULAR
      extends ProNoun(
        ArabicWord(
          ArabicLetter(ALIF_HAMZA_ABOVE, FATHA),
          ArabicLetter(NOON, SUKUN),
          ArabicLetter(TA, FATHA)
        )
      )

  case SECOND_PERSON_MASCULINE_DUAL
      extends ProNoun(
        ArabicWord(
          ArabicLetter(ALIF_HAMZA_ABOVE, FATHA),
          ArabicLetter(NOON, SUKUN),
          ArabicLetter(TA, DAMMA),
          ArabicLetter(MEEM, FATHA),
          ArabicLetter(ALIF)
        )
      )

  case SECOND_PERSON_MASCULINE_PLURAL
      extends ProNoun(
        ArabicWord(
          ArabicLetter(ALIF_HAMZA_ABOVE, FATHA),
          ArabicLetter(NOON, SUKUN),
          ArabicLetter(TA, DAMMA),
          ArabicLetter(MEEM, SUKUN)
        )
      )

  case SECOND_PERSON_FEMININE_SINGULAR
      extends ProNoun(
        ArabicWord(
          ArabicLetter(ALIF_HAMZA_ABOVE, FATHA),
          ArabicLetter(NOON, SUKUN),
          ArabicLetter(TA, KASRA)
        )
      )

  case SECOND_PERSON_FEMININE_DUAL
      extends ProNoun(
        ArabicWord(
          ArabicLetter(ALIF_HAMZA_ABOVE, FATHA),
          ArabicLetter(NOON, SUKUN),
          ArabicLetter(TA, DAMMA),
          ArabicLetter(MEEM, FATHA),
          ArabicLetter(ALIF)
        )
      )

  case SECOND_PERSON_FEMININE_PLURAL
      extends ProNoun(
        ArabicWord(
          ArabicLetter(ALIF_HAMZA_ABOVE, FATHA),
          ArabicLetter(NOON, SUKUN),
          ArabicLetter(TA, DAMMA),
          ArabicLetter(NOON, SHADDA, FATHA)
        )
      )

  case FIRST_PERSON_SINGULAR
      extends ProNoun(
        ArabicWord(
          ArabicLetter(ALIF_HAMZA_ABOVE, FATHA),
          ArabicLetter(NOON, FATHA),
          ArabicLetter(ALIF)
        )
      )

  case FIRST_PERSON_PLURAL
      extends ProNoun(
        ArabicWord(
          ArabicLetter(NOON, FATHA),
          ArabicLetter(HHA, SUKUN),
          ArabicLetter(NOON, DAMMA)
        )
      )
}
