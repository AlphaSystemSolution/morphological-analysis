package com.alphasystem
package arabic
package morphologicalengine
package conjugation
package forms
package noun

import arabic.model.ArabicLetterType
import arabic.morphologicalanalysis.morphology.model.Flexibility
import conjugation.model.internal.RootWord
import conjugation.forms.NounSupport
import conjugation.model.{ NounConjugationGroup, OutputFormat }
import conjugation.rule.RuleProcessor
import conjugation.transformer.noun.AbstractNounTransformer.PluralType
import conjugation.transformer.noun.*

abstract class NounSupportBase extends NounSupport {

  override lazy val code: String = getClass.getSimpleName

  protected val transformerFactory: NounTransformerFactory

  override def transform(
    ruleProcessor: RuleProcessor,
    outputFormat: OutputFormat,
    firstRadical: ArabicLetterType,
    secondRadical: ArabicLetterType,
    thirdRadical: ArabicLetterType,
    fourthRadical: Option[ArabicLetterType]
  ): NounConjugationGroup = transformerFactory.transform(
    rootWord,
    ruleProcessor,
    outputFormat,
    firstRadical,
    secondRadical,
    thirdRadical,
    fourthRadical
  )
}

abstract class MasculineBasedNoun(
  override val rootWord: RootWord,
  override val feminine: Boolean = false,
  override val flexibility: Flexibility = Flexibility.FullyFlexible,
  pluralType: PluralType = PluralType.Default)
    extends NounSupportBase {

  override protected val transformerFactory: NounTransformerFactory =
    NounTransformerFactory(
      MasculineNominativeTransformer(flexibility = flexibility, pluralType = pluralType),
      MasculineAccusativeTransformer(flexibility = flexibility, pluralType = pluralType),
      MasculineGenitiveTransformer(flexibility = flexibility, pluralType = pluralType)
    )
}

abstract class FeminineBasedNoun(
  override val rootWord: RootWord,
  override val feminine: Boolean = true,
  override val flexibility: Flexibility = Flexibility.FullyFlexible)
    extends NounSupportBase {

  override protected val transformerFactory: NounTransformerFactory =
    NounTransformerFactory(
      FeminineNominativeTransformer(),
      FeminineAccusativeTransformer(),
      FeminineGenitiveTransformer()
    )
}
