package com.alphasystem
package arabic
package morphologicalengine
package conjugation
package transformer

import arabic.model.{ ArabicLetterType, ProNoun }
import arabic.morphologicalanalysis.morphology.model.MorphologyVerbType
import conjugation.model.internal.{ RootWord, VerbGroupType }
import conjugation.forms.{ Form, RootWordSupport, noun, verb }
import conjugation.model.{ ConjugationTuple, NamedTemplate, OutputFormat, VerbConjugationGroup }
import conjugation.rule.RuleEngine
import transformer.noun.*
import transformer.noun.AbstractNounTransformer.PluralType
import transformer.verb.{ ImperativeAndForbiddenTransformer, PastTenseTransformer, PresentTenseTransformer }
import munit.FunSuite

class TransformersSpec extends FunSuite {

  private val defaultRuleProcessor = RuleEngine()

  test("FormICategoryAGroupUTemplate") {
    val processingContext =
      ProcessingContext(
        NamedTemplate.FormICategoryAGroupUTemplate,
        OutputFormat.Unicode,
        ArabicLetterType.Noon,
        ArabicLetterType.Sad,
        ArabicLetterType.Ra
      )
    val obtained = Form
      .fromNamedTemplate(processingContext.namedTemplate)
      .pastTense
      .transform(defaultRuleProcessor, processingContext)

    val expected = VerbConjugationGroup(
      masculineSecondPerson = ConjugationTuple("نَصَرْتَ", "نَصَرْتُمْ", Some("نَصَرْتُمَا")),
      feminineSecondPerson = ConjugationTuple("نَصَرْتِ", "نَصَرْتُنَّ", Some("نَصَرْتُمَا")),
      masculineThirdPerson = Some(ConjugationTuple("نَصَرَ", "نَصَرُوْا", Some("نَصَرَا"))),
      feminineThirdPerson = Some(ConjugationTuple("نَصَرَتْ", "نَصَرْنَ", Some("نَصَرَتَا"))),
      firstPerson = Some(ConjugationTuple("نَصَرْتُ", "نَصَرْنَا", None))
    )

    assertEquals(obtained, expected)
  }

  test("MasculineNominativeTransformer") {
    val expected = ConjugationTuple("مُسْلِمٌ", "مُسْلِمُوْنَ", Some("مُسْلِمَانِ"))

    val processingContext =
      ProcessingContext(
        NamedTemplate.FormIVTemplate,
        OutputFormat.Unicode,
        ArabicLetterType.Seen,
        ArabicLetterType.Lam,
        ArabicLetterType.Meem
      )

    val transformer = MasculineNominativeTransformer()
    validateTransformer(
      transformer,
      noun.FormIV.MasculineActiveParticiple,
      processingContext,
      expected,
      "مُسْلِمٌ"
    )
  }

  test("MasculineAccusativeTransformer") {
    val expected = ConjugationTuple("مُعَلَّمًا", "مُعَلَّمَيْنِ", Some("مُعَلَّمَيْنِ"))

    val processingContext =
      ProcessingContext(
        NamedTemplate.FormIITemplate,
        OutputFormat.Unicode,
        ArabicLetterType.Ain,
        ArabicLetterType.Lam,
        ArabicLetterType.Meem
      )

    val transformer = MasculineAccusativeTransformer()
    validateTransformer(
      transformer,
      noun.FormII.MasculinePassiveParticiple,
      processingContext,
      expected,
      "مُعَلَّمٌ"
    )
  }

  test("MasculineGenitiveTransformer") {
    val expected = ConjugationTuple("مُسْتَغْفِرٍ", "مُسْتَغْفِرِيْنَ", Some("مُسْتَغْفِرِيْنَ"))

    val processingContext =
      ProcessingContext(
        NamedTemplate.FormXTemplate,
        OutputFormat.Unicode,
        ArabicLetterType.Ghain,
        ArabicLetterType.Fa,
        ArabicLetterType.Ra
      )

    val transformer = MasculineGenitiveTransformer()
    validateTransformer(
      transformer,
      noun.FormX.MasculineActiveParticiple,
      processingContext,
      expected,
      "مُسْتَغْفِرٌ"
    )
  }

