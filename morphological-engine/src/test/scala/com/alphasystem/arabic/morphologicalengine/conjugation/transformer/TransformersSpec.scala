package com.alphasystem
package arabic
package morphologicalengine
package conjugation
package transformer

import arabic.model.{ ArabicLetterType, ProNoun }
import com.alphasystem.arabic.morphologicalanalysis.morphology.model.VerbType
import conjugation.model.noun.{ Noun, VerbalNoun }
import conjugation.model.verb.{ FormI, FormII, FormIII, FormIV, FormV, FormVII, FormVIII, FormX }
import conjugation.model.{ ConjugationTuple, OutputFormat, RootWord, VerbGroupType }
import conjugation.rule.IdentityRuleProcessor
import transformer.noun.*
import transformer.noun.AbstractNounTransformer.PluralType
import transformer.verb.{ ImperativeAndForbiddenTransformer, PastTenseTransformer, PresentTenseTransformer }
import munit.FunSuite

class TransformersSpec extends FunSuite {

  private val defaultRuleProcessor = IdentityRuleProcessor()

  test("MasculineNominativeTransformer") {
    val expected = ConjugationTuple("مُسْلِمٌ", "مُسْلِمُوْنَ", Some("مُسْلِمَانِ"))
    val transformer = MasculineNominativeTransformer(defaultRuleProcessor)
    validateTransformer(
      transformer,
      Noun.FormIVMasculineActiveParticiple.rootWord,
      expected,
      ArabicLetterType.Seen,
      ArabicLetterType.Lam,
      ArabicLetterType.Meem
    )
  }

  test("MasculineAccusativeTransformer") {
    val expected = ConjugationTuple("مُعَلَّمًا", "مُعَلَّمَيْنِ", Some("مُعَلَّمَيْنِ"))
    val transformer = MasculineAccusativeTransformer(defaultRuleProcessor)
    validateTransformer(
      transformer,
      Noun.FormIIMasculinePassiveParticiple.rootWord,
      expected,
      ArabicLetterType.Ain,
      ArabicLetterType.Lam,
      ArabicLetterType.Meem
    )
  }

  test("MasculineGenitiveTransformer") {
    val expected = ConjugationTuple("مُسْتَغْفِرٍ", "مُسْتَغْفِرِيْنَ", Some("مُسْتَغْفِرِيْنَ"))
    val transformer = MasculineGenitiveTransformer(defaultRuleProcessor)
    validateTransformer(
      transformer,
      Noun.FormXMasculineActiveParticiple.rootWord,
      expected,
      ArabicLetterType.Ghain,
      ArabicLetterType.Fa,
      ArabicLetterType.Ra
    )
  }

  test("FeminineNominativeTransformer: from masculine word") {
    val expected = ConjugationTuple("مُقْتَرَبَةٌ", "مُقْتَرَبَاتٌ", Some("مُقْتَرَبَتَانِ"))
    val transformer = FeminineNominativeTransformer(defaultRuleProcessor)
    validateTransformer(
      transformer,
      Noun.FormVIIIMasculinePassiveParticiple.rootWord,
      expected,
      ArabicLetterType.Qaf,
      ArabicLetterType.Ra,
      ArabicLetterType.Ba
    )
  }

  test("FeminineNominativeTransformer: from feminine word") {
    val expected = ConjugationTuple("مُقْتَرِبَةٌ", "مُقْتَرِبَاتٌ", Some("مُقْتَرِبَتَانِ"))
    val transformer = FeminineNominativeTransformer(defaultRuleProcessor)
    validateTransformer(
      transformer,
      Noun.FormVIIIFeminineActiveParticiple.rootWord,
      expected,
      ArabicLetterType.Qaf,
      ArabicLetterType.Ra,
      ArabicLetterType.Ba
    )
  }

  test("FeminineAccusativeTransformer: from masculine word") {
    val expected = ConjugationTuple("مُجَاهِدَةً", "مُجَاهِدَاتٍ", Some("مُجَاهِدَتَيْنِ"))
    val transformer = FeminineAccusativeTransformer(defaultRuleProcessor)
    validateTransformer(
      transformer,
      Noun.FormIIIMasculineActiveParticiple.rootWord,
      expected,
      ArabicLetterType.Jeem,
      ArabicLetterType.Ha,
      ArabicLetterType.Dal
    )
  }

  test("FeminineAccusativeTransformer: from feminine word") {
    val expected = ConjugationTuple("مُجَاهَدَةً", "مُجَاهَدَاتٍ", Some("مُجَاهَدَتَيْنِ"))
    val transformer = FeminineAccusativeTransformer(defaultRuleProcessor)
    validateTransformer(
      transformer,
      Noun.FormIIIFemininePassiveParticiple.rootWord,
      expected,
      ArabicLetterType.Jeem,
      ArabicLetterType.Ha,
      ArabicLetterType.Dal
    )
  }

