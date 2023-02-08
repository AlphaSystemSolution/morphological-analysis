package com.alphasystem
package arabic
package morphologicalengine
package conjugation
package model

import arabic.model.{ ArabicLetterType, ArabicSupportEnum, ArabicWord }
import conjugation.rule.RuleProcessor
import morphologicalanalysis.morphology.model.Flexibility

trait RootWordSupport[ReturnType <: ConjugationGroup] extends ArabicSupportEnum {

  val rootWord: RootWord

  def transform(
    ruleProcessor: RuleProcessor,
    outputFormat: OutputFormat,
    firstRadical: ArabicLetterType,
    secondRadical: ArabicLetterType,
    thirdRadical: ArabicLetterType,
    fourthRadical: Option[ArabicLetterType]
  ): ReturnType

  override lazy val word: ArabicWord = rootWord.derivedWord
}

trait VerbSupport extends RootWordSupport[VerbConjugationGroup]

trait NounSupport extends RootWordSupport[NounConjugationGroup] {

  val feminine: Boolean
  val flexibility: Flexibility
}
