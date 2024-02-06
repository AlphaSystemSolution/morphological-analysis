package com.alphasystem
package arabic
package morphologicalanalysis
package ui
package dependencygraph
package control

import morphologicalanalysis.morphology.graph.model.PhraseInfo
import morphologicalanalysis.morphology.model.{ NounStatus, PhraseType }
import scalafx.Includes.*
import scalafx.application.JFXApp3
import scalafx.collections.ObservableBuffer
import scalafx.scene.control.ButtonBar.ButtonData
import scalafx.scene.control.{ ButtonType, Dialog }

class CreatePhraseDialog extends Dialog[CreatePhraseResult] {

  private[control] val locationIdsProperty = ObservableBuffer.empty[(Long, Int)]

  private val dialogContent = CreatePhraseView()
  private val okButtonType = new ButtonType("OK", ButtonData.OKDone)

  initOwner(JFXApp3.Stage)
  title = "Create Phrase"
  headerText = "Select phrase type and status"
  dialogPane().buttonTypes = Seq(okButtonType, ButtonType.Cancel)
  dialogPane().content = dialogContent

  private val okButton = dialogPane().lookupButton(okButtonType)
  okButton.disable = true
  okButton.disableProperty().bind(dialogContent.invalidSelectionProperty)

  resultConverter = dialogButtonType =>
    if dialogButtonType == okButtonType then
      CreatePhraseResult(
        Some(
          PhraseInfo(
            text = derivePhraseInfoText(phraseTypes, nounStatus),
            phraseTypes = phraseTypes,
            locations = locationIds,
            status = nounStatus
          )
        )
      )
    else CreatePhraseResult()

  def displayText: String = dialogContent.displayText
  def displayText_=(value: String): Unit = dialogContent.displayText = value

  def nounStatus: Option[NounStatus] = dialogContent.nounStatus
  def nounStatus_=(value: Option[NounStatus]): Unit = dialogContent.nounStatus = value

  def locationIds: Seq[(Long, Int)] = locationIdsProperty.toSeq
  def locationIds_=(values: Seq[(Long, Int)]): Unit = {
    locationIdsProperty.clear()
    locationIdsProperty.addAll(values)
  }

  def phraseTypes: Seq[PhraseType] = dialogContent.selectedPhraseTypes
  def phraseTypes_=(values: Seq[PhraseType]): Unit = dialogContent.selectedPhraseTypes = values
}

object CreatePhraseDialog {
  def apply(): CreatePhraseDialog = new CreatePhraseDialog()
}
