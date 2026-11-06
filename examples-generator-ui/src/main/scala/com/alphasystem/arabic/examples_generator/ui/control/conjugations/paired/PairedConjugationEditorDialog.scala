package com.alphasystem
package arabic
package examples_generator
package ui
package control
package conjugations
package paired

import com.alphasystem.arabic.examples_generator.morphologicalchart.{ PairedConjugation, Settings }
import com.alphasystem.arabic.examples_generator.ui.control.settings.SettingsDialog
import com.alphasystem.arabic.fx.ui.util.DefaultIconSize
import de.jensd.fx.glyphs.materialdesignicons.{ MaterialDesignIcon, MaterialDesignIconView }
import javafx.scene.control.Button
import scalafx.application.JFXApp3
import scalafx.scene.control.ButtonBar.ButtonData
import scalafx.scene.control.{ ButtonType, Dialog }
import scalafx.Includes.*
import scalafx.event.ActionEvent

class PairedConjugationEditorDialog extends Dialog[Option[PairedConjugation]] {

  private val dialogContent = new PairedConjugationEditorView()
  private val settingsDialog = SettingsDialog()
  private val okButtonType = new ButtonType("OK", ButtonData.OKDone)
  private val settingsButtonType = new ButtonType("Settings ...", ButtonData.Other)

  initOwner(JFXApp3.Stage)
  title = "Create / Edit Paired Root Info"
  headerText = "Create or edit paired root info."
  dialogPane().buttonTypes = Seq(okButtonType, ButtonType.Cancel, settingsButtonType)
  dialogPane().content = dialogContent

  resultConverter = dialogButtonType =>
    if dialogButtonType == okButtonType then Option(dialogContent.pairedConjugation)
    else None

  private val okButton = dialogPane().lookupButton(okButtonType).asInstanceOf[Button]
  okButton.disableProperty().bind(dialogContent.validatedProperty.not)

  private val settingsButton = dialogPane().lookupButton(settingsButtonType).asInstanceOf[Button]
  settingsButton.setGraphic(new MaterialDesignIconView(MaterialDesignIcon.SETTINGS, DefaultIconSize))
  settingsButton.addEventFilter(
    ActionEvent.Action,
    event =>
      settingsDialog.reset(
        dialogContent.firstConjugationRequest.morphologicalTermType,
        dialogContent.pairedConjugation.settings
      )
      settingsDialog.showAndWait() match {
        case Some(Some(settings: Settings)) => dialogContent.updateSettings(settings)
        case _                              => // do nothing
      }
      event.consume()
  )
}

object PairedConjugationEditorDialog {
  def apply(): PairedConjugationEditorDialog = new PairedConjugationEditorDialog()
}
