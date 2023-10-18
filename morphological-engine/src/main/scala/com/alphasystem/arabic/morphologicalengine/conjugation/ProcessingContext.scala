package com.alphasystem
package arabic
package morphologicalengine
package conjugation

import arabic.model.{ ArabicLetterType, DiacriticType }
import conjugation.model.internal.WordStatus
import model.{ NamedTemplate, OutputFormat }

import scala.collection.mutable.ListBuffer

class ProcessingContext(
  val namedTemplate: NamedTemplate,
  val outputFormat: OutputFormat,
  val firstRadical: ArabicLetterType,
  val secondRadical: ArabicLetterType,
  val thirdRadical: ArabicLetterType,
  val fourthRadical: Option[ArabicLetterType],
  val skipRuleProcessing: Boolean) {

  private var _pastTenseHasTransformed: Boolean = false
  private var _diacriticForWeakSecondRadicalWaw: Option[DiacriticType] = None
  private val buffer = ListBuffer[String]()

  lazy val wordStatus: WordStatus = WordStatus(this)

  def pastTenseHasTransformed: Boolean = _pastTenseHasTransformed
  def pastTenseHasTransformed_=(value: Boolean): Unit = _pastTenseHasTransformed = value

  def diacriticForWeakSecondRadicalWaw: Option[DiacriticType] = _diacriticForWeakSecondRadicalWaw
  def diacriticForWeakSecondRadicalWaw_=(value: Option[DiacriticType]): Unit =
    if _diacriticForWeakSecondRadicalWaw.isEmpty then _diacriticForWeakSecondRadicalWaw = value

  def appliedRules: Seq[String] = buffer.toSeq
  def applyRule(name: String): Unit = buffer += name
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
