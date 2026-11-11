package com.alphasystem
package arabic
package examples_generator
package ui
package control
package settings

import examples_generator.morphologicalchart.Settings
import arabic.morphologicalengine.conjugation.model.MorphologicalTermType
import javafx.scene.control.{ Control, Skin }
import scalafx.beans.property.{ BooleanProperty, DoubleProperty, ObjectProperty }

class SettingsView extends Control {

  val settingsProperty: ObjectProperty[Settings] = ObjectProperty[Settings](this, "settings")
  private[settings] val morphologicalTermTypeProperty =
    ObjectProperty[MorphologicalTermType](this, "morphologicalTermType", MorphologicalTermType.PastTense)
  private[settings] val showPronounsProperty = new BooleanProperty(this, "showPronouns", false)
  private[settings] val showNumbersProperty = new BooleanProperty(this, "showNumbers", false)
  private[settings] val showGendersProperty = new BooleanProperty(this, "showGenders", false)
  private[settings] val showConversationTypesProperty = new BooleanProperty(this, "showConversationTypes", false)
  private[settings] val showNounStatusProperty = new BooleanProperty(this, "showNounStatus", false)
  private[settings] val showTermTypeCaptionProperty = new BooleanProperty(this, "showTermTypeCaption", false)
  private[settings] val tableWidthProperty = ObjectProperty[Option[Int]](this, "tableWidth", None)

  val labelColumnWidthProperty = new DoubleProperty(this, "labelColumnWidth", -1.0)

  def labelColumnWidth: Double = labelColumnWidthProperty.value
  def labelColumnWidth_=(value: Double): Unit = labelColumnWidthProperty.value = value

  private var initializing = false

  settings = Settings()
  updateSettings()
  setSkin(createDefaultSkin())

  def settings: Settings = settingsProperty.value
  def settings_=(value: Settings): Unit = settingsProperty.value = Option(value).getOrElse(Settings())

  def morphologicalTermType: MorphologicalTermType = morphologicalTermTypeProperty.value
  def morphologicalTermType_=(value: MorphologicalTermType): Unit =
    morphologicalTermTypeProperty.value = Option(value).getOrElse(MorphologicalTermType.PastTense)

  def showPronouns: Boolean = showPronounsProperty.value
  private[settings] def showPronouns_=(value: Boolean): Unit = showPronounsProperty.value = value

  private def showNumbers: Boolean = showNumbersProperty.value
  private[settings] def showNumbers_=(value: Boolean): Unit = showNumbersProperty.value = value

  private def showGenders: Boolean = showGendersProperty.value
  private[settings] def showGenders_=(value: Boolean): Unit = showGendersProperty.value = value

  private def showConversationTypes: Boolean = showConversationTypesProperty.value
  private[settings] def showConversationTypes_=(value: Boolean): Unit =
    showConversationTypesProperty.value = value

  private def showNounStatus: Boolean = showNounStatusProperty.value
  private[settings] def showNounStatus_=(value: Boolean): Unit = showNounStatusProperty.value = value

  private def showTermTypeCaption: Boolean = showTermTypeCaptionProperty.value
  private[settings] def showTermTypeCaption_=(value: Boolean): Unit =
    showTermTypeCaptionProperty.value = value

  def tableWidth: Option[Int] = tableWidthProperty.value
  private[settings] def tableWidth_=(value: Option[Int]): Unit = tableWidthProperty.value = value

  def reset(): Unit = settings = Settings()

  private def initValues(settings: Settings): Unit = {
    initializing = true
    try {
      showPronouns = settings.showPronouns.getOrElse(false)
      showNumbers = settings.showNumbers.getOrElse(false)
      showGenders = settings.showGenders.getOrElse(false)
      showConversationTypes = settings.showConversationTypes.getOrElse(false)
      showNounStatus = settings.showNounStatus.getOrElse(false)
      showTermTypeCaption = settings.showTermTypeCaption.getOrElse(false)
      tableWidth = settings.tableWidth
    } finally initializing = false
  }

  private def updateSettings(): Unit = {
    settingsProperty.onChange((_, _, nv) => initValues(nv))
    showPronounsProperty.onChange((_, _, nv) =>
      if !initializing then settings = settings.copy(showPronouns = Option.when(nv)(true))
    )
    showNumbersProperty.onChange((_, _, nv) =>
      if !initializing then settings = settings.copy(showNumbers = Option.when(nv)(true))
    )
    showGendersProperty.onChange((_, _, nv) =>
      if !initializing then settings = settings.copy(showGenders = Option.when(nv)(true))
    )
    showConversationTypesProperty.onChange((_, _, nv) =>
      if !initializing then settings = settings.copy(showConversationTypes = Option.when(nv)(true))
    )
    showNounStatusProperty.onChange((_, _, nv) =>
      if !initializing then settings = settings.copy(showNounStatus = Option.when(nv)(true))
    )
    showTermTypeCaptionProperty.onChange((_, _, nv) =>
      if !initializing then settings = settings.copy(showTermTypeCaption = Option.when(nv)(true))
    )
    tableWidthProperty.onChange((_, _, nv) => if !initializing then settings = settings.copy(tableWidth = nv))
  }

  override def createDefaultSkin(): Skin[?] = skin.SettingsSkin(this)
}

object SettingsView {
  def apply(): SettingsView = new SettingsView()
}
