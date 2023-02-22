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

  private val hamzaReplacementProcessor = HamzaReplacementProcessor()
  private val imperativeProcessor = ImperativeProcessor()
  private val removeTatweel = RemoveTatweel()

  override def applyRules(baseRootWord: RootWord, processingContext: ProcessingContext): RootWord = {
    var updatedWord = imperativeProcessor.applyRules(baseRootWord, processingContext)
    if processingContext.skipRuleProcessing then {
      // TODO:
    }
    updatedWord = hamzaReplacementProcessor.applyRules(updatedWord, processingContext)
    removeTatweel.applyRules(updatedWord, processingContext)
  }
}

object RuleEngine {
  def apply(): RuleProcessor = new RuleEngine()
}
