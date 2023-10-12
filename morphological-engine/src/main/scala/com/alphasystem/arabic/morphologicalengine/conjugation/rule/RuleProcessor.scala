package com.alphasystem
package arabic
package morphologicalengine
package conjugation
package rule

import conjugation.model.internal.RootWord
import org.slf4j.{ Logger, LoggerFactory }
import rule.processors.*

trait RuleProcessor {

  protected val logger: Logger = LoggerFactory.getLogger(getClass)

  def applyRules(baseRootWord: RootWord, processingContext: ProcessingContext): RootWord
}

class RuleEngine extends RuleProcessor {

  private val hamzaReplacementProcessor = HamzaReplacementProcessor()
  private val imperativeProcessor = ImperativeProcessor()
  private val rule1Processor = Rule1Processor()
  private val removeTatweel = RemoveTatweel()
  private val forbiddenNegationProcessor = ForbiddenNegationProcessor()

  override def applyRules(baseRootWord: RootWord, processingContext: ProcessingContext): RootWord = {
    var updatedWord = imperativeProcessor.applyRules(baseRootWord, processingContext)
    if !processingContext.skipRuleProcessing then {
      updatedWord = rule1Processor.applyRules(updatedWord, processingContext)
    }
    updatedWord = hamzaReplacementProcessor.applyRules(updatedWord, processingContext)
    updatedWord = removeTatweel.applyRules(updatedWord, processingContext)
    forbiddenNegationProcessor.applyRules(updatedWord, processingContext)
  }
}

object RuleEngine {
  def apply(): RuleProcessor = new RuleEngine()
}
