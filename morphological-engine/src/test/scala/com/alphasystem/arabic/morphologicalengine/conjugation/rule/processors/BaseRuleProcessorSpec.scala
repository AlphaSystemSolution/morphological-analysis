package com.alphasystem
package arabic
package morphologicalengine
package conjugation
package rule
package processors

import com.alphasystem.arabic.model.ArabicWord
import com.alphasystem.arabic.morphologicalengine.conjugation.model.internal.RootWord
import munit.FunSuite

abstract class BaseRuleProcessorSpec extends FunSuite {

  protected def validate(
    baseWord: RootWord,
    expected: ArabicWord,
    processor: RuleProcessor,
    processingContext: ProcessingContext
  ): Unit = {
    val rootWord = baseWord.transform(
      processingContext.firstRadical,
      processingContext.secondRadical,
      processingContext.thirdRadical
    )
    val obtained = processor.applyRules(rootWord, processingContext).derivedWord
    assertEquals(obtained, expected)
  }
}
