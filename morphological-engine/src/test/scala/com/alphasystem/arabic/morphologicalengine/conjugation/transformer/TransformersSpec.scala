package com.alphasystem
package arabic
package morphologicalengine
package conjugation
package transformer

import arabic.model.{ ArabicLetterType, ProNoun }
import com.alphasystem.arabic.morphologicalanalysis.morphology.model.MorphologyVerbType
import conjugation.model.{
  ConjugationTuple,
  Form,
  OutputFormat,
  RootWord,
  VerbConjugationGroup,
  VerbGroupType,
  noun as nountypes,
  verb as verbtypes
}
import conjugation.rule.IdentityRuleProcessor
import transformer.noun.*
import transformer.noun.AbstractNounTransformer.PluralType
import transformer.verb.{ ImperativeAndForbiddenTransformer, PastTenseTransformer, PresentTenseTransformer }
import munit.FunSuite

class TransformersSpec extends FunSuite {

  private val defaultRuleProcessor = IdentityRuleProcessor()

  test("FormICategoryAGroupUTemplate") {
    val obtained = Form
      .FormICategoryAGroupUTemplate
      .pastTense
      .transform(
        defaultRuleProcessor,
        OutputFormat.Unicode,
        ArabicLetterType.Noon,
        ArabicLetterType.Sad,
        ArabicLetterType.Ra,
        None
      )

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
    val transformer = MasculineNominativeTransformer()
    validateTransformer(
      transformer,
      nountypes.FormIV.MasculineActiveParticiple.rootWord,
      expected,
      ArabicLetterType.Seen,
      ArabicLetterType.Lam,
      ArabicLetterType.Meem
    )
  }

  test("MasculineAccusativeTransformer") {
    val expected = ConjugationTuple("مُعَلَّمًا", "مُعَلَّمَيْنِ", Some("مُعَلَّمَيْنِ"))
    val transformer = MasculineAccusativeTransformer()
    validateTransformer(
      transformer,
      nountypes.FormII.MasculinePassiveParticiple.rootWord,
      expected,
      ArabicLetterType.Ain,
      ArabicLetterType.Lam,
      ArabicLetterType.Meem
    )
  }

  test("MasculineGenitiveTransformer") {
    val expected = ConjugationTuple("مُسْتَغْفِرٍ", "مُسْتَغْفِرِيْنَ", Some("مُسْتَغْفِرِيْنَ"))
    val transformer = MasculineGenitiveTransformer()
    validateTransformer(
      transformer,
      nountypes.FormX.MasculineActiveParticiple.rootWord,
      expected,
      ArabicLetterType.Ghain,
      ArabicLetterType.Fa,
      ArabicLetterType.Ra
    )
  }

  test("FeminineNominativeTransformer: from masculine word") {
    val expected = ConjugationTuple("مُقْتَرَبَةٌ", "مُقْتَرَبَاتٌ", Some("مُقْتَرَبَتَانِ"))
    val transformer = FeminineNominativeTransformer()
    validateTransformer(
      transformer,
      nountypes.FormVIII.MasculinePassiveParticiple.rootWord,
      expected,
      ArabicLetterType.Qaf,
      ArabicLetterType.Ra,
      ArabicLetterType.Ba
    )
  }

  test("FeminineNominativeTransformer: from feminine word") {
    val expected = ConjugationTuple("مُقْتَرِبَةٌ", "مُقْتَرِبَاتٌ", Some("مُقْتَرِبَتَانِ"))
    val transformer = FeminineNominativeTransformer()
    validateTransformer(
      transformer,
      nountypes.FormVIII.FeminineActiveParticiple.rootWord,
      expected,
      ArabicLetterType.Qaf,
      ArabicLetterType.Ra,
      ArabicLetterType.Ba
    )
  }

