package com.alphasystem
package arabic
package morphologicalanalysis
package ui
package dependencygraph
package control
package skin

import morphology.graph.model.RelationshipNode
import scalafx.geometry.Insets
import scalafx.scene.control.TitledPane
import scalafx.scene.layout.GridPane

class RelationshipNodeSkin(control: RelationshipNodeView)
    extends GraphNodeSkin[RelationshipNode, RelationshipNodeView](control) {

  getChildren.addAll(initializeSkin)

  private def createRelationshipProperties = {
    rowIndex = 0

    val gridPane =
      new GridPane() {
        vgap = DefaultGap
        hgap = DefaultGap
        padding = Insets(DefaultGap, DefaultGap, DefaultGap, DefaultGap)
      }

    addDoubleProperty(control.controlX1Property, screenWidth, "Control X1:", gridPane)
    addDoubleProperty(control.controlY1Property, screenHeight, "Control Y1:", gridPane)
    addDoubleProperty(control.controlX2Property, screenWidth, "Control X2:", gridPane)
    addDoubleProperty(control.controlY2Property, screenHeight, "Control Y2:", gridPane)
    addDoubleProperty(control.t1Property, 5, "T1:", gridPane, amountToStepBy = 0.01, roundingF = identity)
    addDoubleProperty(control.t2Property, 5, "T2:", gridPane, amountToStepBy = 0.01, roundingF = identity)
    createTitledPane("Relationship Properties:", gridPane)
  }

  override protected def addProperties(): Seq[TitledPane] = super.addProperties() :+ createRelationshipProperties
}

object RelationshipNodeSkin {
  def apply(control: RelationshipNodeView): RelationshipNodeSkin = new RelationshipNodeSkin(control)
}
