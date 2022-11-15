package com.alphasystem
package arabic
package morphologicalanalysis
package morphology
package model

import arabic.model.ArabicLetterType.*
import arabic.model.{ ArabicSupportEnum, ArabicWord }

import java.lang.Enum

sealed trait PartOfSpeechType extends ArabicSupportEnum {
  val colorCode: String
}
enum NounPartOfSpeechType(
  override val code: String,
  override val word: ArabicWord,
  override val colorCode: String = "#000000")
    extends Enum[NounPartOfSpeechType]
    with PartOfSpeechType {

  case Noun
      extends NounPartOfSpeechType(
        "Noun",
        ArabicWord(AlifHamzaBelow, Seen, Meem),
        "#000080"
      )

  case ProperNoun
      extends NounPartOfSpeechType(
        "Proper Noun",
        ArabicWord(AlifHamzaBelow, Seen, Meem, Space, Ain, Lam, Meem),
        "#000080"
      )

  case TimeAdverb
      extends NounPartOfSpeechType(
        "Time adverb",
        ArabicWord(Dtha, Ra, Fa, Space, Zain, Meem, Alif, Noon)
      )

  case LocationAdverb
      extends NounPartOfSpeechType(
        "Location adverb",
        ArabicWord(Dtha, Ra, Fa, Space, Meem, Kaf, Alif, Noon)
      )
}

enum ProNounPartOfSpeechType(
  override val code: String,
  override val word: ArabicWord,
  override val colorCode: String)
    extends Enum[ProNounPartOfSpeechType]
    with PartOfSpeechType {

  case Pronoun
      extends ProNounPartOfSpeechType(
        "Pronoun",
        ArabicWord(Ddad, Meem, Ya, Ra),
        "#8C001A"
      )

  case RelativePronoun
      extends ProNounPartOfSpeechType(
        "Relative pronoun",
        ArabicWord(
          AlifHamzaBelow,
          Seen,
          Meem,
          Space,
          Meem,
          Waw,
          Sad,
          Waw,
          Lam
        ),
        "#3EA055"
      )

  case DemonstrativePronoun
      extends ProNounPartOfSpeechType(
        "Demonstrative pronoun",
        ArabicWord(
          AlifHamzaBelow,
          Seen,
          Meem,
          Space,
          AlifHamzaBelow,
          Sheen,
          Alif,
          Ra,
          TaMarbuta
        ),
        "#008080"
      )

}

