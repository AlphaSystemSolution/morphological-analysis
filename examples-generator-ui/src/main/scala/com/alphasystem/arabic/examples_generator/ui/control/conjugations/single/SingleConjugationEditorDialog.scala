package com.alphasystem
package arabic
package examples_generator
package ui
package control
package conjugations
package single

import arabic.morphologicalengine.conjugation.model.MorphologicalTermType
import examples_generator.morphologicalchart.SingleConjugation
import javafx.scene.control.Button
import javafx.stage.Screen
import scalafx.Includes.*
import scalafx.application.JFXApp3
import scalafx.scene.control.ButtonBar.ButtonData
import scalafx.scene.control.{ ButtonType, Dialog }

class SingleConjugationEditorDialog extends Dialog[Option[SingleConjugation]] {

  private val dialogContent = SingleConjugationEditorView()
  private val okButtonType = new ButtonType("OK", ButtonData.OKDone)
  resizable = true
  dialogPane().setMinSize(0, 0)
  onShowing = _ => sizeToScreen()

  initOwner(JFXApp3.Stage)
  title = "Create / Edit Root Info"
  headerText = "Create or edit single root info."
  dialogPane().buttonTypes = Seq(okButtonType, ButtonType.Cancel)
  dialogPane().content = dialogContent

  private val okButton = dialogPane().lookupButton(okButtonType).asInstanceOf[Button]
  okButton.disableProperty().bind(dialogContent.tagProperty.isEmpty)

  resultConverter = dialogButtonType =>
    if dialogButtonType == okButtonType then Option(dialogContent.singleConjugation)
    else None

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

  def reset(
    maybeSingleConjugation: Option[SingleConjugation] = None,
    otherMorphologicalTermType: Option[MorphologicalTermType] = None
  ): Unit = {
    dialogContent.otherMorphologicalTermType = otherMorphologicalTermType
    dialogContent.singleConjugation = maybeSingleConjugation.orNull
  }
}

object SingleConjugationEditorDialog {
  def apply(): SingleConjugationEditorDialog = new SingleConjugationEditorDialog()
}
