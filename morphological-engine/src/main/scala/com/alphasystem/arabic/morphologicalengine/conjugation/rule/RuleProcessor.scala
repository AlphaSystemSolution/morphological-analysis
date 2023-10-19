package com.alphasystem
package arabic
package morphologicalengine
package conjugation
package rule

import arabic.model.SarfMemberType
import conjugation.model.internal.RootWord
import org.slf4j.{ Logger, LoggerFactory }
import rule.processors.*

trait RuleProcessor {

  protected val logger: Logger = LoggerFactory.getLogger(getClass)

  def applyRules(memberType: SarfMemberType, baseRootWord: RootWord, processingContext: ProcessingContext): RootWord
}

class RuleEngine extends RuleProcessor {

  private val hamzaReplacementProcessor = HamzaReplacementProcessor()
  private val imperativeProcessor = ImperativeProcessor()
  private val rule1Processor = Rule1Processor()
  private val rule7Processor = Rule7Processor()
  private val rule8Processor = Rule8Processor()
  private val rule9Processor = Rule9Processor()
  private val rule10Processor = Rule10Processor()
  private val removeTatweel = RemoveTatweel()
  private val patternProcessor = PatternProcessor()
  private val forbiddenNegationProcessor = ForbiddenNegationProcessor()

  override def applyRules(
    memberType: SarfMemberType,
    baseRootWord: RootWord,
    processingContext: ProcessingContext
  ): RootWord = {
    if processingContext.wordStatus.secondRadicalWaw then
      processingContext.diacriticForWeakSecondRadicalWaw = baseRootWord.secondRadicalDiacritic

    var updatedWord = imperativeProcessor.applyRules(memberType, baseRootWord, processingContext)
    if !processingContext.skipRuleProcessing then {
      updatedWord = rule1Processor.applyRules(memberType, updatedWord, processingContext)
      updatedWord = rule7Processor.applyRules(memberType, updatedWord, processingContext)
      updatedWord = rule8Processor.applyRules(memberType, updatedWord, processingContext)
      updatedWord = rule9Processor.applyRules(memberType, updatedWord, processingContext)
      updatedWord = rule10Processor.applyRules(memberType, updatedWord, processingContext)
    }
    updatedWord = hamzaReplacementProcessor.applyRules(memberType, updatedWord, processingContext)
    updatedWord = patternProcessor.applyRules(memberType, updatedWord, processingContext)
    updatedWord = removeTatweel.applyRules(memberType, updatedWord, processingContext)
    forbiddenNegationProcessor.applyRules(memberType, updatedWord, processingContext)
  }
}

object RuleEngine {
  def apply(): RuleProcessor = new RuleEngine()
}
