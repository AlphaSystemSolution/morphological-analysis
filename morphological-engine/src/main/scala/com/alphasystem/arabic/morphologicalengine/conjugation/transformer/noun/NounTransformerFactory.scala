package com.alphasystem
package arabic
package morphologicalengine
package conjugation
package transformer
package noun

import arabic.model.ArabicLetterType
import conjugation.model.internal.RootWord
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
    processingContext: ProcessingContext
  ): NounConjugationGroup = {
    val nominativeTuple = transform(nominativeTransformer, word, ruleProcessor, processingContext)
    val accusativeTuple = transform(accusativeTransformer, word, ruleProcessor, processingContext)
    val genitiveTuple = transform(genitiveTransformer, word, ruleProcessor, processingContext)

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
    processingContext: ProcessingContext
  ): ConjugationTuple = transformer.doTransform(ruleProcessor, rootWord, processingContext)
}

object NounTransformerFactory {
  def apply(
    nominativeTransformer: Transformer,
    accusativeTransformer: Transformer,
    genitiveTransformer: Transformer
  ): NounTransformerFactory =
    new NounTransformerFactory(nominativeTransformer, accusativeTransformer, genitiveTransformer)
}
