package com.alphasystem
package arabic
package morphologicalengine
package conjugation
package transformer
package verb

import arabic.model.{ ArabicWord, ProNoun }
import morphologicalanalysis.morphology.model.{ ConversationType, GenderType }
import conjugation.model.RootWord
import conjugation.rule.RuleProcessor

abstract class AbstractVerbTransformer(
  ruleProcessor: RuleProcessor,
  genderType: GenderType,
  conversationType: ConversationType)
    extends AbstractTransformer(ruleProcessor) {

  override protected def doPreProcess(rootWord: RootWord): RootWord = rootWord
}
