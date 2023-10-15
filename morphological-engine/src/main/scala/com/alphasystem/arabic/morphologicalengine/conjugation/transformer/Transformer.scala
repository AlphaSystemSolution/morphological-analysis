package com.alphasystem
package arabic
package morphologicalengine
package conjugation
package transformer

import arabic.model.{ ArabicLetterType, ArabicWord, HiddenNounStatus, SarfMemberType }
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
    val (baseMemberType, rootWord) = doInitialization(
      baseWord,
      processingContext.firstRadical,
      processingContext.secondRadical,
      processingContext.thirdRadical,
      processingContext.fourthRadical
    )
    doProcess(ruleProcessor, baseMemberType, rootWord, processingContext)
  }

  protected def doPreProcess(rootWord: RootWord): RootWord

  protected def deriveSingularWord(rootWord: RootWord): (SarfMemberType, ArabicWord) =
    (HiddenNounStatus.NominativeSingular, rootWord.derivedWord)
  private def doSingular(rootWord: RootWord): RootWord = rootWord

  protected def deriveDualWord(rootWord: RootWord): Option[(SarfMemberType, ArabicWord)]
  private def doDual(rootWord: RootWord): Option[(SarfMemberType, RootWord)] =
    deriveDualWord(rootWord).map((memberType, word) => (memberType, rootWord.copy(derivedWord = word)))

  protected def derivePluralWord(rootWord: RootWord): (SarfMemberType, ArabicWord)
  private def doPlural(rootWord: RootWord): (SarfMemberType, RootWord) = {
    val (memberType, word) = derivePluralWord(rootWord)
    (memberType, rootWord.copy(derivedWord = word))
  }

  private def doInitialization(
    baseWord: RootWord,
    firstRadical: ArabicLetterType,
    secondRadical: ArabicLetterType,
    thirdRadical: ArabicLetterType,
    fourthRadical: Option[ArabicLetterType]
  ): (SarfMemberType, RootWord) = {
    val rootWord = doPreProcess(baseWord.transform(firstRadical, secondRadical, thirdRadical, fourthRadical))
    // do pre-process again, in case singular word has changed
    val (memberType, derivedWord) = deriveSingularWord(rootWord)
    (memberType, doPreProcess(rootWord.copy(derivedWord = derivedWord)))
  }

  private def doProcess(
    ruleProcessor: RuleProcessor,
    baseMemberType: SarfMemberType,
    rootWord: RootWord,
    processingContext: ProcessingContext
  ): ConjugationTuple = {
    val (pluralMemberType, pluralWord) = doPlural(rootWord)
    ConjugationTuple(
      singular = processRule(ruleProcessor, baseMemberType, doSingular(rootWord), processingContext)
        .toStringValue(processingContext.outputFormat),
      plural = processRule(ruleProcessor, pluralMemberType, pluralWord, processingContext).toStringValue(
        processingContext.outputFormat
      ),
      dual = doDual(rootWord)
        .map((memberType, word) => processRule(ruleProcessor, memberType, word, processingContext))
        .map(_.toStringValue(processingContext.outputFormat))
    )
  }

  private def processRule(
    ruleProcessor: RuleProcessor,
    memberType: SarfMemberType,
    rootWord: RootWord,
    processingContext: ProcessingContext
  ): RootWord =
    ruleProcessor.applyRules(memberType, rootWord, processingContext)
}
