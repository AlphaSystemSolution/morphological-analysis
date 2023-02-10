package com.alphasystem
package arabic
package morphologicalengine
package conjugation

import model.NamedTemplate

class ProcessingContext(val namedTemplate: NamedTemplate, val skipRuleProcessing: Boolean) {

  private var _pastTenseHasTransformed: Boolean = false

  def pastTenseHasTransformed: Boolean = _pastTenseHasTransformed
  def pastTenseHasTransformed_=(value: Boolean): Unit = _pastTenseHasTransformed = value
}

object ProcessingContext {

  def apply(
    namedTemplate: NamedTemplate,
    skipRuleProcessing: Boolean = false
  ): ProcessingContext = new ProcessingContext(namedTemplate, skipRuleProcessing)
}
