package com.alphasystem
package arabic
package morphologicalengine
package conjugation
package forms

import arabic.model.{ ArabicSupportEnum, ArabicWord }
import conjugation.model.internal.RootWord
import morphologicalanalysis.morphology.model.Flexibility
import conjugation.model.{ ConjugationGroup, NounConjugationGroup, OutputFormat, VerbConjugationGroup }
import conjugation.rule.RuleProcessor

trait RootWordSupport[ReturnType <: ConjugationGroup] extends ArabicSupportEnum {

  val rootWord: RootWord

  def transform(ruleProcessor: RuleProcessor, processingContext: ProcessingContext): ReturnType

  override lazy val word: ArabicWord = rootWord.derivedWord
}

trait VerbSupport extends RootWordSupport[VerbConjugationGroup]

trait NounSupport extends RootWordSupport[NounConjugationGroup] {

  val feminine: Boolean
  val flexibility: Flexibility
}