  test("FeminineGenitiveTransformer: from masculine word") {
    val expected = ConjugationTuple("مُتَعَلَّمَةٍ", "مُتَعَلَّمَاتٍ", Some("مُتَعَلَّمَتَيْنِ"))
    val transformer = FeminineGenitiveTransformer(defaultRuleProcessor)
    validateTransformer(
      transformer,
      Noun.FormVMasculinePassiveParticiple.rootWord,
      expected,
      ArabicLetterType.Ain,
      ArabicLetterType.Lam,
      ArabicLetterType.Meem
    )
  }

  test("FeminineGenitiveTransformer: from feminine word") {
    val expected = ConjugationTuple("مُتَعَارَفَةٍ", "مُتَعَارَفَاتٍ", Some("مُتَعَارَفَتَيْنِ"))
    val transformer = FeminineGenitiveTransformer(defaultRuleProcessor)
    validateTransformer(
      transformer,
      Noun.FormVIFemininePassiveParticiple.rootWord,
      expected,
      ArabicLetterType.Ain,
      ArabicLetterType.Ra,
      ArabicLetterType.Fa
    )
  }

  test("VerbalNoun: masculine based") {
    val expected = ConjugationTuple("إِسْلَامٌ", "إِسْلَامَاتٌ", Some("إِسْلَامَانِ"))
    val transformer = MasculineNominativeTransformer(defaultRuleProcessor, pluralType = PluralType.Feminine)
    validateTransformer(
      transformer,
      VerbalNoun.FormIV.rootWord,
      expected,
      ArabicLetterType.Seen,
      ArabicLetterType.Lam,
      ArabicLetterType.Meem
    )
  }

  test("VerbalNoun: feminine based") {
    val expected = ConjugationTuple("مُجَاهِدَةً", "مُجَاهِدَاتٍ", Some("مُجَاهِدَتَيْنِ"))
    val transformer = FeminineAccusativeTransformer(defaultRuleProcessor)
    validateTransformer(
      transformer,
      VerbalNoun.FormIIIV2.rootWord,
      expected,
      ArabicLetterType.Jeem,
      ArabicLetterType.Ha,
      ArabicLetterType.Dal
    )
  }

  test("PastTenseTransformer: ThirdPersonMasculine") {
    val expected = ConjugationTuple("نَصَرَ", "نَصَرُوْا", Some("نَصَرَا"))
    val transformer = PastTenseTransformer(defaultRuleProcessor, VerbGroupType.ThirdPersonMasculine)
    validateTransformer(
      transformer,
      FormI.PastTenseV1.rootWord,
      expected,
      ArabicLetterType.Noon,
      ArabicLetterType.Sad,
      ArabicLetterType.Ra
    )
  }

  test("PastTenseTransformer: ThirdPersonFeminine") {
    val expected = ConjugationTuple("نُصِرَتْ", "نُصِرْنَ", Some("نُصِرَتَا"))
    val transformer = PastTenseTransformer(defaultRuleProcessor, VerbGroupType.ThirdPersonFeminine)
    validateTransformer(
      transformer,
      FormI.PastPassiveTense.rootWord,
      expected,
      ArabicLetterType.Noon,
      ArabicLetterType.Sad,
      ArabicLetterType.Ra
    )
  }

  test("PastTenseTransformer: SecondPersonMasculine") {
    val expected = ConjugationTuple("ضَرَبْتَ", "ضَرَبْتُمْ", Some("ضَرَبْتُمَا"))
    val transformer = PastTenseTransformer(defaultRuleProcessor, VerbGroupType.SecondPersonMasculine)
    validateTransformer(
      transformer,
      FormI.PastTenseV1.rootWord,
      expected,
      ArabicLetterType.Ddad,
      ArabicLetterType.Ra,
      ArabicLetterType.Ba
    )
  }

  test("PastTenseTransformer: SecondPersonFeminine") {
    val expected = ConjugationTuple("ضُرِبْتِ", "ضُرِبْتُنَّ", Some("ضُرِبْتُمَا"))
    val transformer = PastTenseTransformer(defaultRuleProcessor, VerbGroupType.SecondPersonFeminine)
    validateTransformer(
      transformer,
      FormI.PastPassiveTense.rootWord,
      expected,
      ArabicLetterType.Ddad,
      ArabicLetterType.Ra,
      ArabicLetterType.Ba
    )
  }

  test("PastTenseTransformer: FirstPerson") {
    val expected = ConjugationTuple("ضُرِبْتُ", "ضُرِبْنَا", None)
    val transformer = PastTenseTransformer(defaultRuleProcessor, VerbGroupType.FirstPerson)
    validateTransformer(
      transformer,
      FormI.PastPassiveTense.rootWord,
      expected,
      ArabicLetterType.Ddad,
      ArabicLetterType.Ra,
      ArabicLetterType.Ba
    )
  }

  test("PresentTenseTransformer: ThirdPersonMasculine") {
    val expected = ConjugationTuple("يَنْكَسِرُ", "يَنْكَسِرُوْنَ", Some("يَنْكَسِرَانِ"))
    val transformer = PresentTenseTransformer(defaultRuleProcessor, VerbGroupType.ThirdPersonMasculine)
    validateTransformer(
      transformer,
      FormVII.PresentTense.rootWord,
      expected,
      ArabicLetterType.Kaf,
      ArabicLetterType.Seen,
      ArabicLetterType.Ra
    )
  }

