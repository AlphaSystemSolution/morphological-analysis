package com.alphasystem
package arabic
package morphologicalengine
package conjugation
package forms
package verb

import arabic.morphologicalanalysis.morphology.model.MorphologyVerbType
import conjugation.transformer.Transformer
import conjugation.model.internal.{ RootWord, VerbGroupType }
import conjugation.transformer.verb.*

abstract class VerbSupportBase(override val rootWord: RootWord) extends VerbSupport

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
    ImperativeAndForbiddenTransformer(VerbGroupType.SecondPersonMasculine, MorphologyVerbType.Imperative)

  override protected val transformerFactory: VerbTransformerFactory =
    VerbTransformerFactory(
      masculineSecondPersonTransformer = defaultTransformer,
      feminineSecondPersonTransformer =
        ImperativeAndForbiddenTransformer(VerbGroupType.SecondPersonFeminine, MorphologyVerbType.Imperative)
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
