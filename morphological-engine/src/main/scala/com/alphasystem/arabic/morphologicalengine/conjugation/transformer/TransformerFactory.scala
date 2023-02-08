package com.alphasystem
package arabic
package morphologicalengine
package conjugation
package transformer

import arabic.model.ArabicLetterType
import conjugation.rule.RuleProcessor
import conjugation.model.{ ConjugationGroup, OutputFormat, RootWord }

trait TransformerFactory[ReturnType <: ConjugationGroup] {

  def transform(
    word: RootWord,
    ruleProcessor: RuleProcessor,
    outputFormat: OutputFormat,
    firstRadical: ArabicLetterType,
    secondRadical: ArabicLetterType,
    thirdRadical: ArabicLetterType,
    fourthRadical: Option[ArabicLetterType] = None
  ): ReturnType
}
