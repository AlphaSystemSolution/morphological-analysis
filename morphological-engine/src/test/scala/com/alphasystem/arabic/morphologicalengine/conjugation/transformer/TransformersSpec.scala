package com.alphasystem
package arabic
package morphologicalengine
package conjugation
package transformer

import arabic.model.{ ArabicLetterType, ArabicLetters, ArabicWord, JussiveParticle }
import arabic.morphologicalanalysis.morphology.model.MorphologyVerbType
import conjugation.model.internal.VerbGroupType
import conjugation.forms.{ Form, RootWordSupport, VerbSupport, noun, verb }
import conjugation.model.{ ConjugationTuple, NamedTemplate, NounConjugationGroup, OutputFormat, VerbConjugationGroup }
import conjugation.rule.RuleEngine
import transformer.noun.*
import transformer.noun.AbstractNounTransformer.PluralType
import transformer.verb.{
  ImperativeAndForbiddenTransformer,
  PastTenseTransformer,
  PresentTenseJussiveModeTransformer,
  PresentTenseTransformer
}
import munit.FunSuite

class TransformersSpec extends FunSuite {

  private val negationPrefix = ArabicWord(ArabicLetterType.Lam, ArabicLetterType.Alif)

  private val defaultRuleProcessor = RuleEngine()

  /** Case describing a [[Form]]-level verb conjugation (past tense, imperative, or forbidden), built from a
    * [[NamedTemplate]] and a triliteral root.
    */
  private case class VerbFormCase(
    name: String,
    namedTemplate: NamedTemplate,
    firstRadical: ArabicLetterType,
    secondRadical: ArabicLetterType,
    thirdRadical: ArabicLetterType,
    selector: Form => VerbSupport,
    expected: VerbConjugationGroup)

