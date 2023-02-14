package com.alphasystem
package arabic
package morphologicalengine
package conjugation
package transformer

import arabic.model.{ ArabicLetterType, ArabicWord }
import conjugation.model.internal.RootWord
import conjugation.model.{ ConjugationTuple, OutputFormat }
import conjugation.rule.RuleProcessor

trait Transformer {

  /** Transform given `rootWord` into its singular, dual, and plural forms.
    *
    * @param ruleProcessor
    *   [[RuleProcessor]]
    * @param baseWord
    *   base root word
    * @param processingContext
    *   current processing context
    * @return
    *   conjugation group
    */
  def doTransform(
    ruleProcessor: RuleProcessor,
    baseWord: RootWord,
    processingContext: ProcessingContext
  ): ConjugationTuple
}

abstract class AbstractTransformer extends Transformer {

  override def doTransform(
    ruleProcessor: RuleProcessor,
    baseWord: RootWord,
    processingContext: ProcessingContext
  ): ConjugationTuple = {
    val rootWord = doInitialization(
      baseWord,
      processingContext.firstRadical,
      processingContext.secondRadical,
      processingContext.thirdRadical,
      processingContext.fourthRadical
    )
    doProcess(ruleProcessor, rootWord, processingContext)
  }

  protected def doPreProcess(rootWord: RootWord): RootWord

  protected def deriveSingularWord(rootWord: RootWord): ArabicWord = rootWord.derivedWord
  private def doSingular(rootWord: RootWord): RootWord = rootWord

  protected def deriveDualWord(rootWord: RootWord): Option[ArabicWord]
  private def doDual(rootWord: RootWord): Option[RootWord] =
    deriveDualWord(rootWord).map(word => rootWord.copy(derivedWord = word))

  protected def derivePluralWord(rootWord: RootWord): ArabicWord
  private def doPlural(rootWord: RootWord): RootWord = rootWord.copy(derivedWord = derivePluralWord(rootWord))

  private def doInitialization(
    baseWord: RootWord,
    firstRadical: ArabicLetterType,
    secondRadical: ArabicLetterType,
    thirdRadical: ArabicLetterType,
    fourthRadical: Option[ArabicLetterType]
  ): RootWord = {
    val rootWord = doPreProcess(baseWord.transform(firstRadical, secondRadical, thirdRadical, fourthRadical))
    // do pre-process again, in case singular word has changed
    doPreProcess(rootWord.copy(derivedWord = deriveSingularWord(rootWord)))
  }

  private def doProcess(
    ruleProcessor: RuleProcessor,
    rootWord: RootWord,
    processingContext: ProcessingContext
  ): ConjugationTuple =
    ConjugationTuple(
      singular = processRule(ruleProcessor, doSingular(rootWord), processingContext)
        .toStringValue(processingContext.outputFormat),
      plural =
        processRule(ruleProcessor, doPlural(rootWord), processingContext).toStringValue(processingContext.outputFormat),
      dual = doDual(rootWord)
        .map(word => processRule(ruleProcessor, word, processingContext))
        .map(_.toStringValue(processingContext.outputFormat))
    )

  private def processRule(
    ruleProcessor: RuleProcessor,
    rootWord: RootWord,
    processingContext: ProcessingContext
  ): RootWord =
    ruleProcessor.applyRules(rootWord, processingContext)
}
