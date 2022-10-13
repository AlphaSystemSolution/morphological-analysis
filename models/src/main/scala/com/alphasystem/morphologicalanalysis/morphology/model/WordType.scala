package com.alphasystem.morphologicalanalysis.morphology.model

import com.alphasystem.arabic.model.ArabicLetterType.*
import com.alphasystem.arabic.model.{ ArabicSupportEnum, ArabicWord }

enum WordType(override val code: String, override val word: ArabicWord)
    extends Enum[VerbType]
    with ArabicSupportEnum {

  case NOUN extends WordType("Noun", ArabicWord(ALIF_HAMZA_BELOW, SEEN, MEEM))

  case PRO_NOUN extends WordType("Pronoun", ArabicWord(DDAD, MEEM, YA, RA))

  case VERB extends WordType("Verb", ArabicWord(FA, AIN, LAM))

  case PARTICLE extends WordType("Particle", ArabicWord(HHA, RA, FA))
}

/*
enum WordType[
  T <: PartOfSpeechType
](override val code: String, override val label: ArabicWord,
  val values: Array[T]
) extends ArabicSupportEnum {

  case NOUN
      extends WordType(
        "Noun",
        ArabicWord(ALIF_HAMZA_BELOW, SEEN, MEEM),
        NounPartOfSpeechType.values
      )

  case PRO_NOUN
      extends WordType(
        "Pronoun",
        ArabicWord(DDAD, MEEM, YA, RA),
        ProNounPartOfSpeechType.values
      )

  case VERB
      extends WordType(
        "Verb",
        ArabicWord(FA, AIN, LAM),
        VerbPartOfSpeechType.values
      )

  case PARTICLE
      extends WordType(
        "Particle",
        ArabicWord(HHA, RA, FA),
        ParticlePartOfSpeechType.values
      )
}
 */