  private val verbFormCases = Seq(
    VerbFormCase(
      "FormICategoryAGroupUTemplate",
      NamedTemplate.FormICategoryAGroupUTemplate,
      ArabicLetterType.Noon,
      ArabicLetterType.Sad,
      ArabicLetterType.Ra,
      _.pastTense,
      VerbConjugationGroup(
        masculineSecondPerson = ConjugationTuple("نَصَرْتَ", "نَصَرْتُمْ", Some("نَصَرْتُمَا")),
        feminineSecondPerson = ConjugationTuple("نَصَرْتِ", "نَصَرْتُنَّ", Some("نَصَرْتُمَا")),
        masculineThirdPerson = Some(ConjugationTuple("نَصَرَ", "نَصَرُوْا", Some("نَصَرَا"))),
        feminineThirdPerson = Some(ConjugationTuple("نَصَرَتْ", "نَصَرْنَ", Some("نَصَرَتَا"))),
        firstPerson = Some(ConjugationTuple("نَصَرْتُ", "نَصَرْنَا", None))
      )
    ),
    VerbFormCase(
      "Imperative: FormICategoryAGroupUTemplate",
      NamedTemplate.FormICategoryAGroupUTemplate,
      ArabicLetterType.Noon,
      ArabicLetterType.Sad,
      ArabicLetterType.Ra,
      _.imperative,
      VerbConjugationGroup(
        masculineSecondPerson = ConjugationTuple(
          ArabicWord(
            ArabicLetters.AlifHamzaAboveWithDamma,
            ArabicLetters.NoonWithSukun,
            ArabicLetters.SadWithDamma,
            ArabicLetters.RaWithSukun
          ),
          ArabicWord(
            ArabicLetters.AlifHamzaAboveWithDamma,
            ArabicLetters.NoonWithSukun,
            ArabicLetters.SadWithDamma,
            ArabicLetters.RaWithDamma,
            ArabicLetters.WawWithSukun,
            ArabicLetters.LetterAlif
          ),
          Some(
            ArabicWord(
              ArabicLetters.AlifHamzaAboveWithDamma,
              ArabicLetters.NoonWithSukun,
              ArabicLetters.SadWithDamma,
              ArabicLetters.RaWithFatha,
              ArabicLetters.LetterAlif
            )
          )
        ),
        feminineSecondPerson = ConjugationTuple(
          ArabicWord(
            ArabicLetters.AlifHamzaAboveWithDamma,
            ArabicLetters.NoonWithSukun,
            ArabicLetters.SadWithDamma,
            ArabicLetters.RaWithKasra,
            ArabicLetters.YaWithSukun
          ),
          ArabicWord(
            ArabicLetters.AlifHamzaAboveWithDamma,
            ArabicLetters.NoonWithSukun,
            ArabicLetters.SadWithDamma,
            ArabicLetters.RaWithSukun,
            ArabicLetters.NoonWithFatha
          ),
          Some(
            ArabicWord(
              ArabicLetters.AlifHamzaAboveWithDamma,
              ArabicLetters.NoonWithSukun,
              ArabicLetters.SadWithDamma,
              ArabicLetters.RaWithFatha,
              ArabicLetters.LetterAlif
            )
          )
        )
      )
    ),
    VerbFormCase(
      "Forbidden: FormICategoryAGroupUTemplate",
      NamedTemplate.FormICategoryAGroupUTemplate,
      ArabicLetterType.Noon,
      ArabicLetterType.Sad,
      ArabicLetterType.Ra,
      _.forbidden,
      VerbConjugationGroup(
        masculineSecondPerson = ConjugationTuple(
          negationPrefix.concatWithSpace(
            ArabicWord(
              ArabicLetters.TaWithFatha,
              ArabicLetters.NoonWithSukun,
              ArabicLetters.SadWithDamma,
              ArabicLetters.RaWithSukun
            )
          ),
          negationPrefix.concatWithSpace(
            ArabicWord(
              ArabicLetters.TaWithFatha,
              ArabicLetters.NoonWithSukun,
              ArabicLetters.SadWithDamma,
              ArabicLetters.RaWithDamma,
              ArabicLetters.WawWithSukun,
              ArabicLetters.LetterAlif
            )
          ),
          Some(
            negationPrefix.concatWithSpace(
              ArabicWord(
                ArabicLetters.TaWithFatha,
                ArabicLetters.NoonWithSukun,
                ArabicLetters.SadWithDamma,
                ArabicLetters.RaWithFatha,
                ArabicLetters.LetterAlif
              )
            )
          )
        ),
        feminineSecondPerson = ConjugationTuple(
          negationPrefix.concatWithSpace(
            ArabicWord(
              ArabicLetters.TaWithFatha,
              ArabicLetters.NoonWithSukun,
              ArabicLetters.SadWithDamma,
              ArabicLetters.RaWithKasra,
              ArabicLetters.YaWithSukun
            )
          ),
          negationPrefix.concatWithSpace(
            ArabicWord(
              ArabicLetters.TaWithFatha,
              ArabicLetters.NoonWithSukun,
              ArabicLetters.SadWithDamma,
              ArabicLetters.RaWithSukun,
              ArabicLetters.NoonWithFatha
            )
          ),
          Some(
            negationPrefix.concatWithSpace(
              ArabicWord(
                ArabicLetters.TaWithFatha,
                ArabicLetters.NoonWithSukun,
                ArabicLetters.SadWithDamma,
                ArabicLetters.RaWithFatha,
                ArabicLetters.LetterAlif
              )
            )
          )
        )
      )
    ),
    VerbFormCase(
      "Imperative: FormICategoryAGroupITemplate",
      NamedTemplate.FormICategoryAGroupITemplate,
      ArabicLetterType.Ddad,
      ArabicLetterType.Ra,
      ArabicLetterType.Ba,
      _.imperative,
      VerbConjugationGroup(
        masculineSecondPerson = ConjugationTuple(
          ArabicWord(
            ArabicLetters.AlifHamzaBelowWithKasra,
            ArabicLetters.DdadWithSukun,
            ArabicLetters.RaWithKasra,
            ArabicLetters.BaWithSukun
          ),
          ArabicWord(
            ArabicLetters.AlifHamzaBelowWithKasra,
            ArabicLetters.DdadWithSukun,
            ArabicLetters.RaWithKasra,
            ArabicLetters.BaWithDamma,
            ArabicLetters.WawWithSukun,
            ArabicLetters.LetterAlif
          ),
          Some(
            ArabicWord(
              ArabicLetters.AlifHamzaBelowWithKasra,
              ArabicLetters.DdadWithSukun,
              ArabicLetters.RaWithKasra,
              ArabicLetters.BaWithFatha,
              ArabicLetters.LetterAlif
            )
          )
        ),
        feminineSecondPerson = ConjugationTuple(
          ArabicWord(
            ArabicLetters.AlifHamzaBelowWithKasra,
            ArabicLetters.DdadWithSukun,
            ArabicLetters.RaWithKasra,
            ArabicLetters.BaWithKasra,
            ArabicLetters.YaWithSukun
          ),
          ArabicWord(
            ArabicLetters.AlifHamzaBelowWithKasra,
            ArabicLetters.DdadWithSukun,
            ArabicLetters.RaWithKasra,
            ArabicLetters.BaWithSukun,
            ArabicLetters.NoonWithFatha
          ),
          Some(
            ArabicWord(
              ArabicLetters.AlifHamzaBelowWithKasra,
              ArabicLetters.DdadWithSukun,
              ArabicLetters.RaWithKasra,
              ArabicLetters.BaWithFatha,
              ArabicLetters.LetterAlif
            )
          )
        )
      )
    ),
    VerbFormCase(
      "Fobidden: FormICategoryAGroupITemplate",
      NamedTemplate.FormICategoryAGroupITemplate,
      ArabicLetterType.Ddad,
      ArabicLetterType.Ra,
      ArabicLetterType.Ba,
      _.forbidden,
      VerbConjugationGroup(
        masculineSecondPerson = ConjugationTuple(
          negationPrefix.concatWithSpace(
            ArabicWord(
              ArabicLetters.TaWithFatha,
              ArabicLetters.DdadWithSukun,
              ArabicLetters.RaWithKasra,
              ArabicLetters.BaWithSukun
            )
          ),
          negationPrefix.concatWithSpace(
            ArabicWord(
              ArabicLetters.TaWithFatha,
              ArabicLetters.DdadWithSukun,
              ArabicLetters.RaWithKasra,
              ArabicLetters.BaWithDamma,
              ArabicLetters.WawWithSukun,
              ArabicLetters.LetterAlif
            )
          ),
          Some(
            negationPrefix.concatWithSpace(
              ArabicWord(
                ArabicLetters.TaWithFatha,
                ArabicLetters.DdadWithSukun,
                ArabicLetters.RaWithKasra,
                ArabicLetters.BaWithFatha,
                ArabicLetters.LetterAlif
              )
            )
          )
        ),
        feminineSecondPerson = ConjugationTuple(
          negationPrefix.concatWithSpace(
            ArabicWord(
              ArabicLetters.TaWithFatha,
              ArabicLetters.DdadWithSukun,
              ArabicLetters.RaWithKasra,
              ArabicLetters.BaWithKasra,
              ArabicLetters.YaWithSukun
            )
          ),
          negationPrefix.concatWithSpace(
            ArabicWord(
              ArabicLetters.TaWithFatha,
              ArabicLetters.DdadWithSukun,
              ArabicLetters.RaWithKasra,
              ArabicLetters.BaWithSukun,
              ArabicLetters.NoonWithFatha
            )
          ),
          Some(
            negationPrefix.concatWithSpace(
              ArabicWord(
                ArabicLetters.TaWithFatha,
                ArabicLetters.DdadWithSukun,
                ArabicLetters.RaWithKasra,
                ArabicLetters.BaWithFatha,
                ArabicLetters.LetterAlif
              )
            )
          )
        )
      )
    ),
    VerbFormCase(
      "Imperative: FormICategoryAGroupATemplate",
      NamedTemplate.FormICategoryAGroupATemplate,
      ArabicLetterType.Fa,
      ArabicLetterType.Ta,
      ArabicLetterType.Hha,
      _.imperative,
      VerbConjugationGroup(
        masculineSecondPerson = ConjugationTuple(
          ArabicWord(
            ArabicLetters.AlifHamzaBelowWithKasra,
            ArabicLetters.FaWithSukun,
            ArabicLetters.TaWithFatha,
            ArabicLetters.HhaWithSukun
          ),
          ArabicWord(
            ArabicLetters.AlifHamzaBelowWithKasra,
            ArabicLetters.FaWithSukun,
            ArabicLetters.TaWithFatha,
            ArabicLetters.HhaWithDamma,
            ArabicLetters.WawWithSukun,
            ArabicLetters.LetterAlif
          ),
          Some(
            ArabicWord(
              ArabicLetters.AlifHamzaBelowWithKasra,
              ArabicLetters.FaWithSukun,
              ArabicLetters.TaWithFatha,
              ArabicLetters.HhaWithFatha,
              ArabicLetters.LetterAlif
            )
          )
        ),
        feminineSecondPerson = ConjugationTuple(
          ArabicWord(
            ArabicLetters.AlifHamzaBelowWithKasra,
            ArabicLetters.FaWithSukun,
            ArabicLetters.TaWithFatha,
            ArabicLetters.HhaWithKasra,
            ArabicLetters.YaWithSukun
          ),
          ArabicWord(
            ArabicLetters.AlifHamzaBelowWithKasra,
            ArabicLetters.FaWithSukun,
            ArabicLetters.TaWithFatha,
            ArabicLetters.HhaWithSukun,
            ArabicLetters.NoonWithFatha
          ),
          Some(
            ArabicWord(
              ArabicLetters.AlifHamzaBelowWithKasra,
              ArabicLetters.FaWithSukun,
              ArabicLetters.TaWithFatha,
              ArabicLetters.HhaWithFatha,
              ArabicLetters.LetterAlif
            )
          )
        )
      )
    ),
    VerbFormCase(
      "Fobidden: FormICategoryAGroupATemplate",
      NamedTemplate.FormICategoryAGroupATemplate,
      ArabicLetterType.Fa,
      ArabicLetterType.Ta,
      ArabicLetterType.Hha,
      _.forbidden,
      VerbConjugationGroup(
        masculineSecondPerson = ConjugationTuple(
          negationPrefix.concatWithSpace(
            ArabicWord(
              ArabicLetters.TaWithFatha,
              ArabicLetters.FaWithSukun,
              ArabicLetters.TaWithFatha,
              ArabicLetters.HhaWithSukun
            )
          ),
          negationPrefix.concatWithSpace(
            ArabicWord(
              ArabicLetters.TaWithFatha,
              ArabicLetters.FaWithSukun,
              ArabicLetters.TaWithFatha,
              ArabicLetters.HhaWithDamma,
              ArabicLetters.WawWithSukun,
              ArabicLetters.LetterAlif
            )
          ),
          Some(
            negationPrefix.concatWithSpace(
              ArabicWord(
                ArabicLetters.TaWithFatha,
                ArabicLetters.FaWithSukun,
                ArabicLetters.TaWithFatha,
                ArabicLetters.HhaWithFatha,
                ArabicLetters.LetterAlif
              )
            )
          )
        ),
        feminineSecondPerson = ConjugationTuple(
          negationPrefix.concatWithSpace(
            ArabicWord(
              ArabicLetters.TaWithFatha,
              ArabicLetters.FaWithSukun,
              ArabicLetters.TaWithFatha,
              ArabicLetters.HhaWithKasra,
              ArabicLetters.YaWithSukun
            )
          ),
          negationPrefix.concatWithSpace(
            ArabicWord(
              ArabicLetters.TaWithFatha,
              ArabicLetters.FaWithSukun,
              ArabicLetters.TaWithFatha,
              ArabicLetters.HhaWithSukun,
              ArabicLetters.NoonWithFatha
            )
          ),
          Some(
            negationPrefix.concatWithSpace(
              ArabicWord(
                ArabicLetters.TaWithFatha,
                ArabicLetters.FaWithSukun,
                ArabicLetters.TaWithFatha,
                ArabicLetters.HhaWithFatha,
                ArabicLetters.LetterAlif
              )
            )
          )
        )
      )
    ),
    VerbFormCase(
      "Imperative: FormIV",
      NamedTemplate.FormIVTemplate,
      ArabicLetterType.Seen,
      ArabicLetterType.Lam,
      ArabicLetterType.Meem,
      _.imperative,
      VerbConjugationGroup(
        masculineSecondPerson = ConjugationTuple(
          ArabicWord(
            ArabicLetters.AlifHamzaAboveWithFatha,
            ArabicLetters.SeenWithSukun,
            ArabicLetters.LamWithKasra,
            ArabicLetters.MeemWithSukun
          ),
          ArabicWord(
            ArabicLetters.AlifHamzaAboveWithFatha,
            ArabicLetters.SeenWithSukun,
            ArabicLetters.LamWithKasra,
            ArabicLetters.MeemWithDamma,
            ArabicLetters.WawWithSukun,
            ArabicLetters.LetterAlif
          ),
          Some(
            ArabicWord(
              ArabicLetters.AlifHamzaAboveWithFatha,
              ArabicLetters.SeenWithSukun,
              ArabicLetters.LamWithKasra,
              ArabicLetters.MeemWithFatha,
              ArabicLetters.LetterAlif
            )
          )
        ),
        feminineSecondPerson = ConjugationTuple(
          ArabicWord(
            ArabicLetters.AlifHamzaAboveWithFatha,
            ArabicLetters.SeenWithSukun,
            ArabicLetters.LamWithKasra,
            ArabicLetters.MeemWithKasra,
            ArabicLetters.YaWithSukun
          ),
          ArabicWord(
            ArabicLetters.AlifHamzaAboveWithFatha,
            ArabicLetters.SeenWithSukun,
            ArabicLetters.LamWithKasra,
            ArabicLetters.MeemWithSukun,
            ArabicLetters.NoonWithFatha
          ),
          Some(
            ArabicWord(
              ArabicLetters.AlifHamzaAboveWithFatha,
              ArabicLetters.SeenWithSukun,
              ArabicLetters.LamWithKasra,
              ArabicLetters.MeemWithFatha,
              ArabicLetters.LetterAlif
            )
          )
        )
      )
    ),
    VerbFormCase(
      "Imperative: FormICategoryAGroupITemplate: first radical week",
      NamedTemplate.FormICategoryAGroupITemplate,
      ArabicLetterType.Waw,
      ArabicLetterType.Ain,
      ArabicLetterType.Dal,
      _.imperative,
      VerbConjugationGroup(
        masculineSecondPerson = ConjugationTuple(
          ArabicWord(ArabicLetters.AinWithKasra, ArabicLetters.DalWithSukun),
          ArabicWord(
            ArabicLetters.AinWithKasra,
            ArabicLetters.DalWithDamma,
            ArabicLetters.WawWithSukun,
            ArabicLetters.LetterAlif
          ),
          Some(ArabicWord(ArabicLetters.AinWithKasra, ArabicLetters.DalWithFatha, ArabicLetters.LetterAlif))
        ),
        feminineSecondPerson = ConjugationTuple(
          ArabicWord(ArabicLetters.AinWithKasra, ArabicLetters.DalWithKasra, ArabicLetters.YaWithSukun),
          ArabicWord(ArabicLetters.AinWithKasra, ArabicLetters.DalWithSukun, ArabicLetters.NoonWithFatha),
          Some(ArabicWord(ArabicLetters.AinWithKasra, ArabicLetters.DalWithFatha, ArabicLetters.LetterAlif))
        )
      )
    )
  )

