package com.alphasystem
package arabic
package morphologicalanalysis
package ui
package dependencygraph
package control

import dependencygraph.utils.TerminalNodeInput
import morphologicalanalysis.morphology.model.Chapter
import ui.commons.service.ServiceFactory
import morphologicalanalysis.morphology.graph.model.TerminalNode
import scalafx.application.JFXApp3
import scalafx.Includes.*
import scalafx.beans.property.{ BooleanProperty, ObjectProperty }
import scalafx.scene.control.ButtonBar.ButtonData
import scalafx.scene.control.{ ButtonType, Dialog }

class AddNodeDialog(serviceFactory: ServiceFactory) extends Dialog[Option[TerminalNodeInput]] {

  private val showReferenceTypeProperty: BooleanProperty = BooleanProperty(true)
  private val currentChapterProperty = new ObjectProperty[Chapter](this, "currentChapter")

  private val dialogContent = AddNodeView(serviceFactory)
  private val okButtonType = new ButtonType("OK", ButtonData.OKDone)
  dialogContent.currentChapterProperty.bind(currentChapterProperty)
  dialogContent.showReferenceTypeProperty.bind(showReferenceTypeProperty)

  initOwner(JFXApp3.Stage)
  title = "Open Dependency Graph"
  headerText = "Select type of the node to add"
  dialogPane().buttonTypes = Seq(okButtonType, ButtonType.Cancel)
  dialogPane().content = dialogContent

  resultConverter = dialogButtonType =>
    if dialogButtonType == okButtonType then
      Some(TerminalNodeInput(graphNodeType = dialogContent.selectedType, token = dialogContent.selectedToken.userData))
    else None

  def showReferenceType: Boolean = showReferenceTypeProperty.value
  def showReferenceType_=(value: Boolean): Unit = showReferenceTypeProperty.value = value

  def currentChapter: Chapter = currentChapterProperty.value
  def currentChapter_=(value: Chapter): Unit = currentChapterProperty.value = value
}

object AddNodeDialog {
  def apply(serviceFactory: ServiceFactory): AddNodeDialog = new AddNodeDialog(serviceFactory)
}