  test("FeminineAccusativeTransformer: from masculine word") {
    val expected = ConjugationTuple("مُجَاهِدَةً", "مُجَاهِدَاتٍ", Some("مُجَاهِدَتَيْنِ"))
    val transformer = FeminineAccusativeTransformer()
    validateTransformer(
      transformer,
      nountypes.FormIII.MasculineActiveParticiple.rootWord,
      expected,
      ArabicLetterType.Jeem,
      ArabicLetterType.Ha,
      ArabicLetterType.Dal
    )
  }

  test("FeminineAccusativeTransformer: from feminine word") {
    val expected = ConjugationTuple("مُجَاهَدَةً", "مُجَاهَدَاتٍ", Some("مُجَاهَدَتَيْنِ"))
    val transformer = FeminineAccusativeTransformer()
    validateTransformer(
      transformer,
      nountypes.FormIII.FemininePassiveParticiple.rootWord,
      expected,
      ArabicLetterType.Jeem,
      ArabicLetterType.Ha,
      ArabicLetterType.Dal
    )
  }

  test("FeminineGenitiveTransformer: from masculine word") {
    val expected = ConjugationTuple("مُتَعَلَّمَةٍ", "مُتَعَلَّمَاتٍ", Some("مُتَعَلَّمَتَيْنِ"))
    val transformer = FeminineGenitiveTransformer()
    validateTransformer(
      transformer,
      nountypes.FormV.MasculinePassiveParticiple.rootWord,
      expected,
      ArabicLetterType.Ain,
      ArabicLetterType.Lam,
      ArabicLetterType.Meem
    )
  }

  test("FeminineGenitiveTransformer: from feminine word") {
    val expected = ConjugationTuple("مُتَعَارَفَةٍ", "مُتَعَارَفَاتٍ", Some("مُتَعَارَفَتَيْنِ"))
    val transformer = FeminineGenitiveTransformer()
    validateTransformer(
      transformer,
      nountypes.FormVI.FemininePassiveParticiple.rootWord,
      expected,
      ArabicLetterType.Ain,
      ArabicLetterType.Ra,
      ArabicLetterType.Fa
    )
  }

  test("VerbalNoun: masculine based") {
    val expected = ConjugationTuple("إِسْلَامٌ", "إِسْلَامَاتٌ", Some("إِسْلَامَانِ"))
    val transformer = MasculineNominativeTransformer(pluralType = PluralType.Feminine)
    validateTransformer(
      transformer,
      nountypes.VerbalNoun.FormIV.rootWord,
      expected,
      ArabicLetterType.Seen,
      ArabicLetterType.Lam,
      ArabicLetterType.Meem
    )
  }

  test("VerbalNoun: feminine based") {
    val expected = ConjugationTuple("مُجَاهِدَةً", "مُجَاهِدَاتٍ", Some("مُجَاهِدَتَيْنِ"))
    val transformer = FeminineAccusativeTransformer()
    validateTransformer(
      transformer,
      nountypes.VerbalNoun.FormIIIV2.rootWord,
      expected,
      ArabicLetterType.Jeem,
      ArabicLetterType.Ha,
      ArabicLetterType.Dal
    )
  }

  test("PastTenseTransformer: ThirdPersonMasculine") {
    val expected = ConjugationTuple("نَصَرَ", "نَصَرُوْا", Some("نَصَرَا"))
    val transformer = PastTenseTransformer(VerbGroupType.ThirdPersonMasculine)
    validateTransformer(
      transformer,
      verbtypes.FormI.PastTenseV1.rootWord,
      expected,
      ArabicLetterType.Noon,
      ArabicLetterType.Sad,
      ArabicLetterType.Ra
    )
  }

  test("PastTenseTransformer: ThirdPersonFeminine") {
    val expected = ConjugationTuple("نُصِرَتْ", "نُصِرْنَ", Some("نُصِرَتَا"))
    val transformer = PastTenseTransformer(VerbGroupType.ThirdPersonFeminine)
    validateTransformer(
      transformer,
      verbtypes.FormI.PastPassiveTense.rootWord,
      expected,
      ArabicLetterType.Noon,
      ArabicLetterType.Sad,
      ArabicLetterType.Ra
    )
  }

