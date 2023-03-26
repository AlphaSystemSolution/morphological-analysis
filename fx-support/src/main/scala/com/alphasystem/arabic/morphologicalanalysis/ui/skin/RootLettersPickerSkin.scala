package com.alphasystem
package arabic
package morphologicalanalysis
package ui
package skin

import arabic.fx.ui.util.UIUserPreferences
import arabic.model.ArabicWord
import morphologicalengine.conjugation.model.RootLetters
import arabic.utils.*
import javafx.scene.control.SkinBase
import scalafx.Includes.*
import scalafx.scene.control.Button
import scalafx.scene.image.ImageView
import scalafx.scene.layout.{ BorderPane, GridPane }
import scalafx.stage.Popup

class RootLettersPickerSkin(control: RootLettersPickerView)(using preferences: UIUserPreferences)
    extends SkinBase[RootLettersPickerView](control) {

  private val keyBoard = RootLettersKeyBoardView()
  private val label = new ArabicLabelView(ArabicWord("")) {
    setDisable(true)
    setWidth(160)
    setHeight(32)
    font = control.font
    fontProperty.bind(control.fontProperty)
  }
  private val keyboardPopup = new Popup() {
    autoHide = true
    hideOnEscape = true
    content.addOne(keyBoard)
    onAutoHide = event => {
      control.rootLetters = null
      control.rootLetters = keyBoard.rootLetters
      event.consume()
    }
  }
  private val pickerButton = new Button() {
    graphic = ImageView("images.root-letters-icon.png".asResourceUrl)
    onAction = event => {
      showPopup()
      event.consume()
    }
  }

  getChildren.addAll(initializeSkin)

  private def initializeSkin = {
    val gridPane = new GridPane() {
      hgap = 1
      alignment = control.alignment
    }
    gridPane.alignmentProperty().bind(control.alignmentProperty)

    updateView()
    gridPane.add(label, 0, 0)
    gridPane.add(pickerButton, 3, 0)

    new BorderPane() {
      center = gridPane
    }
  }

  private def updateView(): Unit = {
    updateLabel(control.rootLetters)
    control.rootLettersProperty.onChange((_, _, nv) => updateLabel(nv))
  }

  private def updateLabel(rootLetters: RootLetters): Unit = {
    keyBoard.rootLetters = RootLettersKeyBoardView.DefaultRootLetters
    keyBoard.rootLetters = rootLetters
    label.label = rootLetters.arabicWord
  }

  private def showPopup(): Unit = {
    if keyboardPopup.isShowing then keyboardPopup.hide()
    else {
      val bounds = pickerButton.localToScreen(pickerButton.boundsInLocal.value)
      keyboardPopup.show(pickerButton, bounds.getMinX, bounds.getMinY + bounds.getHeight)
    }
  }
}

object RootLettersPickerSkin {
  def apply(control: RootLettersPickerView)(using preferences: UIUserPreferences): RootLettersPickerSkin =
    new RootLettersPickerSkin(control)
}
