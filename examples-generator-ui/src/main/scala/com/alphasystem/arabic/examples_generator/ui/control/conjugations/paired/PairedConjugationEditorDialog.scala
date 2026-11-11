package com.alphasystem
package arabic
package examples_generator
package ui
package control
package conjugations
package paired

import examples_generator.morphologicalchart.PairedConjugation
import javafx.scene.control.Button
import javafx.stage.Screen
import scalafx.Includes.*
import scalafx.application.JFXApp3
import scalafx.scene.control.ButtonBar.ButtonData
import scalafx.scene.control.{ ButtonType, Dialog }

class PairedConjugationEditorDialog extends Dialog[Option[PairedConjugation]] {

  private val dialogContent = new PairedConjugationEditorView()
  private val okButtonType = new ButtonType("OK", ButtonData.OKDone)
  resizable = true
  dialogPane().setMinSize(0, 0)
  onShowing = _ => sizeToScreen()

  initOwner(JFXApp3.Stage)
  title = "Create / Edit Paired Root Info"
  headerText = "Create or edit paired root info."
  dialogPane().buttonTypes = Seq(okButtonType, ButtonType.Cancel)
  dialogPane().content = dialogContent

  resultConverter = dialogButtonType =>
    if dialogButtonType == okButtonType then Option(dialogContent.pairedConjugation)
    else None

  private val okButton = dialogPane().lookupButton(okButtonType).asInstanceOf[Button]
  okButton.disableProperty().bind(dialogContent.validatedProperty.not)

  private def sizeToScreen(): Unit = {
    val screen = Option(delegate.getOwner)
      .filter(_.isShowing)
      .flatMap { owner =>
        val screens = Screen.getScreensForRectangle(owner.getX, owner.getY, owner.getWidth, owner.getHeight)
        if screens.isEmpty then None else Some(screens.get(0))
      }
      .getOrElse(Screen.getPrimary)
    val bounds = screen.getVisualBounds
    val dialogWidth = math.min(900.0, bounds.getWidth * 0.9)
    val dialogHeight = math.min(700.0, bounds.getHeight * 0.85)
    dialogPane().setPrefSize(dialogWidth, dialogHeight)
    delegate.setWidth(dialogWidth)
    delegate.setHeight(dialogHeight)
    delegate.setX(bounds.getMinX + (bounds.getWidth - dialogWidth) / 2)
    delegate.setY(bounds.getMinY + (bounds.getHeight - dialogHeight) / 2)
  }

  def reset(maybePairedConjugation: Option[PairedConjugation] = None): Unit =
    dialogContent.reset(maybePairedConjugation)
}

object PairedConjugationEditorDialog {
  def apply(): PairedConjugationEditorDialog = new PairedConjugationEditorDialog()
}