enum ParticlePartOfSpeechType(
  override val code: String,
  override val word: ArabicWord,
  override val colorCode: String = "#000000")
    extends Enum[ParticlePartOfSpeechType]
    with PartOfSpeechType {

  case GenitiveParticle
      extends ParticlePartOfSpeechType(
        "Genitive particle",
        ArabicWord(Hha, Ra, Fa, Space, Jeem, Ra),
        "#800000"
      )

  case AccusativeParticle
      extends ParticlePartOfSpeechType(
        "Accusative particle",
        ArabicWord(Hha, Ra, Fa, Space, Noon, Sad, Ba),
        "#8B0807"
      )

  case DefiniteArticle
      extends ParticlePartOfSpeechType(
        "Definite article",
        ArabicWord(Lam, Alif, Meem, Space, Alif, Lam, Ta, Ain, Ra, Ya, Fa)
      )

  case EmphaticParticle
      extends ParticlePartOfSpeechType(
        "Emphatic particle",
        ArabicWord(
          Alif,
          Lam,
          Lam,
          Alif,
          Meem,
          Space,
          Lam,
          Alif,
          Meem,
          Space,
          Alif,
          Lam,
          Ta,
          Waw,
          Kaf,
          Ya,
          Dal
        )
      )

  case ImperativeParticle
      extends ParticlePartOfSpeechType(
        "Imperative particle",
        ArabicWord(Lam, Alif, Meem, Space, Alif, Lam, Alif, Meem, Ra)
      )

  case SubjunctiveParticle
      extends ParticlePartOfSpeechType(
        "Subjunctive particle",
        ArabicWord(
          Hha,
          Ra,
          Fa,
          Space,
          Alif,
          Lam,
          Noon,
          Alif,
          Sad,
          Ba,
          TaMarbuta
        )
      )

  case JussiveParticle
      extends ParticlePartOfSpeechType(
        "Jussive particle",
        ArabicWord(
          Hha,
          Ra,
          Fa,
          Space,
          Alif,
          Lam,
          Jeem,
          Alif,
          Zain,
          Meem,
          TaMarbuta
        )
      )

  case ProhibitionParticle
      extends ParticlePartOfSpeechType(
        "Prohibition particle",
        ArabicWord(Hha, Ra, Fa, Space, Noon, Ha, Ya)
      )

  case ConjunctionParticleWaw
      extends ParticlePartOfSpeechType(
        "Conjunction particle",
        ArabicWord(
          Alif,
          Lam,
          Waw,
          Alif,
          Waw,
          Space,
          Ain,
          Alif,
          Tta,
          Fa,
          TaMarbuta
        )
      )

  case ResumptionParticleWaw
      extends ParticlePartOfSpeechType(
        "Resumption particle",
        ArabicWord(
          Alif,
          Lam,
          Waw,
          Alif,
          Waw,
          Space,
          AlifHamzaBelow,
          Seen,
          Ta,
          YaHamzaAbove,
          Noon,
          Alif,
          Fa,
          Ya,
          TaMarbuta
        )
      )

  case ConjunctionParticleFa
      extends ParticlePartOfSpeechType(
        "Conjunction particle",
        ArabicWord(
          Alif,
          Lam,
          Fa,
          Alif,
          Hamza,
          Space,
          Ain,
          Alif,
          Tta,
          Fa,
          TaMarbuta
        )
      )

  case ResumptionParticleFa
      extends ParticlePartOfSpeechType(
        "Resumption particle",
        ArabicWord(
          Alif,
          Lam,
          Fa,
          Alif,
          Hamza,
          Space,
          AlifHamzaBelow,
          Seen,
          Ta,
          YaHamzaAbove,
          Noon,
          Alif,
          Fa,
          Ya,
          TaMarbuta
        )
      )

  case ParticleOfConjunction
      extends ParticlePartOfSpeechType(
        "Conjunction particle",
        ArabicWord(Hha, Ra, Fa, Space, Ain, Tta, Fa)
      )

  case ParticleOfPurpose
      extends ParticlePartOfSpeechType(
        "Particle of purpose",
        ArabicWord(
          Alif,
          Lam,
          Lam,
          Alif,
          Meem,
          Space,
          Lam,
          Alif,
          Meem,
          Space,
          Alif,
          Lam,
          Ta,
          Ain,
          Lam,
          Ya,
          Lam
        )
      )

  case VocativeParticle
      extends ParticlePartOfSpeechType(
        "Vocative particle",
        ArabicWord(Hha, Ra, Fa, Space, Noon, Dal, Alif, Hamza)
      )

  case SubordinatingConjunction
      extends ParticlePartOfSpeechType(
        "Subordinating conjunction",
        ArabicWord(Hha, Ra, Fa, Space, Meem, Sad, Dal, Ra, Ya)
      )

  case AmendmentParticle
      extends ParticlePartOfSpeechType(
        "Amendment particle",
        ArabicWord(Hha, Ra, Fa, Space, Alif, Seen, Ta, Dal, Ra, Alif, Kaf)
      )

  case AnswerParticle
      extends ParticlePartOfSpeechType(
        "Answer particle",
        ArabicWord(Hha, Ra, Fa, Space, Jeem, Waw, Alif, Ba)
      )

  case AversionParticle
      extends ParticlePartOfSpeechType(
        "Aversion particle",
        ArabicWord(Hha, Ra, Fa, Space, Ra, Dal, Ain)
      )

  case ParticleOfCause
      extends ParticlePartOfSpeechType(
        "Particle of cause",
        ArabicWord(Hha, Ra, Fa, Space, Seen, Ba, Ya, TaMarbuta)
      )

  case ParticleOfCertainty
      extends ParticlePartOfSpeechType(
        "Particle of certainty",
        ArabicWord(Hha, Ra, Fa, Space, Ta, Hha, Qaf, Ya, Qaf)
      )

  case CircumstantialParticle
      extends ParticlePartOfSpeechType(
        "Circumstantial particle",
        ArabicWord(
          Alif,
          Lam,
          Waw,
          Alif,
          Waw,
          Space,
          Hha,
          Alif,
          Lam,
          Ya,
          TaMarbuta
        )
      )

  case ComitativeParticle
      extends ParticlePartOfSpeechType(
        "Comitative particle",
        ArabicWord(Waw, Alif, Waw, Space, Alif, Lam, Meem, Ain, Ya, TaMarbuta)
      )

  case ConditionalParticle
      extends ParticlePartOfSpeechType(
        "Conditional particle",
        ArabicWord(Hha, Ra, Fa, Space, Sheen, Ra, Tta)
      )

  case EqualizationParticle
      extends ParticlePartOfSpeechType(
        "Equalization particle",
        ArabicWord(Hha, Ra, Fa, Space, Ta, Seen, Waw, TaMarbuta)
      )

  case ExhortationParticle
      extends ParticlePartOfSpeechType(
        "Exhortation particle",
        ArabicWord(Hha, Ra, Fa, Space, Ta, Hha, Ddad, Ya, Ddad)
      )

  case ExplanationParticle
      extends ParticlePartOfSpeechType(
        "Explanation particle",
        ArabicWord(Hha, Ra, Fa, Space, Ta, Fa, Sad, Ya, Lam)
      )

  case ExceptiveParticle
      extends ParticlePartOfSpeechType(
        "Exceptive particle",
        ArabicWord(
          Alif,
          Dal,
          Alif,
          TaMarbuta,
          Space,
          Alif,
          Seen,
          Ta,
          Tha,
          Noon,
          Alif,
          Hamza
        )
      )

  case FutureParticle
      extends ParticlePartOfSpeechType(
        "Future particle",
        ArabicWord(Hha, Ra, Fa, Space, Alif, Seen, Ta, Qaf, Ba, Alif, Lam)
      )

  case InceptiveParticle
      extends ParticlePartOfSpeechType(
        "Inceptive particle",
        ArabicWord(Hha, Ra, Fa, Space, Alif, Ba, Ta, Dal, Alif, Hamza)
      )

  case ParticleOfInterpretation
      extends ParticlePartOfSpeechType(
        "Particle of interpretation",
        ArabicWord(Hha, Ra, Fa, Space, Ta, Fa, Seen, Ya, Ra)
      )

  case InterrogativeParticle
      extends ParticlePartOfSpeechType(
        "Interrogative particle",
        ArabicWord(Hha, Ra, Fa, Space, Alif, Seen, Ta, Fa, Ha, Alif, Meem)
      )

  case NegativeParticle
      extends ParticlePartOfSpeechType(
        "Negative particle",
        ArabicWord(Hha, Ra, Fa, Space, Noon, Fa, Ya)
      )

  case PreventiveParticle
      extends ParticlePartOfSpeechType(
        "Preventive particle",
        ArabicWord(Hha, Ra, Fa, Space, Kaf, Alif, Fa)
      )

  case RestrictionParticle
      extends ParticlePartOfSpeechType(
        "Restriction particle",
        ArabicWord(AlifHamzaAbove, Dal, Alif, TaMarbuta, Space, Hha, Sad, Ra)
      )

  case RetractionParticle
      extends ParticlePartOfSpeechType(
        "Retraction particle",
        ArabicWord(Hha, Ra, Fa, Space, Alif, Ddad, Ra, Alif, Ba)
      )

  case ResultParticle
      extends ParticlePartOfSpeechType(
        "Result particle",
        ArabicWord(
          Hha,
          Ra,
          Fa,
          Space,
          Waw,
          Alif,
          Qaf,
          Ain,
          Space,
          Fa,
          Ya,
          Space,
          Jeem,
          Waw,
          Alif,
          Ba,
          Space,
          Alif,
          Lam,
          Sheen,
          Ra,
          Tta
        )
      )

  case SupplementalParticle
      extends ParticlePartOfSpeechType(
        "Supplemental particle",
        ArabicWord(Hha, Ra, Fa, Space, Zain, Alif, YaHamzaAbove, Dal)
      )

  case SurpriseParticle
      extends ParticlePartOfSpeechType(
        "Surprise particle",
        ArabicWord(Hha, Ra, Fa, Space, Fa, Jeem, Alif, Hamza, TaMarbuta)
      )

  case QuranicInitial
      extends ParticlePartOfSpeechType(
        "Quranic initial",
        ArabicWord(Hha, Ra, Fa, Space, Meem, Qaf, Tta, Ain, Tta, TaMarbuta)
      )

  case QuranicPunctuation extends ParticlePartOfSpeechType("Quranic Punctuation", ArabicWord(Qaf));
}

enum VerbPartOfSpeechType(
  override val code: String,
  override val colorCode: String,
  override val word: ArabicWord)
    extends Enum[VerbPartOfSpeechType]
    with PartOfSpeechType {

  case Verb extends VerbPartOfSpeechType("Verb", "#4AA02C", ArabicWord(Fa, Ain, Lam))
}
