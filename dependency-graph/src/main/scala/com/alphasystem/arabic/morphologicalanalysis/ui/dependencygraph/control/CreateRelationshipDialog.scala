package com.alphasystem
package arabic
package morphologicalanalysis
package ui
package dependencygraph
package control

import com.alphasystem.arabic.morphologicalanalysis.morphology.graph.model.{ RelationshipInfoOld, RelationshipLinkOld }
import morphologicalanalysis.morphology.model.{ Linkable, RelationshipType }
import scalafx.Includes.*
import scalafx.application.JFXApp3
import scalafx.scene.control.ButtonBar.ButtonData
import scalafx.scene.control.{ ButtonType, Dialog }

class CreateRelationshipDialog extends Dialog[CreateRelationshipResult] {

  private val dialogContent = CreateRelationshipView()
  private val okButtonType = new ButtonType("OK", ButtonData.OKDone)

  initOwner(JFXApp3.Stage)
  title = "Create Relationship"
  headerText = "Select relationship type"
  dialogPane().buttonTypes = Seq(okButtonType, ButtonType.Cancel)
  dialogPane().content = dialogContent

  private val okButton = dialogPane().lookupButton(okButtonType)
  okButton.disable = true
  okButton.disableProperty().bind(dialogContent.invalidSelectionProperty)

  resultConverter = dialogButtonType => {
    if dialogButtonType == okButtonType then
      CreateRelationshipResult(
        text = Some(dialogContent.text),
        relationshipType = dialogContent.relationshipType
      )
    else CreateRelationshipResult()
  }

  def relationshipType: RelationshipType = dialogContent.relationshipType
  def relationshipType_=(value: RelationshipType): Unit = dialogContent.relationshipType = value

  def ownerNode: Linkable = dialogContent.owner
  def ownerNode_=(value: Linkable): Unit = dialogContent.owner = value

  def dependentNode: Linkable = dialogContent.dependent
  def dependentNode_=(value: Linkable): Unit = dialogContent.dependent = value
}

object CreateRelationshipDialog {
  def apply() = new CreateRelationshipDialog()
}
