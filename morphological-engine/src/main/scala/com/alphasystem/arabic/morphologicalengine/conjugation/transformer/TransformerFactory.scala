package com.alphasystem
package arabic
package morphologicalengine
package conjugation
package transformer

import conjugation.model.internal.RootWord
import conjugation.rule.RuleProcessor
import conjugation.model.ConjugationGroup

trait TransformerFactory[ReturnType <: ConjugationGroup] {

  def transform(word: RootWord, ruleProcessor: RuleProcessor, processingContext: ProcessingContext): ReturnType
}
