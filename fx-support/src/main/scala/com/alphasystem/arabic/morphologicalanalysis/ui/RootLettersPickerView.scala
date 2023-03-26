package com.alphasystem
package arabic
package morphologicalanalysis
package ui

import com.alphasystem.arabic.fx.ui.util.UIUserPreferences
import com.alphasystem.arabic.model.ArabicLetterType
import com.alphasystem.arabic.morphologicalanalysis.ui.skin.RootLettersPickerSkin
import com.alphasystem.arabic.morphologicalengine.conjugation.model.RootLetters
import javafx.scene.control.{ Control, Skin }
import scalafx.beans.property.ObjectProperty
import scalafx.geometry.Pos
import scalafx.scene.text.Font

class RootLettersPickerView(using preferences: UIUserPreferences) extends Control {

  val rootLettersProperty: ObjectProperty[RootLetters] =
    ObjectProperty[RootLetters](this, "rootLetters", RootLettersKeyBoardView.DefaultRootLetters)

  val alignmentProperty: ObjectProperty[Pos] = ObjectProperty[Pos](this, "alignment", Pos.CenterLeft)

  val fontProperty: ObjectProperty[Font] = ObjectProperty[Font](this, "font", preferences.arabicFont)

  setSkin(createDefaultSkin())

  def rootLetters: RootLetters = rootLettersProperty.value
  def rootLetters_=(value: RootLetters): Unit = rootLettersProperty.value =
    if Option(value).isEmpty then RootLettersKeyBoardView.DefaultRootLetters else value

  def alignment: Pos = alignmentProperty.value
  def alignment_=(value: Pos): Unit = alignmentProperty.value = value

  def font: Font = fontProperty.value
  def font_=(value: Font): Unit = fontProperty.value = value

  override def createDefaultSkin(): Skin[_] = RootLettersPickerSkin(this)
}

object RootLettersPickerView {
  def apply()(using preferences: UIUserPreferences): RootLettersPickerView = new RootLettersPickerView()
}
