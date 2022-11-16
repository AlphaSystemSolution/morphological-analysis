package com.alphasystem
package arabic
package morphologicalanalysis
package ui
package dependencygraph
package control

import NewGraphDialog.Result
import com.alphasystem.arabic.morphologicalanalysis.morphology.model.Token
import commons.service.ServiceFactory
import scalafx.Includes.*
import scalafx.application.JFXApp3
import scalafx.collections.ObservableBuffer
import scalafx.scene.control.ButtonBar.ButtonData
import scalafx.scene.control.{ ButtonType, Dialog }

import scala.Console

class NewGraphDialog(serviceFactory: ServiceFactory) extends Dialog[Option[Result]] {

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

  resultConverter = dialogButtonType =>
    if dialogButtonType == createButtonType then {
      val allSelectedTokens = dialogContent.allSelectedTokens.toSeq
      if allSelectedTokens.isEmpty then None
      else Some(Result(allSelectedTokens.map(_.userData)))
    } else None

}

object NewGraphDialog {

  def apply(serviceFactory: ServiceFactory): NewGraphDialog = new NewGraphDialog(serviceFactory)

  case class Result(tokens: Seq[Token])
}