  test("PastTenseTransformer: SecondPersonMasculine") {
    val expected = ConjugationTuple("ضَرَبْتَ", "ضَرَبْتُمْ", Some("ضَرَبْتُمَا"))
    val transformer = PastTenseTransformer(VerbGroupType.SecondPersonMasculine)
    validateTransformer(
      transformer,
      verbtypes.FormI.PastTenseV1.rootWord,
      expected,
      ArabicLetterType.Ddad,
      ArabicLetterType.Ra,
      ArabicLetterType.Ba
    )
  }

  test("PastTenseTransformer: SecondPersonFeminine") {
    val expected = ConjugationTuple("ضُرِبْتِ", "ضُرِبْتُنَّ", Some("ضُرِبْتُمَا"))
    val transformer = PastTenseTransformer(VerbGroupType.SecondPersonFeminine)
    validateTransformer(
      transformer,
      verbtypes.FormI.PastPassiveTense.rootWord,
      expected,
      ArabicLetterType.Ddad,
      ArabicLetterType.Ra,
      ArabicLetterType.Ba
    )
  }

  test("PastTenseTransformer: FirstPerson") {
    val expected = ConjugationTuple("ضُرِبْتُ", "ضُرِبْنَا", None)
    val transformer = PastTenseTransformer(VerbGroupType.FirstPerson)
    validateTransformer(
      transformer,
      verbtypes.FormI.PastPassiveTense.rootWord,
      expected,
      ArabicLetterType.Ddad,
      ArabicLetterType.Ra,
      ArabicLetterType.Ba
    )
  }

  test("PresentTenseTransformer: ThirdPersonMasculine") {
    val expected = ConjugationTuple("يَنْكَسِرُ", "يَنْكَسِرُوْنَ", Some("يَنْكَسِرَانِ"))
    val transformer = PresentTenseTransformer(VerbGroupType.ThirdPersonMasculine)
    validateTransformer(
      transformer,
      verbtypes.FormVII.PresentTense.rootWord,
      expected,
      ArabicLetterType.Kaf,
      ArabicLetterType.Seen,
      ArabicLetterType.Ra
    )
  }

  test("PresentTenseTransformer: ThirdPersonFeminine") {
    val expected = ConjugationTuple("تَقْتَرِبُ", "يَقْتَرِبْنَ", Some("تَقْتَرِبَانِ"))
    val transformer = PresentTenseTransformer(VerbGroupType.ThirdPersonFeminine)
    validateTransformer(
      transformer,
      verbtypes.FormVIII.PresentTense.rootWord,
      expected,
      ArabicLetterType.Qaf,
      ArabicLetterType.Ra,
      ArabicLetterType.Ba
    )
  }

  test("PresentTenseTransformer: SecondPersonMasculine") {
    val expected = ConjugationTuple("تُقْتَرَبُ", "تُقْتَرَبُوْنَ", Some("تُقْتَرَبَانِ"))
    val transformer = PresentTenseTransformer(VerbGroupType.SecondPersonMasculine)
    validateTransformer(
      transformer,
      verbtypes.FormVIII.PresentPassiveTense.rootWord,
      expected,
      ArabicLetterType.Qaf,
      ArabicLetterType.Ra,
      ArabicLetterType.Ba
    )
  }

  test("PresentTenseTransformer: SecondPersonFeminine") {
    val expected = ConjugationTuple("تَسْتَغْفِرِيْنَ", "تَسْتَغْفِرْنَ", Some("تَسْتَغْفِرَانِ"))
    val transformer = PresentTenseTransformer(VerbGroupType.SecondPersonFeminine)
    validateTransformer(
      transformer,
      verbtypes.FormX.PresentTense.rootWord,
      expected,
      ArabicLetterType.Ghain,
      ArabicLetterType.Fa,
      ArabicLetterType.Ra
    )
  }

