package com.alphasystem
package arabic
package morphologicalengine
package ui
package control
package skin

import arabic.fx.ui.util.UIUserPreferences
import arabic.model.ArabicWord
import arabic.morphologicalanalysis.ui.ArabicSupportGroupPane
import arabic.morphologicalengine.conjugation.forms.NounSupport
import arabic.morphologicalengine.conjugation.forms.noun.VerbalNoun
import javafx.scene.control.SkinBase
import scalafx.Includes.*
import scalafx.collections.ObservableBuffer
import scalafx.geometry.Pos
import scalafx.scene.control.{ Button, TextField }
import scalafx.scene.image.ImageView
import scalafx.scene.layout.{ BorderPane, GridPane }
import scalafx.scene.text.Text
import scalafx.stage.Popup

class VerbalNounPickerSkin(control: VerbalNounPickerView)(using preferences: UIUserPreferences)
    extends SkinBase[VerbalNounPickerView](control) {

  private val verbalNounChooser = ArabicSupportGroupPane[NounSupport](values = VerbalNoun.values)
  private val verbalNounsDisplayTextField = new TextField {
    prefWidth = 240
    alignment = Pos.CenterRight
    delegate.setFont(control.font)
    text = ""
  }

  private val verbalNounPopup = new Popup() {
    autoHide = true
    hideOnEscape = true
    content.addOne(verbalNounChooser)
    onAutoHide = _ => commitSelection()
    onHiding = _ => commitSelection()
  }

  private def commitSelection(): Unit = {
    val selected = verbalNounChooser.selectedValues
    control.verbalNounsProperty.clear()
    control.verbalNounsProperty.addAll(selected)
  }

  private val pickerButton = new Button() {
    graphic = Option(Thread.currentThread().getContextClassLoader.getResource("images/verbal-noun-icon.png")) match {
      case Some(url) => ImageView(url.toExternalForm)
      case None      => new Text("...")
    }
    onAction = () => showPopup()
  }

  control
    .verbalNounsProperty
    .onChange((_, _) => {
      val currentValues = control.verbalNouns
      updateVerbalNounDisplayText(currentValues)
      verbalNounChooser.updateSelectedValues(currentValues)
    })

  control.fontProperty.onChange((_, _, nv) => verbalNounsDisplayTextField.delegate.setFont(nv))

  private val initializeSkin = {
    val gridPane = new GridPane() {
      hgap = 8
    }
    gridPane.add(verbalNounsDisplayTextField, 0, 0)
    gridPane.add(pickerButton, 3, 0)

    val currentValues = control.verbalNouns
    updateVerbalNounDisplayText(currentValues)
    verbalNounChooser.updateSelectedValues(currentValues)

    new BorderPane {
      center = gridPane
    }
  }

  getChildren.add(initializeSkin)

  private def updateVerbalNounDisplayText(values: Seq[NounSupport]): Unit = {
    val text =
      if values.isEmpty then ""
      else {
        values
          .foldLeft(ArabicWord()) { case (arabicWord, value) =>
            if arabicWord.isEmpty then arabicWord.concat(value.word) else arabicWord.concatenateWithAnd(value.word)
          }
          .unicode
      }

    verbalNounsDisplayTextField.text = text
  }

  private def showPopup(): Unit = {
    if verbalNounPopup.isShowing then verbalNounPopup.hide()
    else {
      val bounds = pickerButton.localToScreen(pickerButton.boundsInLocal.value)
      verbalNounPopup.show(pickerButton, bounds.getMinX, bounds.getMinY + bounds.getHeight)
    }
  }
}

object VerbalNounPickerSkin {
  def apply(control: VerbalNounPickerView)(using preferences: UIUserPreferences): VerbalNounPickerSkin =
    new VerbalNounPickerSkin(control)
}
