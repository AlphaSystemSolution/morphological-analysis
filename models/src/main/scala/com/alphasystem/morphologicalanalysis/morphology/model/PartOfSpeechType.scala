package com.alphasystem.morphologicalanalysis.morphology.model

import com.alphasystem.arabic.model.ArabicLetterType.*
import com.alphasystem.arabic.model.{ ArabicSupportEnum, ArabicWord }

import java.lang.Enum

sealed trait PartOfSpeechType extends ArabicSupportEnum

enum NounPartOfSpeechType(
  override val code: String,
  override val label: ArabicWord)
    extends Enum[NounPartOfSpeechType]
    with PartOfSpeechType {

  case NOUN
      extends NounPartOfSpeechType(
        "Noun",
        ArabicWord(ALIF_HAMZA_BELOW, SEEN, MEEM)
      )

  case PROPER_NOUN
      extends NounPartOfSpeechType(
        "Proper Noun",
        ArabicWord(ALIF_HAMZA_BELOW, SEEN, MEEM, SPACE, AIN, LAM, MEEM)
      )

  case TIME_ADVERB
      extends NounPartOfSpeechType(
        "Time adverb",
        ArabicWord(DTHA, RA, FA, SPACE, ZAIN, MEEM, ALIF, NOON)
      )

  case LOCATION_ADVERB
      extends NounPartOfSpeechType(
        "Location adverb",
        ArabicWord(DTHA, RA, FA, SPACE, MEEM, KAF, ALIF, NOON)
      )
}

enum ProNounPartOfSpeechType(
  override val code: String,
  override val label: ArabicWord)
    extends Enum[ProNounPartOfSpeechType]
    with PartOfSpeechType {

  case PRONOUN
      extends ProNounPartOfSpeechType(
        "Pronoun",
        ArabicWord(DDAD, MEEM, YA, RA)
      )

  case RELATIVE_PRONOUN
      extends ProNounPartOfSpeechType(
        "Relative pronoun",
        ArabicWord(
          ALIF_HAMZA_BELOW,
          SEEN,
          MEEM,
          SPACE,
          MEEM,
          WAW,
          SAD,
          WAW,
          LAM
        )
      )

  case DEMONSTRATIVE_PRONOUN
      extends ProNounPartOfSpeechType(
        "Demonstrative pronoun",
        ArabicWord(
          ALIF_HAMZA_BELOW,
          SEEN,
          MEEM,
          SPACE,
          ALIF_HAMZA_BELOW,
          SHEEN,
          ALIF,
          RA,
          TA_MARBUTA
        )
      )

}