  test("PastTenseTransformer: FirstPerson") {
    val expected = ConjugationTuple("ءُسْتَغْفَرُ", "نُسْتَغْفَرُ", None)
    val transformer = PresentTenseTransformer(VerbGroupType.FirstPerson)
    validateTransformer(
      transformer,
      verbtypes.FormX.PresentPassiveTense.rootWord,
      expected,
      ArabicLetterType.Ghain,
      ArabicLetterType.Fa,
      ArabicLetterType.Ra
    )
  }

  test("ImperativeAndForbiddenTransformer: Imperative: SecondPersonMasculine") {
    val expected = ConjugationTuple("أَسْلِمْ", "أَسْلِمُوْا", Some("أَسْلِمَا"))
    val transformer =
      ImperativeAndForbiddenTransformer(
        VerbGroupType.SecondPersonMasculine,
        MorphologyVerbType.Command
      )
    validateTransformer(
      transformer,
      verbtypes.FormIV.Imperative.rootWord,
      expected,
      ArabicLetterType.Seen,
      ArabicLetterType.Lam,
      ArabicLetterType.Meem
    )
  }

  test("ImperativeAndForbiddenTransformer: Imperative: SecondPersonFeminine") {
    val expected = ConjugationTuple("تَعَلَّمِي", "تَعَلَّمْنَ", Some("تَعَلَّمَا"))
    val transformer =
      ImperativeAndForbiddenTransformer(
        VerbGroupType.SecondPersonFeminine,
        MorphologyVerbType.Command
      )
    validateTransformer(
      transformer,
      verbtypes.FormV.Imperative.rootWord,
      expected,
      ArabicLetterType.Ain,
      ArabicLetterType.Lam,
      ArabicLetterType.Meem
    )
  }

  test("ImperativeAndForbiddenTransformer: Forbidden: SecondPersonMasculine") {
    val expected = ConjugationTuple("تُعَلِّمْ", "تُعَلِّمُوْا", Some("تُعَلِّمَا"))
    val transformer =
      ImperativeAndForbiddenTransformer(
        VerbGroupType.SecondPersonMasculine,
        MorphologyVerbType.Forbidden
      )
    validateTransformer(
      transformer,
      verbtypes.FormII.Forbidden.rootWord,
      expected,
      ArabicLetterType.Ain,
      ArabicLetterType.Lam,
      ArabicLetterType.Meem
    )
  }

  test("ImperativeAndForbiddenTransformer: Forbidden: SecondPersonFeminine") {
    val expected = ConjugationTuple("تُجَاهِدِي", "تُجَاهِدْنَ", Some("تُجَاهِدَا"))
    val transformer =
      ImperativeAndForbiddenTransformer(
        VerbGroupType.SecondPersonFeminine,
        MorphologyVerbType.Forbidden
      )
    validateTransformer(
      transformer,
      verbtypes.FormIII.Forbidden.rootWord,
      expected,
      ArabicLetterType.Jeem,
      ArabicLetterType.Ha,
      ArabicLetterType.Dal
    )
  }

  private def validateTransformer(
    transformer: Transformer,
    rootWord: RootWord,
    expected: ConjugationTuple,
    firstRadical: ArabicLetterType,
    secondRadical: ArabicLetterType,
    thirdRadical: ArabicLetterType,
    fourthRadical: Option[ArabicLetterType] = None
  ): Unit = {
    val conjugationTuple = transformer
      .doTransform(
        defaultRuleProcessor,
        rootWord,
        OutputFormat.Unicode,
        firstRadical,
        secondRadical,
        thirdRadical,
        fourthRadical
      )
    assertEquals(conjugationTuple, expected)
  }

}