  test("FeminineNominativeTransformer: from masculine word") {
    val expected = ConjugationTuple("مُقْتَرَبَةٌ", "مُقْتَرَبَاتٌ", Some("مُقْتَرَبَتَانِ"))

    val processingContext =
      ProcessingContext(
        NamedTemplate.FormVIIITemplate,
        OutputFormat.Unicode,
        ArabicLetterType.Qaf,
        ArabicLetterType.Ra,
        ArabicLetterType.Ba
      )

    val transformer = FeminineNominativeTransformer()
    validateTransformer(
      transformer,
      noun.FormVIII.MasculinePassiveParticiple,
      processingContext,
      expected,
      "مُقْتَرَبٌ"
    )
  }

  test("FeminineNominativeTransformer: from feminine word") {
    val expected = ConjugationTuple("مُقْتَرِبَةٌ", "مُقْتَرِبَاتٌ", Some("مُقْتَرِبَتَانِ"))

    val processingContext =
      ProcessingContext(
        NamedTemplate.FormVIIITemplate,
        OutputFormat.Unicode,
        ArabicLetterType.Qaf,
        ArabicLetterType.Ra,
        ArabicLetterType.Ba
      )

    val transformer = FeminineNominativeTransformer()
    validateTransformer(
      transformer,
      noun.FormVIII.FeminineActiveParticiple,
      processingContext,
      expected,
      "مُقْتَرِبَةٌ"
    )
  }

  test("FeminineAccusativeTransformer: from masculine word") {
    val expected = ConjugationTuple("مُجَاهِدَةً", "مُجَاهِدَاتٍ", Some("مُجَاهِدَتَيْنِ"))

    val processingContext =
      ProcessingContext(
        NamedTemplate.FormIIITemplate,
        OutputFormat.Unicode,
        ArabicLetterType.Jeem,
        ArabicLetterType.Ha,
        ArabicLetterType.Dal
      )

    val transformer = FeminineAccusativeTransformer()
    validateTransformer(
      transformer,
      noun.FormIII.MasculineActiveParticiple,
      processingContext,
      expected,
      "مُجَاهِدٌ"
    )
  }

  test("FeminineAccusativeTransformer: from feminine word") {
    val expected = ConjugationTuple("مُجَاهَدَةً", "مُجَاهَدَاتٍ", Some("مُجَاهَدَتَيْنِ"))

    val processingContext =
      ProcessingContext(
        NamedTemplate.FormIIITemplate,
        OutputFormat.Unicode,
        ArabicLetterType.Jeem,
        ArabicLetterType.Ha,
        ArabicLetterType.Dal
      )

    val transformer = FeminineAccusativeTransformer()
    validateTransformer(
      transformer,
      noun.FormIII.FemininePassiveParticiple,
      processingContext,
      expected,
      "مُجَاهَدَةٌ"
    )
  }

  test("FeminineGenitiveTransformer: from masculine word") {
    val expected = ConjugationTuple("مُتَعَلَّمَةٍ", "مُتَعَلَّمَاتٍ", Some("مُتَعَلَّمَتَيْنِ"))

    val processingContext =
      ProcessingContext(
        NamedTemplate.FormVTemplate,
        OutputFormat.Unicode,
        ArabicLetterType.Ain,
        ArabicLetterType.Lam,
        ArabicLetterType.Meem
      )

    val transformer = FeminineGenitiveTransformer()
    validateTransformer(
      transformer,
      noun.FormV.MasculinePassiveParticiple,
      processingContext,
      expected,
      "مُتَعَلَّمٌ"
    )
  }

  test("FeminineGenitiveTransformer: from feminine word") {
    val expected = ConjugationTuple("مُتَعَارَفَةٍ", "مُتَعَارَفَاتٍ", Some("مُتَعَارَفَتَيْنِ"))

    val processingContext =
      ProcessingContext(
        NamedTemplate.FormVITemplate,
        OutputFormat.Unicode,
        ArabicLetterType.Ain,
        ArabicLetterType.Ra,
        ArabicLetterType.Fa
      )

    val transformer = FeminineGenitiveTransformer()
    validateTransformer(
      transformer,
      noun.FormVI.FemininePassiveParticiple,
      processingContext,
      expected,
      "مُتَعَارَفَةٌ"
    )
  }

