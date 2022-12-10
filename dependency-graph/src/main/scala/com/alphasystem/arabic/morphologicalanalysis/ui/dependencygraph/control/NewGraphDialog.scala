package com.alphasystem
package arabic
package morphologicalanalysis
package ui
package dependencygraph
package control

import morphology.model.{ Chapter, Token }
import commons.service.ServiceFactory
import scalafx.Includes.*
import scalafx.application.JFXApp3
import scalafx.collections.ObservableBuffer
import scalafx.scene.control.ButtonBar.ButtonData
import scalafx.scene.control.{ ButtonType, Dialog }

class NewGraphDialog(serviceFactory: ServiceFactory) extends Dialog[NewDialogResult] {

  private val dialogContent = NewGraphSelectionView(serviceFactory)
  private val createButtonType = new ButtonType("Create", ButtonData.OKDone)

  initOwner(JFXApp3.Stage)
  title = "Create Dependency Graph"
  headerText = "Select Chapter, Verse(s), and Tokens to create new graph"
  dialogPane().buttonTypes = Seq(createButtonType, ButtonType.Cancel)
  dialogPane().content = dialogContent

  private val createButton = dialogPane().lookupButton(createButtonType)
  // TODO: disable / enable create button
  // createButton.disable = true

  resultConverter = dialogButtonType => {
    val result =
      if dialogButtonType == createButtonType then
        NewDialogResult(
          Some(dialogContent.selectedChapter.userData),
          dialogContent.allSelectedTokens.toSeq.map(_.userData)
        )
      else NewDialogResult()

    dialogContent.clearAll()
    result
  }

}

object NewGraphDialog {

  def apply(serviceFactory: ServiceFactory): NewGraphDialog = new NewGraphDialog(serviceFactory)

}
