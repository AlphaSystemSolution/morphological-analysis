package com.alphasystem
package arabic
package morphologicalengine
package ui
package control
package skin

import com.alphasystem.arabic.fx.ui.util.UIUserPreferences
import com.alphasystem.arabic.model.ArabicWord
import com.alphasystem.arabic.morphologicalengine.conjugation.forms.NounSupport
import javafx.scene.control.SkinBase
import scalafx.Includes.*
import scalafx.scene.control.{Button, Label, TextField}
import scalafx.scene.image.ImageView
import scalafx.scene.layout.{BorderPane, GridPane}
import scalafx.scene.paint.Color
import scalafx.scene.text.Text
import scalafx.stage.Popup
import scalafx.geometry.Pos
import scalafx.collections.ObservableBuffer

class VerbalNounPickerSkin(control: VerbalNounPickerView)(using preferences: UIUserPreferences)
    extends SkinBase[VerbalNounPickerView](control) {

  private val verbalNounsDisplayTextField = new TextField {
    prefWidth = 160
    alignment = Pos.CenterRight
    delegate.setFont(control.font)
    text = ""
  }

  control.verbalNounsProperty.onChange((_, changes) => {
    changes.foreach {
      case ObservableBuffer.Add(_, added) => updateVerbalNounDisplayText(added.toSeq)
      case ObservableBuffer.Remove(_, removed) => updateVerbalNounDisplayText(Seq.empty)
      case ObservableBuffer.Reorder(_, _, _) => ()
      case ObservableBuffer.Update(_, _) => ()
    }
  })

  private val initializeSkin = {
    val gridPane = new GridPane() {
      hgap = 8
    }
    gridPane.add(verbalNounsDisplayTextField, 0, 0)

    updateVerbalNounDisplayText(control.verbalNouns)

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
}

object VerbalNounPickerSkin {
  def apply(control: VerbalNounPickerView)(using preferences: UIUserPreferences): VerbalNounPickerSkin =
    new VerbalNounPickerSkin(control)
}
