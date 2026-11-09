package com.alphasystem
package arabic
package examples_generator
package ui
package control
package conjugations
package single

import control.root_info.RootInfoEditorDialog
import arabic.morphologicalengine.conjugation.model.MorphologicalTermType
import examples_generator.morphologicalchart.SingleConjugation
import scalafx.Includes.*
import scalafx.application.JFXApp3
import scalafx.scene.control.ButtonBar.ButtonData
import scalafx.scene.control.{ ButtonType, Dialog }

class SingleConjugationEditorDialog extends Dialog[Option[SingleConjugation]] {

  private val rootInfoEditorDialog = RootInfoEditorDialog()
  private val dialogContent = SingleConjugationEditorView()
  private val okButtonType = new ButtonType("OK", ButtonData.OKDone)

  initOwner(JFXApp3.Stage)
  title = "Create / Edit Root Info"
  headerText = "Create or edit single root info."
  dialogPane().buttonTypes = Seq(okButtonType, ButtonType.Cancel)
  dialogPane().content = dialogContent

  resultConverter = dialogButtonType =>
    if dialogButtonType == okButtonType then Option(dialogContent.singleConjugation)
    else None

  private val okButton = dialogPane().lookupButton(okButtonType)

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
