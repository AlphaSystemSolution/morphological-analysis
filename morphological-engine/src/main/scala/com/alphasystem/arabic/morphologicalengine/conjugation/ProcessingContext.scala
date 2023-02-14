package com.alphasystem
package arabic
package morphologicalengine
package conjugation

import arabic.model.ArabicLetterType
import conjugation.model.internal.WordStatus
import model.{ ChartMode, NamedTemplate, OutputFormat }

class ProcessingContext(
  val namedTemplate: NamedTemplate,
  val outputFormat: OutputFormat,
  val firstRadical: ArabicLetterType,
  val secondRadical: ArabicLetterType,
  val thirdRadical: ArabicLetterType,
  val fourthRadical: Option[ArabicLetterType],
  val skipRuleProcessing: Boolean) {

  private var _pastTenseHasTransformed: Boolean = false

  lazy val wordStatus: WordStatus = WordStatus(this)

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
