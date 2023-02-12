package com.alphasystem
package arabic
package morphologicalengine
package conjugation
package forms
package noun

import arabic.morphologicalanalysis.morphology.model.Flexibility
import com.alphasystem.arabic.morphologicalengine.conjugation.transformer.Transformer
import conjugation.model.internal.RootWord
import conjugation.forms.NounSupport
import conjugation.model.{ NounConjugationGroup, OutputFormat }
import conjugation.rule.RuleProcessor
import conjugation.transformer.noun.AbstractNounTransformer.PluralType
import conjugation.transformer.noun.*

abstract class NounSupportBase extends NounSupport {

  protected val transformerFactory: NounTransformerFactory

  override def transform(ruleProcessor: RuleProcessor, processingContext: ProcessingContext): NounConjugationGroup =
    transformerFactory.transform(rootWord, ruleProcessor, processingContext)
}

abstract class MasculineBasedNoun(
  override val rootWord: RootWord,
  override val feminine: Boolean = false,
  override val flexibility: Flexibility = Flexibility.FullyFlexible,
  pluralType: PluralType = PluralType.Default,
  verbalNounType: Boolean = false)
    extends NounSupportBase {

  private val masculineNominativeTransformer =
    MasculineNominativeTransformer(flexibility = flexibility, pluralType = pluralType)

  private val masculineAccusativeTransformer =
    MasculineAccusativeTransformer(flexibility = flexibility, pluralType = pluralType)

  override protected val defaultTransformer: Transformer =
    if verbalNounType then masculineAccusativeTransformer else masculineNominativeTransformer

  override protected val transformerFactory: NounTransformerFactory =
    NounTransformerFactory(
      masculineNominativeTransformer,
      masculineAccusativeTransformer,
      MasculineGenitiveTransformer(flexibility = flexibility, pluralType = pluralType)
    )

  override def defaultValue(ruleProcessor: RuleProcessor, processingContext: ProcessingContext): String =
    defaultTransformer.doTransform(ruleProcessor, rootWord, processingContext).singular
}

abstract class FeminineBasedNoun(
  override val rootWord: RootWord,
  override val feminine: Boolean = true,
  override val flexibility: Flexibility = Flexibility.FullyFlexible,
  verbalNounType: Boolean = false)
    extends NounSupportBase {

  private val feminineNominativeTransformer = FeminineNominativeTransformer()

  private val feminineAccusativeTransformer = FeminineAccusativeTransformer()

  override protected val defaultTransformer: Transformer =
    if verbalNounType then feminineAccusativeTransformer else feminineNominativeTransformer

  override protected val transformerFactory: NounTransformerFactory =
    NounTransformerFactory(feminineNominativeTransformer, feminineAccusativeTransformer, FeminineGenitiveTransformer())

  override def defaultValue(ruleProcessor: RuleProcessor, processingContext: ProcessingContext): String =
    defaultTransformer.doTransform(ruleProcessor, rootWord, processingContext).singular
}

abstract class MasculineBasedVerbalNoun(
  rootWord: RootWord,
  feminine: Boolean = false,
  flexibility: Flexibility = Flexibility.FullyFlexible,
  pluralType: PluralType = PluralType.Default)
    extends MasculineBasedNoun(rootWord, feminine, flexibility, pluralType, verbalNounType = true)

abstract class FeminineBasedVerbalNoun(
  rootWord: RootWord,
  feminine: Boolean = false,
  flexibility: Flexibility = Flexibility.FullyFlexible)
    extends FeminineBasedNoun(rootWord, feminine, flexibility, verbalNounType = true)
