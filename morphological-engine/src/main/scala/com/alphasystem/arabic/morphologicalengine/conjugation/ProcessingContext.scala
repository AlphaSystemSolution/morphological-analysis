package com.alphasystem
package arabic
package morphologicalengine
package conjugation

import com.alphasystem.arabic.model.ArabicLetterType
import model.{ NamedTemplate, OutputFormat }

class ProcessingContext(
  val namedTemplate: NamedTemplate,
  val outputFormat: OutputFormat,
  val firstRadical: ArabicLetterType,
  val secondRadical: ArabicLetterType,
  val thirdRadical: ArabicLetterType,
  val fourthRadical: Option[ArabicLetterType],
  val skipRuleProcessing: Boolean) {

  private var _pastTenseHasTransformed: Boolean = false

  def pastTenseHasTransformed: Boolean = _pastTenseHasTransformed
  def pastTenseHasTransformed_=(value: Boolean): Unit = _pastTenseHasTransformed = value
}

object ProcessingContext {

  def apply(
    namedTemplate: NamedTemplate,
    outputFormat: OutputFormat,
    firstRadical: ArabicLetterType,
    secondRadical: ArabicLetterType,
    thirdRadical: ArabicLetterType,
    fourthRadical: Option[ArabicLetterType] = None,
    skipRuleProcessing: Boolean = false
  ): ProcessingContext = new ProcessingContext(
    namedTemplate,
    outputFormat,
    firstRadical,
    secondRadical,
    thirdRadical,
    fourthRadical,
    skipRuleProcessing
  )
}
