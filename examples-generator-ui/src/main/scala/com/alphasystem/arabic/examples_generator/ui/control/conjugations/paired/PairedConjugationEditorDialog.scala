package com.alphasystem
package arabic
package examples_generator
package ui
package control
package conjugations
package paired

import com.alphasystem.arabic.examples_generator.morphologicalchart.{ PairedConjugation, Settings, SingleConjugation }
import com.alphasystem.arabic.examples_generator.ui.control.conjugations.single.SingleConjugationEditorDialog
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
  private val selectRootDialog = SingleConjugationEditorDialog()
  private val settingsDialog = SettingsDialog()
  private val okButtonType = new ButtonType("OK", ButtonData.OKDone)
  private val selectFirstRootButtonType = new ButtonType("Select First Root ...", ButtonData.Other)
  private val selectSecondRootButtonType = new ButtonType("Select Second Root ...", ButtonData.Other)
  private val settingsButtonType = new ButtonType("Settings ...", ButtonData.Other)

  initOwner(JFXApp3.Stage)
  title = "Create / Edit Paired Root Info"
  headerText = "Create or edit paired root info."
  dialogPane().buttonTypes =
    Seq(okButtonType, ButtonType.Cancel, selectFirstRootButtonType, selectSecondRootButtonType, settingsButtonType)
  dialogPane().content = dialogContent

  resultConverter = dialogButtonType =>
    if dialogButtonType == okButtonType then Option(dialogContent.pairedConjugation)
    else None

  private val okButton = dialogPane().lookupButton(okButtonType).asInstanceOf[Button]
  okButton.disableProperty().bind(dialogContent.validatedProperty.not)

  private val selectFirstRootButton = dialogPane().lookupButton(selectFirstRootButtonType)
  selectFirstRootButton.addEventFilter(
    ActionEvent.Action,
    event =>
      event.consume()
      selectRootDialog.reset(
        pairedConjugationMode = false,
        maybeSingleConjugation = Some(
          SingleConjugation(
            tag = "",
            settings = Settings(),
            request = dialogContent.firstConjugationRequest
          )
        )
      )
      selectRootDialog.showAndWait() match {
        case Some(Some(singleConjugation: SingleConjugation)) =>
          dialogContent.firstConjugationRequest = singleConjugation.request.copy()
        case _ => // do nothing
      }
  )

  private val selectSecondRootButton = dialogPane().lookupButton(selectSecondRootButtonType)
  selectSecondRootButton.addEventFilter(
    ActionEvent.Action,
    event =>
      event.consume()
      selectRootDialog.reset(
        pairedConjugationMode = false,
        maybeSingleConjugation = Some(
          SingleConjugation(
            tag = "",
            settings = Settings(),
            request = Option(dialogContent.secondConjugationRequest).getOrElse(dialogContent.firstConjugationRequest)
          )
        ),
        otherMorphologicalTermType = Some(dialogContent.firstConjugationRequest.morphologicalTermType)
      )
      selectRootDialog.showAndWait() match {
        case Some(Some(singleConjugation: SingleConjugation)) =>
          dialogContent.secondConjugationRequest = singleConjugation.request.copy()
        case _ => // do nothing
      }
  )

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

  def reset(maybePairedConjugation: Option[PairedConjugation] = None): Unit =
    dialogContent.reset(maybePairedConjugation)
}

object PairedConjugationEditorDialog {
  def apply(): PairedConjugationEditorDialog = new PairedConjugationEditorDialog()
}