  test("VerbalNoun: masculine based") {
    val expected = ConjugationTuple("إِسْلَامٌ", "إِسْلَامَاتٌ", Some("إِسْلَامَانِ"))

    val processingContext =
      ProcessingContext(
        NamedTemplate.FormIVTemplate,
        OutputFormat.Unicode,
        ArabicLetterType.Seen,
        ArabicLetterType.Lam,
        ArabicLetterType.Meem
      )

    val transformer = MasculineNominativeTransformer(pluralType = PluralType.Feminine)
    validateTransformer(
      transformer,
      noun.VerbalNoun.FormIV,
      processingContext,
      expected,
      "إِسْلَامًا"
    )
  }

  test("VerbalNoun: feminine based") {
    val expected = ConjugationTuple("مُجَاهِدَةً", "مُجَاهِدَاتٍ", Some("مُجَاهِدَتَيْنِ"))

    val processingContext =
      ProcessingContext(
        NamedTemplate.FormIIITemplate,
        OutputFormat.Unicode,
        ArabicLetterType.Jeem,
        ArabicLetterType.Ha,
        ArabicLetterType.Dal
      )

    val transformer = FeminineAccusativeTransformer()
    validateTransformer(
      transformer,
      noun.VerbalNoun.FormIIIV2,
      processingContext,
      expected,
      "مُجَاهِدَةً"
    )
  }

  test("PastTenseTransformer: ThirdPersonMasculine") {
    val expected = ConjugationTuple("نَصَرَ", "نَصَرُوْا", Some("نَصَرَا"))

    val processingContext =
      ProcessingContext(
        NamedTemplate.FormICategoryAGroupUTemplate,
        OutputFormat.Unicode,
        ArabicLetterType.Noon,
        ArabicLetterType.Sad,
        ArabicLetterType.Ra
      )

    val transformer = PastTenseTransformer(VerbGroupType.ThirdPersonMasculine)
    validateTransformer(
      transformer,
      verb.FormI.PastTenseV1,
      processingContext,
      expected,
      "نَصَرَ"
    )
  }

  test("PastTenseTransformer: ThirdPersonFeminine") {
    val expected = ConjugationTuple("نُصِرَتْ", "نُصِرْنَ", Some("نُصِرَتَا"))

    val processingContext =
      ProcessingContext(
        NamedTemplate.FormICategoryAGroupUTemplate,
        OutputFormat.Unicode,
        ArabicLetterType.Noon,
        ArabicLetterType.Sad,
        ArabicLetterType.Ra
      )

    val transformer = PastTenseTransformer(VerbGroupType.ThirdPersonFeminine)
    validateTransformer(
      transformer,
      verb.FormI.PastPassiveTense,
      processingContext,
      expected,
      "نُصِرَ"
    )
  }

  test("PastTenseTransformer: SecondPersonMasculine") {
    val expected = ConjugationTuple("ضَرَبْتَ", "ضَرَبْتُمْ", Some("ضَرَبْتُمَا"))

    val processingContext =
      ProcessingContext(
        NamedTemplate.FormICategoryAGroupITemplate,
        OutputFormat.Unicode,
        ArabicLetterType.Ddad,
        ArabicLetterType.Ra,
        ArabicLetterType.Ba
      )

    val transformer = PastTenseTransformer(VerbGroupType.SecondPersonMasculine)
    validateTransformer(
      transformer,
      verb.FormI.PastTenseV1,
      processingContext,
      expected,
      "ضَرَبَ"
    )
  }

  test("PastTenseTransformer: SecondPersonFeminine") {
    val expected = ConjugationTuple("ضُرِبْتِ", "ضُرِبْتُنَّ", Some("ضُرِبْتُمَا"))

    val processingContext =
      ProcessingContext(
        NamedTemplate.FormICategoryAGroupITemplate,
        OutputFormat.Unicode,
        ArabicLetterType.Ddad,
        ArabicLetterType.Ra,
        ArabicLetterType.Ba
      )

    val transformer = PastTenseTransformer(VerbGroupType.SecondPersonFeminine)
    validateTransformer(
      transformer,
      verb.FormI.PastPassiveTense,
      processingContext,
      expected,
      "ضُرِبَ"
    )
  }

