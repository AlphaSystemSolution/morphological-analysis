package com.alphasystem
package arabic
package morphologicalengine
package conjugation
package rule

import conjugation.model.internal.RootWord
import rule.processors.*

trait RuleProcessor {

  def applyRules(baseRootWord: RootWord, processingContext: ProcessingContext): RootWord
}

class RuleEngine extends RuleProcessor {

  private def hamzaReplacementProcessor = HamzaReplacementProcessor()
  override def applyRules(baseRootWord: RootWord, processingContext: ProcessingContext): RootWord = {
    if processingContext.skipRuleProcessing then {
      // TODO:
    }
    hamzaReplacementProcessor.applyRules(baseRootWord, processingContext)
  }
}

object RuleEngine {
  def apply(): RuleProcessor = new RuleEngine()
}
