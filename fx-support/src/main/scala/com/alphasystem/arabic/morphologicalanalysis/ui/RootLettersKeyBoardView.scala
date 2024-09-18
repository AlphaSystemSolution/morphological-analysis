package com.alphasystem
package arabic
package morphologicalanalysis
package ui

import model.ArabicLetterType
import ui.skin.RootLettersKeyBoardSkin
import arabic.morphologicalengine.conjugation.model.RootLetters
import arabic.utils.*
import arabic.fx.ui.util.UIUserPreferences
import javafx.geometry.Insets
import javafx.scene.control.{ Control, Skin }
import scalafx.Includes.*
import scalafx.beans.property.{ DoubleProperty, ObjectProperty }
import scalafx.scene.text.Font

class RootLettersKeyBoardView(using preferences: UIUserPreferences) extends Control {

  private[ui] val firstRadicalProperty = ObjectProperty[ArabicLetterType](this, "firstRadical", ArabicLetterType.Fa)
  private[ui] val secondRadicalProperty = ObjectProperty[ArabicLetterType](this, "secondRadical", ArabicLetterType.Ain)
  private[ui] val thirdRadicalProperty = ObjectProperty[ArabicLetterType](this, "thirdRadical", ArabicLetterType.Lam)
  private[ui] val fourthRadicalProperty = ObjectProperty[Option[ArabicLetterType]](this, "fourthRadical", None)
  val fontProperty: ObjectProperty[Font] = ObjectProperty[Font](this, "font", preferences.arabicFont)
  private[ui] val spacingProperty: DoubleProperty = new DoubleProperty(this, "spacing", 5)
  private[ui] val selectedLabelWidthProperty = new DoubleProperty(this, "selectedLabelWidth", 32)
  private[ui] val selectedLabelHeightProperty = new DoubleProperty(this, "selectedLabelHeight", 32)
  private[ui] val keyboardButtonWidthProperty = new DoubleProperty(this, "keyboardButtonWidth", 48)
  private[ui] val keyboardButtonHeightProperty = new DoubleProperty(this, "keyboardButtonHeight", 36)
  private[ui] val rootLettersProperty = ObjectProperty(
    this,
    "rootLetters",
    RootLetters(firstRadical, secondRadical, thirdRadical, fourthRadical)
  )

  firstRadicalProperty.onChange((_, _, nv) => rootLettersProperty.value = rootLetters.copy(firstRadical = nv))
  secondRadicalProperty.onChange((_, _, nv) => rootLettersProperty.value = rootLetters.copy(secondRadical = nv))
  thirdRadicalProperty.onChange((_, _, nv) => rootLettersProperty.value = rootLetters.copy(thirdRadical = nv))
  fourthRadicalProperty.onChange((_, _, nv) => rootLettersProperty.value = rootLetters.copy(fourthRadical = nv))
  rootLettersProperty.onChange((_, _, nv) => {
    firstRadical = nv.firstRadical
    secondRadical = nv.secondRadical
    thirdRadical = nv.thirdRadical
    fourthRadical = nv.fourthRadical
  })

  setPadding(Insets(5, 5, 5, 5))
  setStyle("-fx-base: beige;")
  setSkin(createDefaultSkin())

  def firstRadical: ArabicLetterType = firstRadicalProperty.value
  def firstRadical_=(value: ArabicLetterType): Unit = firstRadicalProperty.value = value

  def secondRadical: ArabicLetterType = secondRadicalProperty.value
  def secondRadical_=(value: ArabicLetterType): Unit = secondRadicalProperty.value = value

  def thirdRadical: ArabicLetterType = thirdRadicalProperty.value
  def thirdRadical_=(value: ArabicLetterType): Unit = thirdRadicalProperty.value = value

  def fourthRadical: Option[ArabicLetterType] = fourthRadicalProperty.value
  def fourthRadical_=(value: Option[ArabicLetterType]): Unit = fourthRadicalProperty.value = value

  def font: Font = fontProperty.value
  def font_=(value: Font): Unit = fontProperty.value = value

  def spacing: Double = spacingProperty.value
  def spacing_=(value: Double): Unit = spacingProperty.value = value

  def selectedLabelWidth: Double = selectedLabelWidthProperty.value
  def selectedLabelWidth_=(value: Double): Unit = selectedLabelWidthProperty.value = value

  def selectedLabelHeight: Double = selectedLabelHeightProperty.value
  def selectedLabelHeight_=(value: Double): Unit = selectedLabelHeightProperty.value = value

  def keyboardButtonWidth: Double = keyboardButtonWidthProperty.value
  def keyboardButtonWidth_=(value: Double): Unit = keyboardButtonWidthProperty.value = value

  def keyboardButtonHeight: Double = keyboardButtonHeightProperty.value
  def keyboardButtonHeight_=(value: Double): Unit = keyboardButtonHeightProperty.value = value

  def rootLetters: RootLetters = rootLettersProperty.value
  def rootLetters_=(value: RootLetters): Unit = rootLettersProperty.value = value

  override def getUserAgentStylesheet: String = "styles/ui-common.css".asResourceUrl

  override def createDefaultSkin(): Skin[?] = RootLettersKeyBoardSkin(this)
}

object RootLettersKeyBoardView {

  val DefaultRootLetters: RootLetters =
    RootLetters(ArabicLetterType.Fa, ArabicLetterType.Ain, ArabicLetterType.Lam)

  def apply()(using preferences: UIUserPreferences) = new RootLettersKeyBoardView()
}