  test("PastTenseTransformer: FirstPerson") {
    val expected = ConjugationTuple("ضُرِبْتُ", "ضُرِبْنَا", None)

    val processingContext =
      ProcessingContext(
        NamedTemplate.FormICategoryAGroupITemplate,
        OutputFormat.Unicode,
        ArabicLetterType.Ddad,
        ArabicLetterType.Ra,
        ArabicLetterType.Ba
      )

    val transformer = PastTenseTransformer(VerbGroupType.FirstPerson)
    validateTransformer(
      transformer,
      verb.FormI.PastPassiveTense,
      processingContext,
      expected,
      "ضُرِبَ"
    )
  }

  test("PresentTenseTransformer: ThirdPersonMasculine") {
    val expected = ConjugationTuple("يَنْكَسِرُ", "يَنْكَسِرُوْنَ", Some("يَنْكَسِرَانِ"))

    val processingContext =
      ProcessingContext(
        NamedTemplate.FormVIITemplate,
        OutputFormat.Unicode,
        ArabicLetterType.Kaf,
        ArabicLetterType.Seen,
        ArabicLetterType.Ra
      )

    val transformer = PresentTenseTransformer(VerbGroupType.ThirdPersonMasculine)
    validateTransformer(
      transformer,
      verb.FormVII.PresentTense,
      processingContext,
      expected,
      "يَنْكَسِرُ"
    )
  }

  test("PresentTenseTransformer: ThirdPersonFeminine") {
    val expected = ConjugationTuple("تَقْتَرِبُ", "يَقْتَرِبْنَ", Some("تَقْتَرِبَانِ"))

    val processingContext =
      ProcessingContext(
        NamedTemplate.FormVIIITemplate,
        OutputFormat.Unicode,
        ArabicLetterType.Qaf,
        ArabicLetterType.Ra,
        ArabicLetterType.Ba
      )

    val transformer = PresentTenseTransformer(VerbGroupType.ThirdPersonFeminine)
    validateTransformer(
      transformer,
      verb.FormVIII.PresentTense,
      processingContext,
      expected,
      "يَقْتَرِبُ"
    )
  }

  test("PresentTenseTransformer: SecondPersonMasculine") {
    val expected = ConjugationTuple("تُقْتَرَبُ", "تُقْتَرَبُوْنَ", Some("تُقْتَرَبَانِ"))

    val processingContext =
      ProcessingContext(
        NamedTemplate.FormVIIITemplate,
        OutputFormat.Unicode,
        ArabicLetterType.Qaf,
        ArabicLetterType.Ra,
        ArabicLetterType.Ba
      )

    val transformer = PresentTenseTransformer(VerbGroupType.SecondPersonMasculine)
    validateTransformer(
      transformer,
      verb.FormVIII.PresentPassiveTense,
      processingContext,
      expected,
      "يُقْتَرَبُ"
    )
  }

  test("PresentTenseTransformer: SecondPersonFeminine") {
    val expected = ConjugationTuple("تَسْتَغْفِرِيْنَ", "تَسْتَغْفِرْنَ", Some("تَسْتَغْفِرَانِ"))

    val processingContext =
      ProcessingContext(
        NamedTemplate.FormXTemplate,
        OutputFormat.Unicode,
        ArabicLetterType.Ghain,
        ArabicLetterType.Fa,
        ArabicLetterType.Ra
      )

    val transformer = PresentTenseTransformer(VerbGroupType.SecondPersonFeminine)
    validateTransformer(
      transformer,
      verb.FormX.PresentTense,
      processingContext,
      expected,
      "يَسْتَغْفِرُ"
    )
  }

  test("PastTenseTransformer: FirstPerson") {
    val expected = ConjugationTuple("أُسْتَغْفَرُ", "نُسْتَغْفَرُ", None)

    val processingContext =
      ProcessingContext(
        NamedTemplate.FormXTemplate,
        OutputFormat.Unicode,
        ArabicLetterType.Ghain,
        ArabicLetterType.Fa,
        ArabicLetterType.Ra
      )

    val transformer = PresentTenseTransformer(VerbGroupType.FirstPerson)
    validateTransformer(
      transformer,
      verb.FormX.PresentPassiveTense,
      processingContext,
      expected,
      "يُسْتَغْفَرُ"
    )
  }

