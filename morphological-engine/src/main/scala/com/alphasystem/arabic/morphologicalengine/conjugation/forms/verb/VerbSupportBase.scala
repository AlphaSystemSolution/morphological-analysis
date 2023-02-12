package com.alphasystem
package arabic
package morphologicalengine
package conjugation
package forms
package verb

import arabic.morphologicalanalysis.morphology.model.MorphologyVerbType
import conjugation.transformer.Transformer
import conjugation.model.internal.{ RootWord, VerbGroupType }
import conjugation.model.{ OutputFormat, VerbConjugationGroup }
import conjugation.rule.RuleProcessor
import conjugation.transformer.verb.*

abstract class VerbSupportBase(override val rootWord: RootWord) extends VerbSupport {

  protected val transformerFactory: VerbTransformerFactory

  override def transform(ruleProcessor: RuleProcessor, processingContext: ProcessingContext): VerbConjugationGroup =
    transformerFactory.transform(rootWord, ruleProcessor, processingContext)

  override def defaultValue(ruleProcessor: RuleProcessor, processingContext: ProcessingContext): String =
    defaultTransformer.doTransform(ruleProcessor, rootWord, processingContext).singular
}

abstract class PastTenseSupport(rootWord: RootWord) extends VerbSupportBase(rootWord) {

  override protected val defaultTransformer: Transformer = PastTenseTransformer(VerbGroupType.ThirdPersonMasculine)

  override protected val transformerFactory: VerbTransformerFactory =
    VerbTransformerFactory(
      masculineSecondPersonTransformer = PastTenseTransformer(VerbGroupType.SecondPersonMasculine),
      feminineSecondPersonTransformer = PastTenseTransformer(VerbGroupType.SecondPersonFeminine),
      masculineThirdPersonTransformer = Some(defaultTransformer),
      feminineThirdPersonTransformer = Some(PastTenseTransformer(VerbGroupType.ThirdPersonFeminine)),
      firstPersonTransformer = Some(PastTenseTransformer(VerbGroupType.FirstPerson))
    )
}

abstract class PresentTenseSupport(rootWord: RootWord) extends VerbSupportBase(rootWord) {

  override protected val defaultTransformer: Transformer = PresentTenseTransformer(VerbGroupType.ThirdPersonMasculine)

  override protected val transformerFactory: VerbTransformerFactory =
    VerbTransformerFactory(
      masculineSecondPersonTransformer = PresentTenseTransformer(VerbGroupType.SecondPersonMasculine),
      feminineSecondPersonTransformer = PresentTenseTransformer(VerbGroupType.SecondPersonFeminine),
      masculineThirdPersonTransformer = Some(defaultTransformer),
      feminineThirdPersonTransformer = Some(PresentTenseTransformer(VerbGroupType.ThirdPersonFeminine)),
      firstPersonTransformer = Some(PresentTenseTransformer(VerbGroupType.FirstPerson))
    )
}

abstract class ImperativeTenseSupport(rootWord: RootWord) extends VerbSupportBase(rootWord) {

  override protected val defaultTransformer: Transformer =
    ImperativeAndForbiddenTransformer(VerbGroupType.SecondPersonMasculine, MorphologyVerbType.Command)

  override protected val transformerFactory: VerbTransformerFactory =
    VerbTransformerFactory(
      masculineSecondPersonTransformer = defaultTransformer,
      feminineSecondPersonTransformer =
        ImperativeAndForbiddenTransformer(VerbGroupType.SecondPersonFeminine, MorphologyVerbType.Command)
    )
}

abstract class ForbiddenTenseSupport(rootWord: RootWord) extends VerbSupportBase(rootWord) {

  override protected val defaultTransformer: Transformer =
    ImperativeAndForbiddenTransformer(VerbGroupType.SecondPersonMasculine, MorphologyVerbType.Forbidden)

  override protected val transformerFactory: VerbTransformerFactory =
    VerbTransformerFactory(
      masculineSecondPersonTransformer = defaultTransformer,
      feminineSecondPersonTransformer =
        ImperativeAndForbiddenTransformer(VerbGroupType.SecondPersonFeminine, MorphologyVerbType.Forbidden)
    )
}
