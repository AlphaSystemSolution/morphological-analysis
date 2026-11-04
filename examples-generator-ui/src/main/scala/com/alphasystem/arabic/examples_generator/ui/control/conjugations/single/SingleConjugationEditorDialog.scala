package com.alphasystem
package arabic
package examples_generator
package ui
package control
package conjugations
package single

import control.root_info.RootInfoEditorDialog
import arabic.morphologicalengine.conjugation.model.ConjugationInput
import control.settings.SettingsDialog
import arabic.fx.ui.util.DefaultIconSize
import de.jensd.fx.glyphs.materialdesignicons.{ MaterialDesignIcon, MaterialDesignIconView }
import examples_generator.morphologicalchart.{ Settings, SingleConjugation }
import javafx.scene.control.Button
import scalafx.Includes.*
import scalafx.application.JFXApp3
import scalafx.event.ActionEvent
import scalafx.scene.control.ButtonBar.ButtonData
import scalafx.scene.control.{ ButtonType, Dialog }

class SingleConjugationEditorDialog extends Dialog[Option[SingleConjugation]] {

  private val rootInfoEditorDialog = RootInfoEditorDialog()
  private val dialogContent = SingleConjugationEditorView()
  private val settingsDialog = SettingsDialog()
  private val okButtonType = new ButtonType("OK", ButtonData.OKDone)
  private val selectRootButtonType = new ButtonType("Select Root ...", ButtonData.Other)
  private val settingsButtonType = new ButtonType("Settings ...", ButtonData.Other)

  dialogContent
    .conjugationInputProperty
    .onChange((_, _, nv) => if Option(nv).isDefined then rootInfoEditorDialog.setConjugationInput(nv))

  initOwner(JFXApp3.Stage)
  title = "Create / Edit Conjugation"
  headerText = "Create or edit single conjugation info."
  dialogPane().buttonTypes = Seq(okButtonType, ButtonType.Cancel, selectRootButtonType, settingsButtonType)
  dialogPane().content = dialogContent

  resultConverter = dialogButtonType =>
    if dialogButtonType == okButtonType then Option(dialogContent.singleConjugation)
    else None

  private val selectRootButton = dialogPane().lookupButton(selectRootButtonType)
  selectRootButton.addEventFilter(
    ActionEvent.Action,
    event =>
      rootInfoEditorDialog.showAndWait() match {
        case Some(Some(conjugationInput: ConjugationInput)) => dialogContent.conjugationInput = conjugationInput
        case _                                              => // do nothing
      }
      event.consume()
  )

  private val settingsButton = dialogPane().lookupButton(settingsButtonType).asInstanceOf[Button]
  settingsButton.setGraphic(new MaterialDesignIconView(MaterialDesignIcon.SETTINGS, DefaultIconSize))
  settingsButton.addEventFilter(
    ActionEvent.Action,
    event =>
      settingsDialog.reset(
        morphologicalTermType = dialogContent.morphologicalTermType,
        settings = dialogContent.singleConjugation.settings
      )
      settingsDialog.showAndWait() match {
        case Some(Some(settings: Settings)) => dialogContent.settings = settings
        case _                              => // do nothing
      }
      event.consume()
  )

  def reset(showTagField: Boolean = true, maybeSingleConjugation: Option[SingleConjugation] = None): Unit =
    dialogContent.showTag = showTagField
    dialogContent.singleConjugation = maybeSingleConjugation.orNull
}

object SingleConjugationEditorDialog {
  def apply(): SingleConjugationEditorDialog = new SingleConjugationEditorDialog()
}
