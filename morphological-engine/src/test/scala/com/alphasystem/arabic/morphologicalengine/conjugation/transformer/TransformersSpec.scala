package com.alphasystem
package arabic
package morphologicalengine
package conjugation
package transformer

import arabic.model.{ ArabicLetterType, ProNoun }
import arabic.morphologicalanalysis.morphology.model.MorphologyVerbType
import conjugation.model.internal.{ RootWord, VerbGroupType }
import conjugation.forms.{ Form, noun, verb }
import conjugation.model.{ ConjugationTuple, NamedTemplate, OutputFormat, VerbConjugationGroup }
import conjugation.rule.IdentityRuleProcessor
import transformer.noun.*
import transformer.noun.AbstractNounTransformer.PluralType
import transformer.verb.{ ImperativeAndForbiddenTransformer, PastTenseTransformer, PresentTenseTransformer }
import munit.FunSuite

class TransformersSpec extends FunSuite {

  private val defaultRuleProcessor = IdentityRuleProcessor()

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
      noun.FormIV.MasculineActiveParticiple.rootWord,
      expected,
      processingContext
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
      noun.FormII.MasculinePassiveParticiple.rootWord,
      expected,
      processingContext
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
      noun.FormX.MasculineActiveParticiple.rootWord,
      expected,
      processingContext
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
      noun.FormVIII.MasculinePassiveParticiple.rootWord,
      expected,
      processingContext
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
      noun.FormVIII.FeminineActiveParticiple.rootWord,
      expected,
      processingContext
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
      noun.FormIII.MasculineActiveParticiple.rootWord,
      expected,
      processingContext
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
      noun.FormIII.FemininePassiveParticiple.rootWord,
      expected,
      processingContext
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
      noun.FormV.MasculinePassiveParticiple.rootWord,
      expected,
      processingContext
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
      noun.FormVI.FemininePassiveParticiple.rootWord,
      expected,
      processingContext
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
      noun.VerbalNoun.FormIV.rootWord,
      expected,
      processingContext
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
      noun.VerbalNoun.FormIIIV2.rootWord,
      expected,
      processingContext
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
      verb.FormI.PastTenseV1.rootWord,
      expected,
      processingContext
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
      verb.FormI.PastPassiveTense.rootWord,
      expected,
      processingContext
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
      verb.FormI.PastTenseV1.rootWord,
      expected,
      processingContext
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
      verb.FormI.PastPassiveTense.rootWord,
      expected,
      processingContext
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
      verb.FormI.PastPassiveTense.rootWord,
      expected,
      processingContext
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
      verb.FormVII.PresentTense.rootWord,
      expected,
      processingContext
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
      verb.FormVIII.PresentTense.rootWord,
      expected,
      processingContext
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
      verb.FormVIII.PresentPassiveTense.rootWord,
      expected,
      processingContext
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
      verb.FormX.PresentTense.rootWord,
      expected,
      processingContext
    )
  }

  test("PastTenseTransformer: FirstPerson") {
    val expected = ConjugationTuple("ءُسْتَغْفَرُ", "نُسْتَغْفَرُ", None)

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
      verb.FormX.PresentPassiveTense.rootWord,
      expected,
      processingContext
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
        MorphologyVerbType.Command
      )
    validateTransformer(
      transformer,
      verb.FormIV.Imperative.rootWord,
      expected,
      processingContext
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
        MorphologyVerbType.Command
      )
    validateTransformer(
      transformer,
      verb.FormV.Imperative.rootWord,
      expected,
      processingContext
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
      verb.FormII.Forbidden.rootWord,
      expected,
      processingContext
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
      verb.FormIII.Forbidden.rootWord,
      expected,
      processingContext
    )
  }

  private def validateTransformer(
    transformer: Transformer,
    rootWord: RootWord,
    expected: ConjugationTuple,
    processingContext: ProcessingContext
  ): Unit = {
    val conjugationTuple = transformer.doTransform(defaultRuleProcessor, rootWord, processingContext)
    assertEquals(conjugationTuple, expected)
  }

}