  test("ImperativeAndForbiddenTransformer: Imperative: SecondPersonMasculine") {
    val expected = ConjugationTuple("أَسْلِمْ", "أَسْلِمُوْا", Some("أَسْلِمَا"))

    val processingContext =
      ProcessingContext(
        NamedTemplate.FormIVTemplate,
        OutputFormat.Unicode,
        ArabicLetterType.Seen,
        ArabicLetterType.Lam,
        ArabicLetterType.Meem
      )

    val transformer =
      ImperativeAndForbiddenTransformer(
        VerbGroupType.SecondPersonMasculine,
        MorphologyVerbType.Imperative
      )
    validateTransformer(
      transformer,
      verb.FormIV.Imperative,
      processingContext,
      expected,
      "أَسْلِمْ"
    )
  }

  test("ImperativeAndForbiddenTransformer: Imperative: SecondPersonFeminine") {
    val expected = ConjugationTuple("تَعَلَّمِي", "تَعَلَّمْنَ", Some("تَعَلَّمَا"))

    val processingContext =
      ProcessingContext(
        NamedTemplate.FormIITemplate,
        OutputFormat.Unicode,
        ArabicLetterType.Ain,
        ArabicLetterType.Lam,
        ArabicLetterType.Meem
      )

    val transformer =
      ImperativeAndForbiddenTransformer(
        VerbGroupType.SecondPersonFeminine,
        MorphologyVerbType.Imperative
      )
    validateTransformer(
      transformer,
      verb.FormV.Imperative,
      processingContext,
      expected,
      "تَعَلَّمْ"
    )
  }

  test("ImperativeAndForbiddenTransformer: Forbidden: SecondPersonMasculine") {
    val expected = ConjugationTuple("تُعَلِّمْ", "تُعَلِّمُوْا", Some("تُعَلِّمَا"))

    val processingContext =
      ProcessingContext(
        NamedTemplate.FormIITemplate,
        OutputFormat.Unicode,
        ArabicLetterType.Ain,
        ArabicLetterType.Lam,
        ArabicLetterType.Meem
      )

    val transformer =
      ImperativeAndForbiddenTransformer(
        VerbGroupType.SecondPersonMasculine,
        MorphologyVerbType.Forbidden
      )
    validateTransformer(
      transformer,
      verb.FormII.Forbidden,
      processingContext,
      expected,
      "تُعَلِّمْ"
    )
  }

  test("ImperativeAndForbiddenTransformer: Forbidden: SecondPersonFeminine") {
    val expected = ConjugationTuple("تُجَاهِدِي", "تُجَاهِدْنَ", Some("تُجَاهِدَا"))

    val processingContext =
      ProcessingContext(
        NamedTemplate.FormIIITemplate,
        OutputFormat.Unicode,
        ArabicLetterType.Jeem,
        ArabicLetterType.Ha,
        ArabicLetterType.Dal
      )

    val transformer =
      ImperativeAndForbiddenTransformer(
        VerbGroupType.SecondPersonFeminine,
        MorphologyVerbType.Forbidden
      )
    validateTransformer(
      transformer,
      verb.FormIII.Forbidden,
      processingContext,
      expected,
      "تُجَاهِدْ"
    )
  }

  private def validateTransformer(
    transformer: Transformer,
    rootWordSupport: RootWordSupport[?, ?],
    processingContext: ProcessingContext,
    expectedTuple: ConjugationTuple,
    expectedDefaultValue: String
  ): Unit = {
    val rootWord = rootWordSupport.rootWord
    val obtainedConjugationTuple = transformer.doTransform(defaultRuleProcessor, rootWord, processingContext)
    assertEquals(obtainedConjugationTuple, expectedTuple)

    val obtainedDefaultValue = rootWordSupport.defaultValue(defaultRuleProcessor, processingContext)
    assertEquals(obtainedDefaultValue, expectedDefaultValue)
  }

}
