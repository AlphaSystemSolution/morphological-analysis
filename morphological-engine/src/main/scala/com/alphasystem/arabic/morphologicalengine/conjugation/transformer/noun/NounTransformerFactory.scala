package com.alphasystem
package arabic
package morphologicalengine
package conjugation
package transformer
package noun

import arabic.model.ArabicLetterType
import conjugation.model.*
import conjugation.rule.RuleProcessor

class NounTransformerFactory(
  nominativeTransformer: Transformer,
  accusativeTransformer: Transformer,
  genitiveTransformer: Transformer)
    extends TransformerFactory[NounConjugationGroup] {

  override def transform(
    word: RootWord,
    ruleProcessor: RuleProcessor,
    outputFormat: OutputFormat,
    firstRadical: ArabicLetterType,
    secondRadical: ArabicLetterType,
    thirdRadical: ArabicLetterType,
    fourthRadical: Option[ArabicLetterType] = None
  ): NounConjugationGroup = {
    val nominativeTuple =
      transform(
        nominativeTransformer,
        word,
        ruleProcessor,
        outputFormat,
        firstRadical,
        secondRadical,
        thirdRadical,
        fourthRadical
      )

    val accusativeTuple =
      transform(
        accusativeTransformer,
        word,
        ruleProcessor,
        outputFormat,
        firstRadical,
        secondRadical,
        thirdRadical,
        fourthRadical
      )

    val genitiveTuple =
      transform(
        genitiveTransformer,
        word,
        ruleProcessor,
        outputFormat,
        firstRadical,
        secondRadical,
        thirdRadical,
        fourthRadical
      )

    NounConjugationGroup(
      nominative = nominativeTuple,
      accusative = accusativeTuple,
      genitive = genitiveTuple
    )
  }

  private def transform(
    transformer: Transformer,
    rootWord: RootWord,
    ruleProcessor: RuleProcessor,
    outputFormat: OutputFormat,
    firstRadical: ArabicLetterType,
    secondRadical: ArabicLetterType,
    thirdRadical: ArabicLetterType,
    fourthRadical: Option[ArabicLetterType]
  ): ConjugationTuple = transformer.doTransform(
    ruleProcessor,
    rootWord,
    outputFormat,
    firstRadical,
    secondRadical,
    thirdRadical,
    fourthRadical
  )
}

object NounTransformerFactory {
  def apply(
    nominativeTransformer: Transformer,
    accusativeTransformer: Transformer,
    genitiveTransformer: Transformer
  ): NounTransformerFactory =
    new NounTransformerFactory(nominativeTransformer, accusativeTransformer, genitiveTransformer)
}