enum ParticlePartOfSpeechType(
  override val code: String,
  override val label: ArabicWord)
    extends Enum[ParticlePartOfSpeechType]
    with PartOfSpeechType {

  case GENITIVE_PARTICLE
      extends ParticlePartOfSpeechType(
        "Genitive particle",
        ArabicWord(HHA, RA, FA, SPACE, JEEM, RA)
      )

  case ACCUSATIVE_PARTICLE
      extends ParticlePartOfSpeechType(
        "Accusative particle",
        ArabicWord(HHA, RA, FA, SPACE, NOON, SAD, BA)
      )

  case DEFINITE_ARTICLE
      extends ParticlePartOfSpeechType(
        "Definite article",
        ArabicWord(LAM, ALIF, MEEM, SPACE, ALIF, LAM, TA, AIN, RA, YA, FA)
      )

  case EMPHATIC_PARTICLE
      extends ParticlePartOfSpeechType(
        "Emphatic particle",
        ArabicWord(
          ALIF,
          LAM,
          LAM,
          ALIF,
          MEEM,
          SPACE,
          LAM,
          ALIF,
          MEEM,
          SPACE,
          ALIF,
          LAM,
          TA,
          WAW,
          KAF,
          YA,
          DAL
        )
      )

  case IMPERATIVE_PARTICLE
      extends ParticlePartOfSpeechType(
        "Imperative particle",
        ArabicWord(LAM, ALIF, MEEM, SPACE, ALIF, LAM, ALIF, MEEM, RA)
      )

  case SUBJUNCTIVE_PARTICLE
      extends ParticlePartOfSpeechType(
        "Subjunctive particle",
        ArabicWord(
          HHA,
          RA,
          FA,
          SPACE,
          ALIF,
          LAM,
          NOON,
          ALIF,
          SAD,
          BA,
          TA_MARBUTA
        )
      )
  case JUSSIVE_PARTICLE
      extends ParticlePartOfSpeechType(
        "Jussive particle",
        ArabicWord(
          HHA,
          RA,
          FA,
          SPACE,
          ALIF,
          LAM,
          JEEM,
          ALIF,
          ZAIN,
          MEEM,
          TA_MARBUTA
        )
      )
  case PROHIBITION_PARTICLE
      extends ParticlePartOfSpeechType(
        "Prohibition particle",
        ArabicWord(HHA, RA, FA, SPACE, NOON, HA, YA)
      )
  case CONJUNCTION_PARTICLE_WAW
      extends ParticlePartOfSpeechType(
        "Conjunction particle",
        ArabicWord(
          ALIF,
          LAM,
          WAW,
          ALIF,
          WAW,
          SPACE,
          AIN,
          ALIF,
          TTA,
          FA,
          TA_MARBUTA
        )
      )
  case RESUMPTION_PARTICLE_WAW
      extends ParticlePartOfSpeechType(
        "Resumption particle",
        ArabicWord(
          ALIF,
          LAM,
          WAW,
          ALIF,
          WAW,
          SPACE,
          ALIF_HAMZA_BELOW,
          SEEN,
          TA,
          YA_HAMZA_ABOVE,
          NOON,
          ALIF,
          FA,
          YA,
          TA_MARBUTA
        )
      )

  case CONJUNCTION_PARTICLE_FA
      extends ParticlePartOfSpeechType(
        "Conjunction particle",
        ArabicWord(
          ALIF,
          LAM,
          FA,
          ALIF,
          HAMZA,
          SPACE,
          AIN,
          ALIF,
          TTA,
          FA,
          TA_MARBUTA
        )
      )

  case RESUMPTION_PARTICLE_FA
      extends ParticlePartOfSpeechType(
        "Resumption particle",
        ArabicWord(
          ALIF,
          LAM,
          FA,
          ALIF,
          HAMZA,
          SPACE,
          ALIF_HAMZA_BELOW,
          SEEN,
          TA,
          YA_HAMZA_ABOVE,
          NOON,
          ALIF,
          FA,
          YA,
          TA_MARBUTA
        )
      )

  case PARTICLE_OF_CONJUNCTION
      extends ParticlePartOfSpeechType(
        "Conjunction particle",
        ArabicWord(HHA, RA, FA, SPACE, AIN, TTA, FA)
      )

  case PARTICLE_OF_PURPOSE
      extends ParticlePartOfSpeechType(
        "Particle of purpose",
        ArabicWord(
          ALIF,
          LAM,
          LAM,
          ALIF,
          MEEM,
          SPACE,
          LAM,
          ALIF,
          MEEM,
          SPACE,
          ALIF,
          LAM,
          TA,
          AIN,
          LAM,
          YA,
          LAM
        )
      )

  case VOCATIVE_PARTICLE
      extends ParticlePartOfSpeechType(
        "Vocative particle",
        ArabicWord(HHA, RA, FA, SPACE, NOON, DAL, ALIF, HAMZA)
      )

  case SUBORDINATING_CONJUNCTION
      extends ParticlePartOfSpeechType(
        "Subordinating conjunction",
        ArabicWord(HHA, RA, FA, SPACE, MEEM, SAD, DAL, RA, YA)
      )

  case AMENDMENT_PARTICLE
      extends ParticlePartOfSpeechType(
        "Amendment particle",
        ArabicWord(HHA, RA, FA, SPACE, ALIF, SEEN, TA, DAL, RA, ALIF, KAF)
      )

  case ANSWER_PARTICLE
      extends ParticlePartOfSpeechType(
        "Answer particle",
        ArabicWord(HHA, RA, FA, SPACE, JEEM, WAW, ALIF, BA)
      )

  case AVERSION_PARTICLE
      extends ParticlePartOfSpeechType(
        "Aversion particle",
        ArabicWord(HHA, RA, FA, SPACE, RA, DAL, AIN)
      )

  case PARTICLE_OF_CAUSE
      extends ParticlePartOfSpeechType(
        "Particle of cause",
        ArabicWord(HHA, RA, FA, SPACE, SEEN, BA, YA, TA_MARBUTA)
      )

  case PARTICLE_OF_CERTAINTY
      extends ParticlePartOfSpeechType(
        "Particle of certainty",
        ArabicWord(HHA, RA, FA, SPACE, TA, HHA, QAF, YA, QAF)
      )

  case CIRCUMSTANTIAL_PARTICLE
      extends ParticlePartOfSpeechType(
        "Circumstantial particle",
        ArabicWord(
          ALIF,
          LAM,
          WAW,
          ALIF,
          WAW,
          SPACE,
          HHA,
          ALIF,
          LAM,
          YA,
          TA_MARBUTA
        )
      )

  case COMITATIVE_PARTICLE
      extends ParticlePartOfSpeechType(
        "Comitative particle",
        ArabicWord(WAW, ALIF, WAW, SPACE, ALIF, LAM, MEEM, AIN, YA, TA_MARBUTA)
      )

  case CONDITIONAL_PARTICLE
      extends ParticlePartOfSpeechType(
        "Conditional particle",
        ArabicWord(HHA, RA, FA, SPACE, SHEEN, RA, TTA)
      )

  case EQUALIZATION_PARTICLE
      extends ParticlePartOfSpeechType(
        "Equalization particle",
        ArabicWord(HHA, RA, FA, SPACE, TA, SEEN, WAW, TA_MARBUTA)
      )

  case EXHORTATION_PARTICLE
      extends ParticlePartOfSpeechType(
        "Exhortation particle",
        ArabicWord(HHA, RA, FA, SPACE, TA, HHA, DDAD, YA, DDAD)
      )

  case EXPLANATION_PARTICLE
      extends ParticlePartOfSpeechType(
        "Explanation particle",
        ArabicWord(HHA, RA, FA, SPACE, TA, FA, SAD, YA, LAM)
      )

  case EXCEPTIVE_PARTICLE
      extends ParticlePartOfSpeechType(
        "Exceptive particle",
        ArabicWord(
          ALIF,
          DAL,
          ALIF,
          TA_MARBUTA,
          SPACE,
          ALIF,
          SEEN,
          TA,
          THA,
          NOON,
          ALIF,
          HAMZA
        )
      )

  case FUTURE_PARTICLE
      extends ParticlePartOfSpeechType(
        "Future particle",
        ArabicWord(HHA, RA, FA, SPACE, ALIF, SEEN, TA, QAF, BA, ALIF, LAM)
      )

  case INCEPTIVE_PARTICLE
      extends ParticlePartOfSpeechType(
        "Inceptive particle",
        ArabicWord(HHA, RA, FA, SPACE, ALIF, BA, TA, DAL, ALIF, HAMZA)
      )

  case PARTICLE_OF_INTERPRETATION
      extends ParticlePartOfSpeechType(
        "Particle of interpretation",
        ArabicWord(HHA, RA, FA, SPACE, TA, FA, SEEN, YA, RA)
      )

  case INTERROGATIVE_PARTICLE
      extends ParticlePartOfSpeechType(
        "Interrogative particle",
        ArabicWord(HHA, RA, FA, SPACE, ALIF, SEEN, TA, FA, HA, ALIF, MEEM)
      )

  case NEGATIVE_PARTICLE
      extends ParticlePartOfSpeechType(
        "Negative particle",
        ArabicWord(HHA, RA, FA, SPACE, NOON, FA, YA)
      )

  case PREVENTIVE_PARTICLE
      extends ParticlePartOfSpeechType(
        "Preventive particle",
        ArabicWord(HHA, RA, FA, SPACE, KAF, ALIF, FA)
      )

  case RESTRICTION_PARTICLE
      extends ParticlePartOfSpeechType(
        "Restriction particle",
        ArabicWord(ALIF_HAMZA_ABOVE, DAL, ALIF, TA_MARBUTA, SPACE, HHA, SAD, RA)
      )
  case RETRACTION_PARTICLE
      extends ParticlePartOfSpeechType(
        "Retraction particle",
        ArabicWord(HHA, RA, FA, SPACE, ALIF, DDAD, RA, ALIF, BA)
      )

  case RESULT_PARTICLE
      extends ParticlePartOfSpeechType(
        "Result particle",
        ArabicWord(
          HHA,
          RA,
          FA,
          SPACE,
          WAW,
          ALIF,
          QAF,
          AIN,
          SPACE,
          FA,
          YA,
          SPACE,
          JEEM,
          WAW,
          ALIF,
          BA,
          SPACE,
          ALIF,
          LAM,
          SHEEN,
          RA,
          TTA
        )
      )

  case SUPPLEMENTAL_PARTICLE
      extends ParticlePartOfSpeechType(
        "Supplemental particle",
        ArabicWord(HHA, RA, FA, SPACE, ZAIN, ALIF, YA_HAMZA_ABOVE, DAL)
      )

  case SURPRISE_PARTICLE
      extends ParticlePartOfSpeechType(
        "Surprise particle",
        ArabicWord(HHA, RA, FA, SPACE, FA, JEEM, ALIF, HAMZA, TA_MARBUTA)
      )

  case QURANIC_INITIAL
      extends ParticlePartOfSpeechType(
        "Quranic initial",
        ArabicWord(HHA, RA, FA, SPACE, MEEM, QAF, TTA, AIN, TTA, TA_MARBUTA)
      )

  case QURANIC_PUNCTUATION
      extends ParticlePartOfSpeechType("Quranic Punctuation", ArabicWord(QAF));
}

enum VerbPartOfSpeechType(
  override val code: String,
  override val label: ArabicWord)
    extends Enum[VerbPartOfSpeechType]
    with PartOfSpeechType {

  case VERB extends VerbPartOfSpeechType("Verb", ArabicWord(FA, AIN, LAM))
}
