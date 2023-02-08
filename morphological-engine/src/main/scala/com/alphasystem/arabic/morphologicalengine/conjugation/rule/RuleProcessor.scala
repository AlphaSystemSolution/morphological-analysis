package com.alphasystem
package arabic
package morphologicalengine
package conjugation
package rule

import conjugation.model.RootWord

trait RuleProcessor {

  def applyRules(baseRootWord: RootWord): RootWord
}

class IdentityRuleProcessor extends RuleProcessor {
  override def applyRules(baseRootWord: RootWord): RootWord = baseRootWord
}

object IdentityRuleProcessor {
  def apply(): RuleProcessor = new IdentityRuleProcessor()
}
