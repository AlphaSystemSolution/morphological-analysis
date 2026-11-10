package com.alphasystem
package arabic
package examples_generator
package ui
package control
package conjugations
package full

import arabic.morphologicalengine.generator.model.ConjugationTemplate
import javafx.scene.control.Button
import scalafx.Includes.*
import scalafx.application.JFXApp3
import scalafx.scene.control.ButtonBar.ButtonData
import scalafx.scene.control.{ ButtonType, Dialog }

class ConjugationTemplateConfigurationEditorDialog extends Dialog[Option[ConjugationTemplate]] {

  private val dialogContent = ConjugationTemplateConfigurationEditorView()
  private val okButtonType = new ButtonType("OK", ButtonData.OKDone)

  initOwner(JFXApp3.Stage)
  title = "Create / Edit Template Configuration"
  headerText = "Create or edit conjugation template configuration."
  dialogPane().buttonTypes = Seq(okButtonType, ButtonType.Cancel)
  dialogPane().content = dialogContent

  private val okButton = dialogPane().lookupButton(okButtonType).asInstanceOf[Button]
  okButton.disableProperty().bind(dialogContent.tagProperty.isEmpty)

  resultConverter = dialogButtonType =>
    if dialogButtonType == okButtonType then Option(dialogContent.conjugationTemplate)
    else None

  def reset(conjugationTemplate: ConjugationTemplate): Unit = dialogContent.conjugationTemplate = conjugationTemplate
}

object ConjugationTemplateConfigurationEditorDialog {
  def apply(): ConjugationTemplateConfigurationEditorDialog = new ConjugationTemplateConfigurationEditorDialog()
}
