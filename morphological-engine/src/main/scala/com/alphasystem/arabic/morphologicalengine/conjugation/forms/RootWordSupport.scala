package com.alphasystem
package arabic
package morphologicalengine
package conjugation
package forms

import arabic.model.{ ArabicSupportEnum, ArabicWord }
import conjugation.transformer.noun.NounTransformerFactory
import conjugation.transformer.verb.VerbTransformerFactory
import conjugation.transformer.{ Transformer, TransformerFactory }
import conjugation.model.internal.RootWord
import morphologicalanalysis.morphology.model.Flexibility
import conjugation.model.{ ConjugationGroup, NounConjugationGroup, OutputFormat, VerbConjugationGroup }
import conjugation.rule.RuleProcessor

trait RootWordSupport[ReturnType <: ConjugationGroup, Factory <: TransformerFactory[ReturnType]]
    extends ArabicSupportEnum {

  override val code: String = getClass.getSimpleName.replaceAll("\\$", "")

  val rootWord: RootWord

  protected val defaultTransformer: Transformer

  protected val transformerFactory: Factory

  def transform(ruleProcessor: RuleProcessor, processingContext: ProcessingContext): ReturnType =
    transformerFactory.transform(rootWord, ruleProcessor, processingContext)

  def defaultValue(ruleProcessor: RuleProcessor, processingContext: ProcessingContext): String =
    defaultTransformer.doTransform(ruleProcessor, rootWord, processingContext).singular

  override lazy val word: ArabicWord = rootWord.derivedWord
}

trait VerbSupport extends RootWordSupport[VerbConjugationGroup, VerbTransformerFactory]

trait NounSupport extends RootWordSupport[NounConjugationGroup, NounTransformerFactory] {

  val feminine: Boolean
  val flexibility: Flexibility
}
