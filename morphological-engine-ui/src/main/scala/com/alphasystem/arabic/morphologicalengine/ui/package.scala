package com.alphasystem
package arabic
package morphologicalengine

import arabic.fx.ui.util.UIUserPreferences
import com.alphasystem.arabic.model.{ ArabicLetterType, ArabicSupport }
import scalafx.scene.text.Text
import ui.utils.MorphologicalEnginePreferences

import java.nio.file.{ Path, Paths }

package object ui {

  given preferences: MorphologicalEnginePreferences = MorphologicalEnginePreferences()

  private def createLabel(label: String)(using preferences: UIUserPreferences): Text =
    new Text() {
      text = label
      font = preferences.arabicFont
    }

  def createLabel(label: ArabicSupport)(using preferences: UIUserPreferences): Text = createLabel(label.label)

  def createLabel(letter: ArabicLetterType)(using preferences: UIUserPreferences): Text =
    createLabel(letter.label)

  def createSpaceLabel(numOfSpaces: Int = 1)(using preferences: UIUserPreferences): Text =
    createLabel(" " * numOfSpaces)

  def createSpaceWithAndLabel(numOfSpaces: Int = 1): Text = {
    val space = " " * numOfSpaces
    createLabel(space + ArabicLetterType.Waw.label + space)
  }

  def roundTo100(srcValue: Double): Double =
    ((srcValue.toInt + 99) / 100).toDouble * 100

  def getBaseName(path: Path): String = {
    val fileName = path.getFileName.toString
    val lastExtension = fileName.lastIndexOf('.')
    if lastExtension >= 0 then fileName.substring(0, lastExtension) else fileName
  }

  def toDocFile(path: Path): Path = {
    val baseName = getBaseName(path)
    Paths.get(path.getParent.toString, s"$baseName.docx")
  }
}
