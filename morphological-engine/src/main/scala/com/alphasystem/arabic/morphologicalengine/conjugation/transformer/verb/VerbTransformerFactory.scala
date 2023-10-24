package com.alphasystem
package arabic
package morphologicalengine
package conjugation
package transformer
package verb

import conjugation.model.internal.RootWord
import conjugation.model.VerbConjugationGroup
import conjugation.rule.RuleProcessor

class VerbTransformerFactory(
  masculineSecondPersonTransformer: Transformer,
  feminineSecondPersonTransformer: Transformer,
  masculineThirdPersonTransformer: Option[Transformer],
  feminineThirdPersonTransformer: Option[Transformer],
  firstPersonTransformer: Option[Transformer])
    extends TransformerFactory[VerbConjugationGroup] {

  override def transform(
    word: RootWord,
    ruleProcessor: RuleProcessor,
    processingContext: ProcessingContext
  ): VerbConjugationGroup = {
    val masculineThirdPerson =
      masculineThirdPersonTransformer.map(_.doTransform(ruleProcessor, word, processingContext))
    val feminineThirdPerson = feminineThirdPersonTransformer.map(_.doTransform(ruleProcessor, word, processingContext))
    val masculineSecondPerson = masculineSecondPersonTransformer.doTransform(ruleProcessor, word, processingContext)
    val feminineSecondPerson = feminineSecondPersonTransformer.doTransform(ruleProcessor, word, processingContext)
    val firstPerson = firstPersonTransformer.map(_.doTransform(ruleProcessor, word, processingContext))

    VerbConjugationGroup(
      masculineSecondPerson = masculineSecondPerson,
      feminineSecondPerson = feminineSecondPerson,
      masculineThirdPerson = masculineThirdPerson,
      feminineThirdPerson = feminineThirdPerson,
      firstPerson = firstPerson
    )
  }
}

object VerbTransformerFactory {
  def apply(
    masculineSecondPersonTransformer: Transformer,
    feminineSecondPersonTransformer: Transformer,
    masculineThirdPersonTransformer: Option[Transformer] = None,
    feminineThirdPersonTransformer: Option[Transformer] = None,
    firstPersonTransformer: Option[Transformer] = None
  ): VerbTransformerFactory = new VerbTransformerFactory(
    masculineSecondPersonTransformer,
    feminineSecondPersonTransformer,
    masculineThirdPersonTransformer,
    feminineThirdPersonTransformer,
    firstPersonTransformer
  )
}