  verbFormCases.foreach { c =>
    test(c.name) {
      val processingContext =
        ProcessingContext(c.namedTemplate, OutputFormat.Unicode, c.firstRadical, c.secondRadical, c.thirdRadical)
      val obtained = c
        .selector(Form.fromNamedTemplate(c.namedTemplate))
        .transform(defaultRuleProcessor, processingContext)
      assertEquals(obtained, c.expected)
    }
  }

  test("VerbalNoun conjugation") {
    val processingContext =
      ProcessingContext(
        NamedTemplate.FormIITemplate,
        OutputFormat.Unicode,
        ArabicLetterType.Ain,
        ArabicLetterType.Lam,
        ArabicLetterType.Meem
      )

    val obtained = Form
      .fromNamedTemplate(processingContext.namedTemplate)
      .verbalNouns
      .head
      .transform(defaultRuleProcessor, processingContext)

    val expected = NounConjugationGroup(
      nominative = ConjugationTuple("تَعْلِيْمٌ", "تَعْلِيْمَاتٌ", Some("تَعْلِيْمَانِ")),
      accusative = ConjugationTuple("تَعْلِيْمًا", "تَعْلِيْمَاتٍ", Some("تَعْلِيْمَيْنِ")),
      genitive = ConjugationTuple("تَعْلِيْمٍ", "تَعْلِيْمَاتٍ", Some("تَعْلِيْمَيْنِ"))
    )

    assertEquals(obtained, expected)
  }

  /** Case describing a single [[Transformer]] invocation validated via [[validateTransformer]]. */
  private case class TransformerCase(
    name: String,
    transformer: Transformer,
    rootWordSupport: RootWordSupport[?, ?],
    namedTemplate: NamedTemplate,
    firstRadical: ArabicLetterType,
    secondRadical: ArabicLetterType,
    thirdRadical: ArabicLetterType,
    expected: ConjugationTuple,
    expectedDefaultValue: String,
    jussiveParticle: Option[JussiveParticle] = None)

