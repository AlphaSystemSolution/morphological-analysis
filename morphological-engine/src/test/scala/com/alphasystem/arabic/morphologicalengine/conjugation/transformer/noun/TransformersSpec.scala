package com.alphasystem
package arabic
package morphologicalengine
package conjugation
package transformer
package noun

import arabic.model.ArabicLetterType
import com.alphasystem.arabic.morphologicalengine.conjugation.transformer.noun.AbstractNounTransformer.PluralType
import conjugation.model.{ ConjugationTuple, OutputFormat, RootWord }
import conjugation.model.noun.{ Noun, VerbalNoun }
import conjugation.rule.IdentityRuleProcessor
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
