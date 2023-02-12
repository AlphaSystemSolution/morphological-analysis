package com.alphasystem
package arabic
package morphologicalengine
package conjugation
package rule

import conjugation.model.internal.RootWord

trait RuleProcessor {

  def applyRules(baseRootWord: RootWord, processingContext: ProcessingContext): RootWord
}

class IdentityRuleProcessor extends RuleProcessor {
  override def applyRules(baseRootWord: RootWord, processingContext: ProcessingContext): RootWord = baseRootWord
}

object IdentityRuleProcessor {
  def apply(): RuleProcessor = new IdentityRuleProcessor()
}
