package com.alphasystem
package arabic
package morphologicalengine
package conjugation
package rule
package processors

import arabic.model.{ ArabicWord, SarfMemberType }
import conjugation.model.internal.RootWord
import munit.FunSuite

abstract class BaseRuleProcessorSpec extends FunSuite {

  private val processor = RuleEngine()

  protected def validate(
    baseWord: RootWord,
    expected: ArabicWord,
    memberType: SarfMemberType,
    processingContext: ProcessingContext
  ): Unit = {
    val rootWord = baseWord.transform(
      processingContext.firstRadical,
      processingContext.secondRadical,
      processingContext.thirdRadical
    )
    val obtained = processor.applyRules(memberType, rootWord, processingContext).derivedWord

    val appliedRules = processingContext.appliedRules
    lazy val clue =
      if appliedRules.nonEmpty then {
        s"Values are not same, applied rules: ${appliedRules.mkString("[", ", ", "]")}, expected word: ${expected.label}, obtained word: ${obtained.label}"
      } else s"Values are not same, expected word: ${expected.label}, obtained word: ${obtained.label}"
    assertEquals(obtained, expected, clue)
  }
}
