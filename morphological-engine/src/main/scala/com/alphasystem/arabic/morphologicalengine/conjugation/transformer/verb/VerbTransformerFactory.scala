package com.alphasystem
package arabic
package morphologicalengine
package conjugation
package transformer
package verb

import com.alphasystem.arabic.model.ArabicLetterType
import conjugation.model.{ OutputFormat, RootWord, VerbConjugationGroup }
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
    outputFormat: OutputFormat,
    firstRadical: ArabicLetterType,
    secondRadical: ArabicLetterType,
    thirdRadical: ArabicLetterType,
    fourthRadical: Option[ArabicLetterType]
  ): VerbConjugationGroup = {
    val masculineThirdPerson = masculineThirdPersonTransformer.map(
      _.doTransform(ruleProcessor, word, outputFormat, firstRadical, secondRadical, thirdRadical, fourthRadical)
    )
    val feminineThirdPerson = feminineThirdPersonTransformer.map(
      _.doTransform(ruleProcessor, word, outputFormat, firstRadical, secondRadical, thirdRadical, fourthRadical)
    )
    val masculineSecondPerson = masculineSecondPersonTransformer.doTransform(
      ruleProcessor,
      word,
      outputFormat,
      firstRadical,
      secondRadical,
      thirdRadical,
      fourthRadical
    )
    val feminineSecondPerson = feminineSecondPersonTransformer.doTransform(
      ruleProcessor,
      word,
      outputFormat,
      firstRadical,
      secondRadical,
      thirdRadical,
      fourthRadical
    )
    val firstPerson = firstPersonTransformer.map(
      _.doTransform(ruleProcessor, word, outputFormat, firstRadical, secondRadical, thirdRadical, fourthRadical)
    )

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
