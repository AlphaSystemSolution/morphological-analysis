package com.alphasystem
package arabic
package morphologicalanalysis
package ui
package tokeneditor
package control

import com.alphasystem.arabic.morphologicalanalysis.morphology.model.incomplete_verb.{
  IncompleteVerbType,
  KanaPastTense,
  KanaPresentTense
}
import morphology.model.{ VerbProperties, defaultVerbProperties }
import skin.VerbPropertiesSkin
import morphology.model.*
import javafx.scene.control.Skin
import scalafx.beans.property.ObjectProperty

class VerbPropertiesView
    extends AbstractPropertiesView[
      VerbPartOfSpeechType,
      VerbProperties
    ] {

  override protected val initialProperties: VerbProperties =
    defaultVerbProperties

  val conversationTypeProperty: ObjectProperty[ConversationType] =
    ObjectProperty[ConversationType](this, "conversationType")

  val verbTypeProperty: ObjectProperty[MorphologyVerbType] =
    ObjectProperty[MorphologyVerbType](this, "verbType")

  val verbModeProperty: ObjectProperty[VerbMode] =
    ObjectProperty[VerbMode](this, "verbMode")

  val incompleteVerbTypeProperty: ObjectProperty[Option[IncompleteVerbType]] =
    ObjectProperty[Option[IncompleteVerbType]](this, "incompleteVerbType", None)

  conversationTypeProperty.onChange { (_, oldValue, newValue) =>
    if oldValue != newValue then properties = properties.copy(conversationType = newValue)
  }

  verbTypeProperty.onChange { (_, oldValue, newValue) =>
    if oldValue != newValue then properties = properties.copy(verbType = newValue)
  }

  incompleteVerbTypeProperty.onChange((_, oldValue, newValue) =>
    if oldValue != newValue then properties = properties.copy(incompleteVerb = incompleteVerbType)
  )

  properties = initialProperties
  setSkin(createDefaultSkin())

  def conversationType: ConversationType = conversationTypeProperty.value
  def conversationType_=(value: ConversationType): Unit =
    conversationTypeProperty.value = value

  def verbType: MorphologyVerbType = verbTypeProperty.value
  def verbType_=(value: MorphologyVerbType): Unit =
    verbTypeProperty.value = value

  def verbMode: VerbMode = verbModeProperty.value
  def verbMode_=(value: VerbMode): Unit = verbModeProperty.value = value

  def incompleteVerbType: Option[IncompleteVerbType] = incompleteVerbTypeProperty.value
  def incompleteVerbType_=(value: Option[IncompleteVerbType]): Unit = incompleteVerbTypeProperty.value = value

  override protected def update(
    partOfSpeechType: VerbPartOfSpeechType,
    properties: VerbProperties
  ): VerbProperties = properties.copy(partOfSpeech = partOfSpeechType)

  override protected def update(
    numberType: NumberType,
    properties: VerbProperties
  ): VerbProperties = properties.copy(number = numberType)

  override protected def update(
    genderType: GenderType,
    properties: VerbProperties
  ): VerbProperties = properties.copy(gender = genderType)

  override protected def update(properties: VerbProperties): Unit = {
    super.update(properties)
    conversationType = properties.conversationType
    verbType = properties.verbType
    verbMode = properties.mode
    incompleteVerbType = properties.incompleteVerb
  }

  override def createDefaultSkin(): Skin[?] = VerbPropertiesSkin(this)
}

object VerbPropertiesView {

  def apply(): VerbPropertiesView = new VerbPropertiesView()
}
