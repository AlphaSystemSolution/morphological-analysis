package com.alphasystem
package arabic
package morphologicalengine
package conjugation
package model
package verb

import arabic.model.ArabicLetterType
import morphologicalanalysis.morphology.model.MorphologyVerbType
import conjugation.rule.RuleProcessor
import conjugation.transformer.verb.*

abstract class VerbSupportBase(override val rootWord: RootWord) extends VerbSupport {

  override lazy val code: String = getClass.getSimpleName

  protected val transformerFactory: VerbTransformerFactory

  override def transform(
    ruleProcessor: RuleProcessor,
    outputFormat: OutputFormat,
    firstRadical: ArabicLetterType,
    secondRadical: ArabicLetterType,
    thirdRadical: ArabicLetterType,
    fourthRadical: Option[ArabicLetterType]
  ): VerbConjugationGroup = transformerFactory.transform(
    rootWord,
    ruleProcessor,
    outputFormat,
    firstRadical,
    secondRadical,
    thirdRadical,
    fourthRadical
  )
}

abstract class PastTenseSupport(rootWord: RootWord) extends VerbSupportBase(rootWord) {

  override protected val transformerFactory: VerbTransformerFactory =
    VerbTransformerFactory(
      masculineSecondPersonTransformer = PastTenseTransformer(VerbGroupType.SecondPersonMasculine),
      feminineSecondPersonTransformer = PastTenseTransformer(VerbGroupType.SecondPersonFeminine),
      masculineThirdPersonTransformer = Some(PastTenseTransformer(VerbGroupType.ThirdPersonMasculine)),
      feminineThirdPersonTransformer = Some(PastTenseTransformer(VerbGroupType.ThirdPersonFeminine)),
      firstPersonTransformer = Some(PastTenseTransformer(VerbGroupType.FirstPerson))
    )
}

abstract class PresentTenseSupport(rootWord: RootWord) extends VerbSupportBase(rootWord) {

  override protected val transformerFactory: VerbTransformerFactory =
    VerbTransformerFactory(
      masculineSecondPersonTransformer = PresentTenseTransformer(VerbGroupType.SecondPersonMasculine),
      feminineSecondPersonTransformer = PresentTenseTransformer(VerbGroupType.SecondPersonFeminine),
      masculineThirdPersonTransformer = Some(PresentTenseTransformer(VerbGroupType.ThirdPersonMasculine)),
      feminineThirdPersonTransformer = Some(PresentTenseTransformer(VerbGroupType.ThirdPersonFeminine)),
      firstPersonTransformer = Some(PresentTenseTransformer(VerbGroupType.FirstPerson))
    )
}

abstract class ImperativeTenseSupport(rootWord: RootWord) extends VerbSupportBase(rootWord) {

  override protected val transformerFactory: VerbTransformerFactory =
    VerbTransformerFactory(
      masculineSecondPersonTransformer =
        ImperativeAndForbiddenTransformer(VerbGroupType.SecondPersonMasculine, MorphologyVerbType.Command),
      feminineSecondPersonTransformer =
        ImperativeAndForbiddenTransformer(VerbGroupType.SecondPersonFeminine, MorphologyVerbType.Command)
    )
}

abstract class ForbiddenTenseSupport(rootWord: RootWord) extends VerbSupportBase(rootWord) {

  override protected val transformerFactory: VerbTransformerFactory =
    VerbTransformerFactory(
      masculineSecondPersonTransformer =
        ImperativeAndForbiddenTransformer(VerbGroupType.SecondPersonMasculine, MorphologyVerbType.Forbidden),
      feminineSecondPersonTransformer =
        ImperativeAndForbiddenTransformer(VerbGroupType.SecondPersonFeminine, MorphologyVerbType.Forbidden)
    )
}
