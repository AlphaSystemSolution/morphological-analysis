package com.alphasystem
package arabic
package morphologicalengine

import arabic.fx.ui.util.UIUserPreferences
import com.alphasystem.arabic.model.ArabicLetterType
import scalafx.scene.text.Text
import ui.utils.MorphologicalEnginePreferences

package object ui {

  given preferences: MorphologicalEnginePreferences = MorphologicalEnginePreferences()

  private def createLabel(label: String)(using preferences: UIUserPreferences): Text =
    new Text() {
      text = label
      font = preferences.arabicFont
    }

  def createLabel(letter: ArabicLetterType)(using preferences: UIUserPreferences): Text =
    createLabel(letter.label)

  def createSpaceLabel(numOfSpaces: Int = 1)(using preferences: UIUserPreferences): Text =
    createLabel(" " * numOfSpaces)

  def roundTo100(srcValue: Double): Double =
    ((srcValue.toInt + 99) / 100).toDouble * 100
}
