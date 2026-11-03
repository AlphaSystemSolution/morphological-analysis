package com.alphasystem
package arabic
package examples_generator
package ui
package control
package conjugations
package single

import examples_generator.morphologicalchart.ConjugationRequest
import scalafx.Includes.*
import scalafx.application.JFXApp3
import scalafx.scene.control.ButtonBar.ButtonData
import scalafx.scene.control.{ ButtonType, Dialog }

class SingleConjugationEditorDialog extends Dialog[Option[ConjugationRequest]] {

  private val dialogContent = SingleConjugationEditorView()
  private val okButtonType = new ButtonType("OK", ButtonData.OKDone)

  initOwner(JFXApp3.Stage)
  title = "Create / Edit Root"
  headerText = "Create or edit root info."
  dialogPane().buttonTypes = Seq(okButtonType, ButtonType.Cancel)
  dialogPane().content = dialogContent

  resultConverter = dialogButtonType =>
    if dialogButtonType == okButtonType then Option(dialogContent.conjugationRequest)
    else None

  def reset(conjugationRequest: Option[ConjugationRequest] = None): Unit =
    dialogContent.conjugationRequest = conjugationRequest.orNull
}

object SingleConjugationEditorDialog {
  def apply(): SingleConjugationEditorDialog = new SingleConjugationEditorDialog()
}
