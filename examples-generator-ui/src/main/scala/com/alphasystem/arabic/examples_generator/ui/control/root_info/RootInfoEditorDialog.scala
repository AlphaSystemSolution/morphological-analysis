package com.alphasystem
package arabic
package examples_generator
package ui
package control
package root_info

import arabic.morphologicalengine.conjugation.model.ConjugationInput
import scalafx.Includes.*
import scalafx.application.JFXApp3
import scalafx.scene.control.ButtonBar.ButtonData
import scalafx.scene.control.{ ButtonType, Dialog }

class RootInfoEditorDialog extends Dialog[Option[ConjugationInput]] {

  private val dialogContent = RootInfoEditorView()
  private val okButtonType = new ButtonType("OK", ButtonData.OKDone)

  initOwner(JFXApp3.Stage)
  title = "Create / Edit Root"
  headerText = "Create or edit root info."
  dialogPane().buttonTypes = Seq(okButtonType, ButtonType.Cancel)
  dialogPane().content = dialogContent

  resultConverter = dialogButtonType =>
    if dialogButtonType == okButtonType then Option(dialogContent.conjugationInput)
    else None

  def setConjugationInput(value: ConjugationInput): Unit = dialogContent.conjugationInput = value
  def reset(): Unit = dialogContent.reset()
}

object RootInfoEditorDialog {
  def apply(): RootInfoEditorDialog = new RootInfoEditorDialog()
}