  test("PresentTenseTransformer: ThirdPersonFeminine") {
    val expected = ConjugationTuple("تَقْتَرِبُ", "يَقْتَرِبْنَ", Some("تَقْتَرِبَانِ"))
    val transformer = PresentTenseTransformer(defaultRuleProcessor, VerbGroupType.ThirdPersonFeminine)
    validateTransformer(
      transformer,
      FormVIII.PresentTense.rootWord,
      expected,
      ArabicLetterType.Qaf,
      ArabicLetterType.Ra,
      ArabicLetterType.Ba
    )
  }

  test("PresentTenseTransformer: SecondPersonMasculine") {
    val expected = ConjugationTuple("تُقْتَرَبُ", "تُقْتَرَبُوْنَ", Some("تُقْتَرَبَانِ"))
    val transformer = PresentTenseTransformer(defaultRuleProcessor, VerbGroupType.SecondPersonMasculine)
    validateTransformer(
      transformer,
      FormVIII.PresentPassiveTense.rootWord,
      expected,
      ArabicLetterType.Qaf,
      ArabicLetterType.Ra,
      ArabicLetterType.Ba
    )
  }

  test("PresentTenseTransformer: SecondPersonFeminine") {
    val expected = ConjugationTuple("تَسْتَغْفِرِيْنَ", "تَسْتَغْفِرْنَ", Some("تَسْتَغْفِرَانِ"))
    val transformer = PresentTenseTransformer(defaultRuleProcessor, VerbGroupType.SecondPersonFeminine)
    validateTransformer(
      transformer,
      FormX.PresentTense.rootWord,
      expected,
      ArabicLetterType.Ghain,
      ArabicLetterType.Fa,
      ArabicLetterType.Ra
    )
  }

  test("PastTenseTransformer: FirstPerson") {
    val expected = ConjugationTuple("ءُسْتَغْفَرُ", "نُسْتَغْفَرُ", None)
    val transformer = PresentTenseTransformer(defaultRuleProcessor, VerbGroupType.FirstPerson)
    validateTransformer(
      transformer,
      FormX.PresentPassiveTense.rootWord,
      expected,
      ArabicLetterType.Ghain,
      ArabicLetterType.Fa,
      ArabicLetterType.Ra
    )
  }

  test("ImperativeAndForbiddenTransformer: Imperative: SecondPersonMasculine") {
    val expected = ConjugationTuple("أَسْلِمْ", "أَسْلِمُوْا", Some("أَسْلِمَا"))
    val transformer =
      ImperativeAndForbiddenTransformer(defaultRuleProcessor, VerbGroupType.SecondPersonMasculine, VerbType.Command)
    validateTransformer(
      transformer,
      FormIV.Imperative.rootWord,
      expected,
      ArabicLetterType.Seen,
      ArabicLetterType.Lam,
      ArabicLetterType.Meem
    )
  }

  test("ImperativeAndForbiddenTransformer: Imperative: SecondPersonFeminine") {
    val expected = ConjugationTuple("تَعَلَّمِي", "تَعَلَّمْنَ", Some("تَعَلَّمَا"))
    val transformer =
      ImperativeAndForbiddenTransformer(defaultRuleProcessor, VerbGroupType.SecondPersonFeminine, VerbType.Command)
    validateTransformer(
      transformer,
      FormV.Imperative.rootWord,
      expected,
      ArabicLetterType.Ain,
      ArabicLetterType.Lam,
      ArabicLetterType.Meem
    )
  }

  test("ImperativeAndForbiddenTransformer: Forbidden: SecondPersonMasculine") {
    val expected = ConjugationTuple("تُعَلِّمْ", "تُعَلِّمُوْا", Some("تُعَلِّمَا"))
    val transformer =
      ImperativeAndForbiddenTransformer(defaultRuleProcessor, VerbGroupType.SecondPersonMasculine, VerbType.Forbidden)
    validateTransformer(
      transformer,
      FormII.Forbidden.rootWord,
      expected,
      ArabicLetterType.Ain,
      ArabicLetterType.Lam,
      ArabicLetterType.Meem
    )
  }

  test("ImperativeAndForbiddenTransformer: Forbidden: SecondPersonFeminine") {
    val expected = ConjugationTuple("تُجَاهِدِي", "تُجَاهِدْنَ", Some("تُجَاهِدَا"))
    val transformer =
      ImperativeAndForbiddenTransformer(defaultRuleProcessor, VerbGroupType.SecondPersonFeminine, VerbType.Forbidden)
    validateTransformer(
      transformer,
      FormIII.Forbidden.rootWord,
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
      .doTransform(rootWord, OutputFormat.Unicode, firstRadical, secondRadical, thirdRadical, fourthRadical)
    assertEquals(conjugationTuple, expected)
  }

}
