package com.alphasystem
package arabic
package morphologicalanalysis
package ui
package dependencygraph
package control

import morphology.graph.model.DependencyGraph
import commons.service.ServiceFactory
import javafx.application.Platform
import javafx.beans.binding.Bindings
import scalafx.Includes.*
import scalafx.application.JFXApp3
import scalafx.scene.control.ButtonBar.ButtonData
import scalafx.scene.control.{ ButtonType, Dialog }

class DependencyGraphOpenDialog(serviceFactory: ServiceFactory) extends Dialog[OpenDialogResult] {

  private val dialogContent = DependencyGraphSelectionView(serviceFactory)
  private val openButtonType = new ButtonType("Open", ButtonData.OKDone)

  initOwner(JFXApp3.Stage)
  title = "Open Dependency Graph"
  headerText = "Select Chapter and Verse to view available Dependency Graphs"
  dialogPane().buttonTypes = Seq(openButtonType, ButtonType.Cancel)
  dialogPane().content = dialogContent

  private val openButton = dialogPane().lookupButton(openButtonType)
  openButton.disable = true

  dialogContent.selectedGraphProperty.onChange((_, _, nv) => openButton.setDisable(Option(nv).isEmpty))

  resultConverter = dialogButtonType =>
    if dialogButtonType == openButtonType then
      OpenDialogResult(
        chapter = Option(dialogContent.selectedChapter.userData),
        dependencyGraph = Option(dialogContent.selectedGraph).map(_.userData)
      )
    else OpenDialogResult()

}

object DependencyGraphOpenDialog {
  def apply(serviceFactory: ServiceFactory): DependencyGraphOpenDialog = new DependencyGraphOpenDialog(serviceFactory)
}