  private val transformerCases = Seq(
    TransformerCase(
      "MasculineNominativeTransformer",
      MasculineNominativeTransformer(),
      noun.FormIV.MasculineActiveParticiple,
      NamedTemplate.FormIVTemplate,
      ArabicLetterType.Seen,
      ArabicLetterType.Lam,
      ArabicLetterType.Meem,
      ConjugationTuple("مُسْلِمٌ", "مُسْلِمُوْنَ", Some("مُسْلِمَانِ")),
      "مُسْلِمٌ"
    ),
    TransformerCase(
      "MasculineAccusativeTransformer",
      MasculineAccusativeTransformer(),
      noun.FormII.MasculinePassiveParticiple,
      NamedTemplate.FormIITemplate,
      ArabicLetterType.Ain,
      ArabicLetterType.Lam,
      ArabicLetterType.Meem,
      ConjugationTuple("مُعَلَّمًا", "مُعَلَّمِيْنَ", Some("مُعَلَّمَيْنِ")),
      "مُعَلَّمٌ"
    ),
    TransformerCase(
      "MasculineGenitiveTransformer",
      MasculineGenitiveTransformer(),
      noun.FormX.MasculineActiveParticiple,
      NamedTemplate.FormXTemplate,
      ArabicLetterType.Ghain,
      ArabicLetterType.Fa,
      ArabicLetterType.Ra,
      ConjugationTuple("مُسْتَغْفِرٍ", "مُسْتَغْفِرِيْنَ", Some("مُسْتَغْفِرَيْنِ")),
      "مُسْتَغْفِرٌ"
    ),
    TransformerCase(
      "FeminineNominativeTransformer: from masculine word",
      FeminineNominativeTransformer(),
      noun.FormVIII.MasculinePassiveParticiple,
      NamedTemplate.FormVIIITemplate,
      ArabicLetterType.Qaf,
      ArabicLetterType.Ra,
      ArabicLetterType.Ba,
      ConjugationTuple("مُقْتَرَبَةٌ", "مُقْتَرَبَاتٌ", Some("مُقْتَرَبَتَانِ")),
      "مُقْتَرَبٌ"
    ),
    TransformerCase(
      "FeminineNominativeTransformer: from feminine word",
      FeminineNominativeTransformer(),
      noun.FormVIII.FeminineActiveParticiple,
      NamedTemplate.FormVIIITemplate,
      ArabicLetterType.Qaf,
      ArabicLetterType.Ra,
      ArabicLetterType.Ba,
      ConjugationTuple("مُقْتَرِبَةٌ", "مُقْتَرِبَاتٌ", Some("مُقْتَرِبَتَانِ")),
      "مُقْتَرِبَةٌ"
    ),
    TransformerCase(
      "FeminineAccusativeTransformer: from masculine word",
      FeminineAccusativeTransformer(),
      noun.FormIII.MasculineActiveParticiple,
      NamedTemplate.FormIIITemplate,
      ArabicLetterType.Jeem,
      ArabicLetterType.Ha,
      ArabicLetterType.Dal,
      ConjugationTuple("مُجَاهِدَةً", "مُجَاهِدَاتٍ", Some("مُجَاهِدَتَيْنِ")),
      "مُجَاهِدٌ"
    ),
    TransformerCase(
      "FeminineAccusativeTransformer: from feminine word",
      FeminineAccusativeTransformer(),
      noun.FormIII.FemininePassiveParticiple,
      NamedTemplate.FormIIITemplate,
      ArabicLetterType.Jeem,
      ArabicLetterType.Ha,
      ArabicLetterType.Dal,
      ConjugationTuple("مُجَاهَدَةً", "مُجَاهَدَاتٍ", Some("مُجَاهَدَتَيْنِ")),
      "مُجَاهَدَةٌ"
    ),
    TransformerCase(
      "FeminineGenitiveTransformer: from masculine word",
      FeminineGenitiveTransformer(),
      noun.FormV.MasculinePassiveParticiple,
      NamedTemplate.FormVTemplate,
      ArabicLetterType.Ain,
      ArabicLetterType.Lam,
      ArabicLetterType.Meem,
      ConjugationTuple("مُتَعَلَّمَةٍ", "مُتَعَلَّمَاتٍ", Some("مُتَعَلَّمَتَيْنِ")),
      "مُتَعَلَّمٌ"
    ),
    TransformerCase(
      "FeminineGenitiveTransformer: from feminine word",
      FeminineGenitiveTransformer(),
      noun.FormVI.FemininePassiveParticiple,
      NamedTemplate.FormVITemplate,
      ArabicLetterType.Ain,
      ArabicLetterType.Ra,
      ArabicLetterType.Fa,
      ConjugationTuple("مُتَعَارَفَةٍ", "مُتَعَارَفَاتٍ", Some("مُتَعَارَفَتَيْنِ")),
      "مُتَعَارَفَةٌ"
    ),
    TransformerCase(
      "VerbalNoun: masculine based",
      MasculineNominativeTransformer(pluralType = PluralType.Feminine),
      noun.VerbalNoun.FormIV,
      NamedTemplate.FormIVTemplate,
      ArabicLetterType.Seen,
      ArabicLetterType.Lam,
      ArabicLetterType.Meem,
      ConjugationTuple("إِسْلَامٌ", "إِسْلَامَاتٌ", Some("إِسْلَامَانِ")),
      "إِسْلَامًا"
    ),
    TransformerCase(
      "VerbalNoun: feminine based",
      FeminineAccusativeTransformer(),
      noun.VerbalNoun.FormIIIV2,
      NamedTemplate.FormIIITemplate,
      ArabicLetterType.Jeem,
      ArabicLetterType.Ha,
      ArabicLetterType.Dal,
      ConjugationTuple("مُجَاهِدَةً", "مُجَاهِدَاتٍ", Some("مُجَاهِدَتَيْنِ")),
      "مُجَاهِدَةً"
    ),
    TransformerCase(
      "PastTenseTransformer: ThirdPersonMasculine",
      PastTenseTransformer(VerbGroupType.ThirdPersonMasculine),
      verb.FormI.PastTenseV1,
      NamedTemplate.FormICategoryAGroupUTemplate,
      ArabicLetterType.Noon,
      ArabicLetterType.Sad,
      ArabicLetterType.Ra,
      ConjugationTuple("نَصَرَ", "نَصَرُوْا", Some("نَصَرَا")),
      "نَصَرَ"
    ),
    TransformerCase(
      "PastTenseTransformer: ThirdPersonFeminine",
      PastTenseTransformer(VerbGroupType.ThirdPersonFeminine),
      verb.FormI.PastPassiveTense,
      NamedTemplate.FormICategoryAGroupUTemplate,
      ArabicLetterType.Noon,
      ArabicLetterType.Sad,
      ArabicLetterType.Ra,
      ConjugationTuple("نُصِرَتْ", "نُصِرْنَ", Some("نُصِرَتَا")),
      "نُصِرَ"
    ),
    TransformerCase(
      "PastTenseTransformer: SecondPersonMasculine",
      PastTenseTransformer(VerbGroupType.SecondPersonMasculine),
      verb.FormI.PastTenseV1,
      NamedTemplate.FormICategoryAGroupITemplate,
      ArabicLetterType.Ddad,
      ArabicLetterType.Ra,
      ArabicLetterType.Ba,
      ConjugationTuple("ضَرَبْتَ", "ضَرَبْتُمْ", Some("ضَرَبْتُمَا")),
      "ضَرَبَ"
    ),
    TransformerCase(
      "PastTenseTransformer: SecondPersonFeminine",
      PastTenseTransformer(VerbGroupType.SecondPersonFeminine),
      verb.FormI.PastPassiveTense,
      NamedTemplate.FormICategoryAGroupITemplate,
      ArabicLetterType.Ddad,
      ArabicLetterType.Ra,
      ArabicLetterType.Ba,
      ConjugationTuple("ضُرِبْتِ", "ضُرِبْتُنَّ", Some("ضُرِبْتُمَا")),
      "ضُرِبَ"
    ),
    TransformerCase(
      "PastTenseTransformer: FirstPerson",
      PastTenseTransformer(VerbGroupType.FirstPerson),
      verb.FormI.PastPassiveTense,
      NamedTemplate.FormICategoryAGroupITemplate,
      ArabicLetterType.Ddad,
      ArabicLetterType.Ra,
      ArabicLetterType.Ba,
      ConjugationTuple("ضُرِبْتُ", "ضُرِبْنَا", None),
      "ضُرِبَ"
    ),
    TransformerCase(
      "PresentTenseTransformer: ThirdPersonMasculine",
      PresentTenseTransformer(VerbGroupType.ThirdPersonMasculine),
      verb.FormVII.PresentTense,
      NamedTemplate.FormVIITemplate,
      ArabicLetterType.Kaf,
      ArabicLetterType.Seen,
      ArabicLetterType.Ra,
      ConjugationTuple("يَنْكَسِرُ", "يَنْكَسِرُوْنَ", Some("يَنْكَسِرَانِ")),
      "يَنْكَسِرُ"
    ),
    TransformerCase(
      "PresentTenseTransformer: ThirdPersonFeminine",
      PresentTenseTransformer(VerbGroupType.ThirdPersonFeminine),
      verb.FormVIII.PresentTense,
      NamedTemplate.FormVIIITemplate,
      ArabicLetterType.Qaf,
      ArabicLetterType.Ra,
      ArabicLetterType.Ba,
      ConjugationTuple("تَقْتَرِبُ", "يَقْتَرِبْنَ", Some("تَقْتَرِبَانِ")),
      "يَقْتَرِبُ"
    ),
    TransformerCase(
      "PresentTenseTransformer: SecondPersonMasculine",
      PresentTenseTransformer(VerbGroupType.SecondPersonMasculine),
      verb.FormVIII.PresentPassiveTense,
      NamedTemplate.FormVIIITemplate,
      ArabicLetterType.Qaf,
      ArabicLetterType.Ra,
      ArabicLetterType.Ba,
      ConjugationTuple("تُقْتَرَبُ", "تُقْتَرَبُوْنَ", Some("تُقْتَرَبَانِ")),
      "يُقْتَرَبُ"
    ),
    TransformerCase(
      "PresentTenseTransformer: SecondPersonFeminine",
      PresentTenseTransformer(VerbGroupType.SecondPersonFeminine),
      verb.FormX.PresentTense,
      NamedTemplate.FormXTemplate,
      ArabicLetterType.Ghain,
      ArabicLetterType.Fa,
      ArabicLetterType.Ra,
      ConjugationTuple("تَسْتَغْفِرِيْنَ", "تَسْتَغْفِرْنَ", Some("تَسْتَغْفِرَانِ")),
      "يَسْتَغْفِرُ"
    ),
    TransformerCase(
      "PresentTenseTransformer: FirstPerson",
      PresentTenseTransformer(VerbGroupType.FirstPerson),
      verb.FormX.PresentPassiveTense,
      NamedTemplate.FormXTemplate,
      ArabicLetterType.Ghain,
      ArabicLetterType.Fa,
      ArabicLetterType.Ra,
      ConjugationTuple("أُسْتَغْفَرُ", "نُسْتَغْفَرُ", None),
      "يُسْتَغْفَرُ"
    ),
    TransformerCase(
      "PresentTenseJussiveModeTransformer: ThirdPersonMasculine",
      PresentTenseJussiveModeTransformer(VerbGroupType.ThirdPersonMasculine),
      verb.FormI.PresentTenseJussiveModeV1,
      NamedTemplate.FormICategoryAGroupUTemplate,
      ArabicLetterType.Noon,
      ArabicLetterType.Sad,
      ArabicLetterType.Ra,
      ConjugationTuple("لَمْ يَنْصُرْ", "لَمْ يَنْصُرُوْا", Some("لَمْ يَنْصُرَا")),
      "لَمْ يَنْصُرْ",
      jussiveParticle = Some(JussiveParticle.DidNot)
    ),
    TransformerCase(
      "PresentTenseJussiveModeTransformer: ThirdPersonFeminine",
      PresentTenseJussiveModeTransformer(VerbGroupType.ThirdPersonFeminine),
      verb.FormI.PresentTenseJussiveModeV2,
      NamedTemplate.FormICategoryAGroupITemplate,
      ArabicLetterType.Ddad,
      ArabicLetterType.Ra,
      ArabicLetterType.Ba,
      ConjugationTuple("لَمَّا تَضْرِبْ", "لَمَّا يَضْرِبْنَ", Some("لَمَّا تَضْرِبَا")),
      "لَمَّا يَضْرِبْ",
      jussiveParticle = Some(JussiveParticle.NotYet)
    ),
    TransformerCase(
      "PresentTenseJussiveModeTransformer: SecondPersonMasculine",
      PresentTenseJussiveModeTransformer(VerbGroupType.SecondPersonMasculine),
      verb.FormI.PresentTenseJussiveModeV3,
      NamedTemplate.FormICategoryAGroupATemplate,
      ArabicLetterType.Fa,
      ArabicLetterType.Ta,
      ArabicLetterType.Hha,
      ConjugationTuple("إِفْتَحْ", "إِفْتَحُوْا", Some("إِفْتَحَا")),
      "لِيَفْتَحْ",
      jussiveParticle = Some(JussiveParticle.LamOfCommand)
    ),
    TransformerCase(
      "PresentTenseJussiveModeTransformer: SecondPersonFeminine",
      PresentTenseJussiveModeTransformer(VerbGroupType.SecondPersonFeminine),
      verb.FormI.PresentTenseJussiveModeV1,
      NamedTemplate.FormICategoryAGroupUTemplate,
      ArabicLetterType.Noon,
      ArabicLetterType.Sad,
      ArabicLetterType.Ra,
      ConjugationTuple("لَاتَنْصُرِيْ", "لَاتَنْصُرْنَ", Some("لَاتَنْصُرَا")),
      "لَايَنْصُرْ",
      jussiveParticle = Some(JussiveParticle.LamOfProhibition)
    ),
    TransformerCase(
      "PresentTenseJussiveModeTransformer: FirstPerson",
      PresentTenseJussiveModeTransformer(VerbGroupType.FirstPerson),
      verb.FormI.PresentTenseJussiveModeV2,
      NamedTemplate.FormICategoryAGroupITemplate,
      ArabicLetterType.Ddad,
      ArabicLetterType.Ra,
      ArabicLetterType.Ba,
      ConjugationTuple("لَمْ أَضْرِبْ", "لَمْ نَضْرِبْ", None),
      "لَمْ يَضْرِبْ",
      jussiveParticle = Some(JussiveParticle.DidNot)
    ),
    TransformerCase(
      "PresentTenseJussiveModeTransformer: PresentPassiveTenseJussiveMode: ThirdPersonMasculine",
      PresentTenseJussiveModeTransformer(VerbGroupType.ThirdPersonMasculine),
      verb.FormI.PresentPassiveTenseJussiveMode,
      NamedTemplate.FormICategoryAGroupUTemplate,
      ArabicLetterType.Noon,
      ArabicLetterType.Sad,
      ArabicLetterType.Ra,
      ConjugationTuple("لَمَّا يُنْصَرْ", "لَمَّا يُنْصَرُوْا", Some("لَمَّا يُنْصَرَا")),
      "لَمَّا يُنْصَرْ",
      jussiveParticle = Some(JussiveParticle.NotYet)
    ),
    TransformerCase(
      "PresentTenseJussiveModeTransformer: PresentPassiveTenseJussiveMode: ThirdPersonFeminine",
      PresentTenseJussiveModeTransformer(VerbGroupType.ThirdPersonFeminine),
      verb.FormI.PresentPassiveTenseJussiveMode,
      NamedTemplate.FormICategoryAGroupUTemplate,
      ArabicLetterType.Noon,
      ArabicLetterType.Sad,
      ArabicLetterType.Ra,
      ConjugationTuple("لِتُنْصَرْ", "لِيُنْصَرْنَ", Some("لِتُنْصَرَا")),
      "لِيُنْصَرْ",
      jussiveParticle = Some(JussiveParticle.LamOfCommand)
    ),
    TransformerCase(
      "PresentTenseJussiveModeTransformer: PresentPassiveTenseJussiveMode: SecondPersonMasculine",
      PresentTenseJussiveModeTransformer(VerbGroupType.SecondPersonMasculine),
      verb.FormI.PresentPassiveTenseJussiveMode,
      NamedTemplate.FormICategoryAGroupUTemplate,
      ArabicLetterType.Noon,
      ArabicLetterType.Sad,
      ArabicLetterType.Ra,
      ConjugationTuple("لَاتُنْصَرْ", "لَاتُنْصَرُوْا", Some("لَاتُنْصَرَا")),
      "لَايُنْصَرْ",
      jussiveParticle = Some(JussiveParticle.LamOfProhibition)
    ),
    TransformerCase(
      "PresentTenseJussiveModeTransformer: PresentPassiveTenseJussiveMode: SecondPersonFeminine",
      PresentTenseJussiveModeTransformer(VerbGroupType.SecondPersonFeminine),
      verb.FormI.PresentPassiveTenseJussiveMode,
      NamedTemplate.FormICategoryAGroupUTemplate,
      ArabicLetterType.Noon,
      ArabicLetterType.Sad,
      ArabicLetterType.Ra,
      ConjugationTuple("لَمْ تُنْصَرِيْ", "لَمْ تُنْصَرْنَ", Some("لَمْ تُنْصَرَا")),
      "لَمْ يُنْصَرْ",
      jussiveParticle = Some(JussiveParticle.DidNot)
    ),
    TransformerCase(
      "PresentTenseJussiveModeTransformer: PresentPassiveTenseJussiveMode: FirstPerson",
      PresentTenseJussiveModeTransformer(VerbGroupType.FirstPerson),
      verb.FormI.PresentPassiveTenseJussiveMode,
      NamedTemplate.FormICategoryAGroupUTemplate,
      ArabicLetterType.Noon,
      ArabicLetterType.Sad,
      ArabicLetterType.Ra,
      ConjugationTuple("لَمَّا أُنْصَرْ", "لَمَّا نُنْصَرْ", None),
      "لَمَّا يُنْصَرْ",
      jussiveParticle = Some(JussiveParticle.NotYet)
    ),
    TransformerCase(
      "PresentTenseJussiveModeTransformer: FormII: ThirdPersonMasculine",
      PresentTenseJussiveModeTransformer(VerbGroupType.ThirdPersonMasculine),
      verb.FormII.PresentTenseJussiveMode,
      NamedTemplate.FormIITemplate,
      ArabicLetterType.Ain,
      ArabicLetterType.Lam,
      ArabicLetterType.Meem,
      ConjugationTuple("لِيُعَلِّمْ", "لِيُعَلِّمُوْا", Some("لِيُعَلِّمَا")),
      "لِيُعَلِّمْ",
      jussiveParticle = Some(JussiveParticle.LamOfCommand)
    ),
    TransformerCase(
      "PresentTenseJussiveModeTransformer: FormII: SecondPersonFeminine",
      PresentTenseJussiveModeTransformer(VerbGroupType.SecondPersonFeminine),
      verb.FormII.PresentTenseJussiveMode,
      NamedTemplate.FormIITemplate,
      ArabicLetterType.Ain,
      ArabicLetterType.Lam,
      ArabicLetterType.Meem,
      ConjugationTuple("لَاتُعَلِّمِيْ", "لَاتُعَلِّمْنَ", Some("لَاتُعَلِّمَا")),
      "لَايُعَلِّمْ",
      jussiveParticle = Some(JussiveParticle.LamOfProhibition)
    ),
    TransformerCase(
      "PresentTenseJussiveModeTransformer: FormII: PresentPassiveTenseJussiveMode: ThirdPersonMasculine",
      PresentTenseJussiveModeTransformer(VerbGroupType.ThirdPersonMasculine),
      verb.FormII.PresentPassiveTenseJussiveMode,
      NamedTemplate.FormIITemplate,
      ArabicLetterType.Ain,
      ArabicLetterType.Lam,
      ArabicLetterType.Meem,
      ConjugationTuple("لَمْ يُعَلَّمْ", "لَمْ يُعَلَّمُوْا", Some("لَمْ يُعَلَّمَا")),
      "لَمْ يُعَلَّمْ",
      jussiveParticle = Some(JussiveParticle.DidNot)
    ),
    TransformerCase(
      "PresentTenseJussiveModeTransformer: FormIII: ThirdPersonMasculine",
      PresentTenseJussiveModeTransformer(VerbGroupType.ThirdPersonMasculine),
      verb.FormIII.PresentTenseJussiveMode,
      NamedTemplate.FormIIITemplate,
      ArabicLetterType.Jeem,
      ArabicLetterType.Ha,
      ArabicLetterType.Dal,
      ConjugationTuple("لَمَّا يُجَاهِدْ", "لَمَّا يُجَاهِدُوْا", Some("لَمَّا يُجَاهِدَا")),
      "لَمَّا يُجَاهِدْ",
      jussiveParticle = Some(JussiveParticle.NotYet)
    ),
    TransformerCase(
      "PresentTenseJussiveModeTransformer: FormIII: SecondPersonFeminine",
      PresentTenseJussiveModeTransformer(VerbGroupType.SecondPersonFeminine),
      verb.FormIII.PresentTenseJussiveMode,
      NamedTemplate.FormIIITemplate,
      ArabicLetterType.Jeem,
      ArabicLetterType.Ha,
      ArabicLetterType.Dal,
      ConjugationTuple("جَاهِدِيْ", "جَاهِدْنَ", Some("جَاهِدَا")),
      "لِيُجَاهِدْ",
      jussiveParticle = Some(JussiveParticle.LamOfCommand)
    ),
    TransformerCase(
      "PresentTenseJussiveModeTransformer: FormIII: PresentPassiveTenseJussiveMode: ThirdPersonMasculine",
      PresentTenseJussiveModeTransformer(VerbGroupType.ThirdPersonMasculine),
      verb.FormIII.PresentPassiveTenseJussiveMode,
      NamedTemplate.FormIIITemplate,
      ArabicLetterType.Jeem,
      ArabicLetterType.Ha,
      ArabicLetterType.Dal,
      ConjugationTuple("لَايُجَاهَدْ", "لَايُجَاهَدُوْا", Some("لَايُجَاهَدَا")),
      "لَايُجَاهَدْ",
      jussiveParticle = Some(JussiveParticle.LamOfProhibition)
    ),
    TransformerCase(
      "PresentTenseJussiveModeTransformer: FormIV: ThirdPersonMasculine",
      PresentTenseJussiveModeTransformer(VerbGroupType.ThirdPersonMasculine),
      verb.FormIV.PresentTenseJussiveMode,
      NamedTemplate.FormIVTemplate,
      ArabicLetterType.Seen,
      ArabicLetterType.Lam,
      ArabicLetterType.Meem,
      ConjugationTuple("لَمْ يُسْلِمْ", "لَمْ يُسْلِمُوْا", Some("لَمْ يُسْلِمَا")),
      "لَمْ يُسْلِمْ",
      jussiveParticle = Some(JussiveParticle.DidNot)
    ),
    TransformerCase(
      "PresentTenseJussiveModeTransformer: FormIV: SecondPersonFeminine",
      PresentTenseJussiveModeTransformer(VerbGroupType.SecondPersonFeminine),
      verb.FormIV.PresentTenseJussiveMode,
      NamedTemplate.FormIVTemplate,
      ArabicLetterType.Seen,
      ArabicLetterType.Lam,
      ArabicLetterType.Meem,
      ConjugationTuple("لَمَّا تُسْلِمِيْ", "لَمَّا تُسْلِمْنَ", Some("لَمَّا تُسْلِمَا")),
      "لَمَّا يُسْلِمْ",
      jussiveParticle = Some(JussiveParticle.NotYet)
    ),
    TransformerCase(
      "PresentTenseJussiveModeTransformer: FormIV: PresentPassiveTenseJussiveMode: ThirdPersonMasculine",
      PresentTenseJussiveModeTransformer(VerbGroupType.ThirdPersonMasculine),
      verb.FormIV.PresentPassiveTenseJussiveMode,
      NamedTemplate.FormIVTemplate,
      ArabicLetterType.Seen,
      ArabicLetterType.Lam,
      ArabicLetterType.Meem,
      ConjugationTuple("لِيُسْلَمْ", "لِيُسْلَمُوْا", Some("لِيُسْلَمَا")),
      "لِيُسْلَمْ",
      jussiveParticle = Some(JussiveParticle.LamOfCommand)
    ),
    TransformerCase(
      "PresentTenseJussiveModeTransformer: FormV: ThirdPersonMasculine",
      PresentTenseJussiveModeTransformer(VerbGroupType.ThirdPersonMasculine),
      verb.FormV.PresentTenseJussiveMode,
      NamedTemplate.FormVTemplate,
      ArabicLetterType.Ain,
      ArabicLetterType.Lam,
      ArabicLetterType.Meem,
      ConjugationTuple("لَايَتَعَلَّمْ", "لَايَتَعَلَّمُوْا", Some("لَايَتَعَلَّمَا")),
      "لَايَتَعَلَّمْ",
      jussiveParticle = Some(JussiveParticle.LamOfProhibition)
    ),
    TransformerCase(
      "PresentTenseJussiveModeTransformer: FormV: SecondPersonFeminine",
      PresentTenseJussiveModeTransformer(VerbGroupType.SecondPersonFeminine),
      verb.FormV.PresentTenseJussiveMode,
      NamedTemplate.FormVTemplate,
      ArabicLetterType.Ain,
      ArabicLetterType.Lam,
      ArabicLetterType.Meem,
      ConjugationTuple("لَمْ تَتَعَلَّمِيْ", "لَمْ تَتَعَلَّمْنَ", Some("لَمْ تَتَعَلَّمَا")),
      "لَمْ يَتَعَلَّمْ",
      jussiveParticle = Some(JussiveParticle.DidNot)
    ),
    TransformerCase(
      "PresentTenseJussiveModeTransformer: FormV: PresentPassiveTenseJussiveMode: ThirdPersonMasculine",
      PresentTenseJussiveModeTransformer(VerbGroupType.ThirdPersonMasculine),
      verb.FormV.PresentPassiveTenseJussiveMode,
      NamedTemplate.FormVTemplate,
      ArabicLetterType.Ain,
      ArabicLetterType.Lam,
      ArabicLetterType.Meem,
      ConjugationTuple("لَمَّا يُتَعَلَّمْ", "لَمَّا يُتَعَلَّمُوْا", Some("لَمَّا يُتَعَلَّمَا")),
      "لَمَّا يُتَعَلَّمْ",
      jussiveParticle = Some(JussiveParticle.NotYet)
    ),
    TransformerCase(
      "PresentTenseJussiveModeTransformer: FormVI: ThirdPersonMasculine",
      PresentTenseJussiveModeTransformer(VerbGroupType.ThirdPersonMasculine),
      verb.FormVI.PresentTenseJussiveMode,
      NamedTemplate.FormVITemplate,
      ArabicLetterType.Ain,
      ArabicLetterType.Ra,
      ArabicLetterType.Fa,
      ConjugationTuple("لِيَتَعَارَفْ", "لِيَتَعَارَفُوْا", Some("لِيَتَعَارَفَا")),
      "لِيَتَعَارَفْ",
      jussiveParticle = Some(JussiveParticle.LamOfCommand)
    ),
    TransformerCase(
      "PresentTenseJussiveModeTransformer: FormVI: SecondPersonFeminine",
      PresentTenseJussiveModeTransformer(VerbGroupType.SecondPersonFeminine),
      verb.FormVI.PresentTenseJussiveMode,
      NamedTemplate.FormVITemplate,
      ArabicLetterType.Ain,
      ArabicLetterType.Ra,
      ArabicLetterType.Fa,
      ConjugationTuple("لَاتَتَعَارَفِيْ", "لَاتَتَعَارَفْنَ", Some("لَاتَتَعَارَفَا")),
      "لَايَتَعَارَفْ",
      jussiveParticle = Some(JussiveParticle.LamOfProhibition)
    ),
    TransformerCase(
      "PresentTenseJussiveModeTransformer: FormVI: PresentPassiveTenseJussiveMode: ThirdPersonMasculine",
      PresentTenseJussiveModeTransformer(VerbGroupType.ThirdPersonMasculine),
      verb.FormVI.PresentPassiveTenseJussiveMode,
      NamedTemplate.FormVITemplate,
      ArabicLetterType.Ain,
      ArabicLetterType.Ra,
      ArabicLetterType.Fa,
      ConjugationTuple("لَمْ يُتَعَارَفْ", "لَمْ يُتَعَارَفُوْا", Some("لَمْ يُتَعَارَفَا")),
      "لَمْ يُتَعَارَفْ",
      jussiveParticle = Some(JussiveParticle.DidNot)
    ),
    TransformerCase(
      "PresentTenseJussiveModeTransformer: FormVII: ThirdPersonMasculine",
      PresentTenseJussiveModeTransformer(VerbGroupType.ThirdPersonMasculine),
      verb.FormVII.PresentTenseJussiveMode,
      NamedTemplate.FormVIITemplate,
      ArabicLetterType.Kaf,
      ArabicLetterType.Seen,
      ArabicLetterType.Ra,
      ConjugationTuple("لَمَّا يَنْكَسِرْ", "لَمَّا يَنْكَسِرُوْا", Some("لَمَّا يَنْكَسِرَا")),
      "لَمَّا يَنْكَسِرْ",
      jussiveParticle = Some(JussiveParticle.NotYet)
    ),
    TransformerCase(
      "PresentTenseJussiveModeTransformer: FormVII: SecondPersonFeminine",
      PresentTenseJussiveModeTransformer(VerbGroupType.SecondPersonFeminine),
      verb.FormVII.PresentTenseJussiveMode,
      NamedTemplate.FormVIITemplate,
      ArabicLetterType.Kaf,
      ArabicLetterType.Seen,
      ArabicLetterType.Ra,
      ConjugationTuple("إِنْكَسِرِيْ", "إِنْكَسِرْنَ", Some("إِنْكَسِرَا")),
      "لِيَنْكَسِرْ",
      jussiveParticle = Some(JussiveParticle.LamOfCommand)
    ),
    TransformerCase(
      "PresentTenseJussiveModeTransformer: FormVIII: ThirdPersonMasculine",
      PresentTenseJussiveModeTransformer(VerbGroupType.ThirdPersonMasculine),
      verb.FormVIII.PresentTenseJussiveMode,
      NamedTemplate.FormVIIITemplate,
      ArabicLetterType.Qaf,
      ArabicLetterType.Ra,
      ArabicLetterType.Ba,
      ConjugationTuple("لَايَقْتَرِبْ", "لَايَقْتَرِبُوْا", Some("لَايَقْتَرِبَا")),
      "لَايَقْتَرِبْ",
      jussiveParticle = Some(JussiveParticle.LamOfProhibition)
    ),
    TransformerCase(
      "PresentTenseJussiveModeTransformer: FormVIII: SecondPersonFeminine",
      PresentTenseJussiveModeTransformer(VerbGroupType.SecondPersonFeminine),
      verb.FormVIII.PresentTenseJussiveMode,
      NamedTemplate.FormVIIITemplate,
      ArabicLetterType.Qaf,
      ArabicLetterType.Ra,
      ArabicLetterType.Ba,
      ConjugationTuple("لَمْ تَقْتَرِبِيْ", "لَمْ تَقْتَرِبْنَ", Some("لَمْ تَقْتَرِبَا")),
      "لَمْ يَقْتَرِبْ",
      jussiveParticle = Some(JussiveParticle.DidNot)
    ),
    TransformerCase(
      "PresentTenseJussiveModeTransformer: FormVIII: PresentPassiveTenseJussiveMode: ThirdPersonMasculine",
      PresentTenseJussiveModeTransformer(VerbGroupType.ThirdPersonMasculine),
      verb.FormVIII.PresentPassiveTenseJussiveMode,
      NamedTemplate.FormVIIITemplate,
      ArabicLetterType.Qaf,
      ArabicLetterType.Ra,
      ArabicLetterType.Ba,
      ConjugationTuple("لَمَّا يُقْتَرَبْ", "لَمَّا يُقْتَرَبُوْا", Some("لَمَّا يُقْتَرَبَا")),
      "لَمَّا يُقْتَرَبْ",
      jussiveParticle = Some(JussiveParticle.NotYet)
    ),
    TransformerCase(
      "PresentTenseJussiveModeTransformer: FormX: ThirdPersonMasculine",
      PresentTenseJussiveModeTransformer(VerbGroupType.ThirdPersonMasculine),
      verb.FormX.PresentTenseJussiveMode,
      NamedTemplate.FormXTemplate,
      ArabicLetterType.Ghain,
      ArabicLetterType.Fa,
      ArabicLetterType.Ra,
      ConjugationTuple("لِيَسْتَغْفِرْ", "لِيَسْتَغْفِرُوْا", Some("لِيَسْتَغْفِرَا")),
      "لِيَسْتَغْفِرْ",
      jussiveParticle = Some(JussiveParticle.LamOfCommand)
    ),
    TransformerCase(
      "PresentTenseJussiveModeTransformer: FormX: SecondPersonFeminine",
      PresentTenseJussiveModeTransformer(VerbGroupType.SecondPersonFeminine),
      verb.FormX.PresentTenseJussiveMode,
      NamedTemplate.FormXTemplate,
      ArabicLetterType.Ghain,
      ArabicLetterType.Fa,
      ArabicLetterType.Ra,
      ConjugationTuple("لَاتَسْتَغْفِرِيْ", "لَاتَسْتَغْفِرْنَ", Some("لَاتَسْتَغْفِرَا")),
      "لَايَسْتَغْفِرْ",
      jussiveParticle = Some(JussiveParticle.LamOfProhibition)
    ),
    TransformerCase(
      "PresentTenseJussiveModeTransformer: FormX: PresentPassiveTenseJussiveMode: ThirdPersonMasculine",
      PresentTenseJussiveModeTransformer(VerbGroupType.ThirdPersonMasculine),
      verb.FormX.PresentPassiveTenseJussiveMode,
      NamedTemplate.FormXTemplate,
      ArabicLetterType.Ghain,
      ArabicLetterType.Fa,
      ArabicLetterType.Ra,
      ConjugationTuple("لَمْ يُسْتَغْفَرْ", "لَمْ يُسْتَغْفَرُوْا", Some("لَمْ يُسْتَغْفَرَا")),
      "لَمْ يُسْتَغْفَرْ",
      jussiveParticle = Some(JussiveParticle.DidNot)
    ),
    TransformerCase(
      "ImperativeAndForbiddenTransformer: Imperative: SecondPersonMasculine",
      ImperativeAndForbiddenTransformer(VerbGroupType.SecondPersonMasculine, MorphologyVerbType.Imperative),
      verb.FormIV.Imperative,
      NamedTemplate.FormIVTemplate,
      ArabicLetterType.Seen,
      ArabicLetterType.Lam,
      ArabicLetterType.Meem,
      ConjugationTuple("أَسْلِمْ", "أَسْلِمُوْا", Some("أَسْلِمَا")),
      "أَسْلِمْ"
    ),
    TransformerCase(
      "ImperativeAndForbiddenTransformer: Imperative: SecondPersonFeminine",
      ImperativeAndForbiddenTransformer(VerbGroupType.SecondPersonFeminine, MorphologyVerbType.Imperative),
      verb.FormV.Imperative,
      NamedTemplate.FormIITemplate,
      ArabicLetterType.Ain,
      ArabicLetterType.Lam,
      ArabicLetterType.Meem,
      ConjugationTuple("تَعَلَّمِيْ", "تَعَلَّمْنَ", Some("تَعَلَّمَا")),
      "تَعَلَّمْ"
    ),
    TransformerCase(
      "ImperativeAndForbiddenTransformer: Forbidden: SecondPersonMasculine",
      ImperativeAndForbiddenTransformer(VerbGroupType.SecondPersonMasculine, MorphologyVerbType.Forbidden),
      verb.FormII.Forbidden,
      NamedTemplate.FormIITemplate,
      ArabicLetterType.Ain,
      ArabicLetterType.Lam,
      ArabicLetterType.Meem,
      ConjugationTuple("لا تُعَلِّمْ", "لا تُعَلِّمُوْا", Some("لا تُعَلِّمَا")),
      "لا تُعَلِّمْ"
    ),
    TransformerCase(
      "ImperativeAndForbiddenTransformer: Forbidden: SecondPersonFeminine",
      ImperativeAndForbiddenTransformer(VerbGroupType.SecondPersonFeminine, MorphologyVerbType.Forbidden),
      verb.FormIII.Forbidden,
      NamedTemplate.FormIIITemplate,
      ArabicLetterType.Jeem,
      ArabicLetterType.Ha,
      ArabicLetterType.Dal,
      ConjugationTuple("لا تُجَاهِدِيْ", "لا تُجَاهِدْنَ", Some("لا تُجَاهِدَا")),
      "لا تُجَاهِدْ"
    )
  )

  transformerCases.foreach { c =>
    test(c.name) {
      val processingContext =
        ProcessingContext(
          c.namedTemplate,
          OutputFormat.Unicode,
          c.firstRadical,
          c.secondRadical,
          c.thirdRadical,
          jussiveParticle = c.jussiveParticle
        )
      validateTransformer(c.transformer, c.rootWordSupport, processingContext, c.expected, c.expectedDefaultValue)
    }
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
