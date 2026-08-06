package com.alphasystem
package arabic
package morphologicalanalysis
package ui
package skin

import arabic.fx.ui.util.UIUserPreferences
import arabic.model.ArabicLetterType
import morphologicalengine.conjugation.model.RootLetters
import javafx.event.{ ActionEvent, EventHandler }
import javafx.scene.control.SkinBase
import scalafx.Includes.*
import scalafx.scene.control.{ Button, Label, TextField }
import scalafx.scene.image.ImageView
import scalafx.scene.layout.{ BorderPane, GridPane }
import scalafx.scene.paint.Color
import scalafx.scene.text.Text
import scalafx.stage.Popup

class RootLettersPickerSkin(control: RootLettersPickerView)(using preferences: UIUserPreferences)
    extends SkinBase[RootLettersPickerView](control) {

  private val keyBoard = RootLettersKeyBoardView()
  private val rootLettersField = new TextField {
    prefWidth = 160
    delegate.setFont(control.font)
    text = control.rootLetters.rawString
    delegate.setOnAction(new EventHandler[ActionEvent] {
      override def handle(event: ActionEvent): Unit = commitTypedRootLetters()
    })
  }
  private val validationLabel = new Label {
    textFill = Color.Red
    visible = false
    managed = false
  }
  private val keyboardPopup = new Popup() {
    autoHide = true
    hideOnEscape = true
    content.addOne(keyBoard)
    onAutoHide = _ => {
      control.rootLetters = null
      control.rootLetters = keyBoard.rootLetters
    }
  }
  private val pickerButton = new Button() {
    graphic = Option(Thread.currentThread().getContextClassLoader.getResource("images/root-letters-icon.png")) match {
      case Some(url) => ImageView(url.toExternalForm)
      case None      => new Text("...")
    }
    delegate.setOnAction(new EventHandler[ActionEvent] {
      override def handle(event: ActionEvent): Unit = showPopup()
    })
  }

  getChildren.addAll(initializeSkin)

  private def initializeSkin = {
    val gridPane = new GridPane() {
      hgap = 8
      alignment = control.alignment
    }
    gridPane.alignmentProperty().bind(control.alignmentProperty)

    updateView()
    gridPane.add(rootLettersField, 0, 0)
    gridPane.add(pickerButton, 3, 0)
    gridPane.add(validationLabel, 0, 1, 4, 1)

    new BorderPane() {
      center = gridPane
    }
  }

  private def updateView(): Unit = {
    updateRootLetters(control.rootLetters)
    control.rootLettersProperty.onChange((_, _, nv) => updateRootLetters(nv))
    control.fontProperty.onChange((_, _, nv) => rootLettersField.delegate.setFont(nv))
    rootLettersField.text.onChange((_, _, _) => validateTypedRootLetters())
  }

  private def updateRootLetters(rootLetters: RootLetters): Unit = {
    keyBoard.rootLetters = RootLettersKeyBoardView.DefaultRootLetters
    keyBoard.rootLetters = rootLetters
    rootLettersField.text = rootLetters.rawString
  }

  private def commitTypedRootLetters(): Unit = {
    parseRootLetters(rootLettersField.text.value) match {
      case Some(rootLetters) =>
        control.rootLetters = rootLetters
        validateTypedRootLetters()
      case None =>
        rootLettersField.text = control.rootLetters.rawString
        validateTypedRootLetters()
    }
  }

  private def validateTypedRootLetters(): Unit = {
    val normalized = Option(rootLettersField.text.value).getOrElse("").trim.filterNot(_.isWhitespace)
    val isPotential =
      normalized.isEmpty ||
        (normalized.length <= 4 && normalized.forall(ch => ArabicLetterType.UnicodesMap.contains(ch)))
    val isValidFinal = normalized.length < 3 || parseRootLetters(normalized).isDefined
    val isValid = isPotential && isValidFinal
    rootLettersField.style = if isValid then "" else "-fx-border-color: #d32f2f; -fx-border-width: 1.5;"
    val showMessage = !isValid && normalized.nonEmpty
    validationLabel.visible = showMessage
    validationLabel.managed = showMessage
    validationLabel.text = if showMessage then "Enter 3-4 valid Arabic root letters." else ""
  }

  private def parseRootLetters(input: String): Option[RootLetters] = {
    val normalized = Option(input).getOrElse("").trim.filterNot(_.isWhitespace)
    if normalized.length < 3 || normalized.length > 4 then None
    else {
      val letters = normalized.map(ArabicLetterType.UnicodesMap.get)
      if letters.exists(_.isEmpty) then None
      else {
        val parsed = letters.flatten
        val fourth = parsed.lift(3).filterNot(_ == ArabicLetterType.Tatweel)
        Some(RootLetters(parsed(0), parsed(1), parsed(2), fourth))
      }
    }
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
