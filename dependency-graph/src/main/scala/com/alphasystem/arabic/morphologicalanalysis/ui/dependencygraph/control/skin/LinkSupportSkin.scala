package com.alphasystem
package arabic
package morphologicalanalysis
package ui
package dependencygraph
package control
package skin

import morphology.graph.model.LinkSupport
import scalafx.geometry.Insets
import scalafx.scene.control.TitledPane
import scalafx.scene.layout.GridPane

abstract class LinkSupportSkin[N <: LinkSupport, C <: LinkSupportView[N]](control: C)
    extends LineSupportSkin[N, C](control) {

  private def createLinkProperties = {
    rowIndex = 0
    val gridPane =
      new GridPane() {
        vgap = DefaultGap
        hgap = DefaultGap
        padding = Insets(DefaultGap, DefaultGap, DefaultGap, DefaultGap)
      }

    addDoubleProperty(control.cxProperty, screenWidth, "Circle x:", gridPane)
    addDoubleProperty(control.cyProperty, screenHeight, "Circle y:", gridPane)
    createTitledPane("Link Properties:", gridPane)
  }

  override protected def addProperties(): Seq[TitledPane] = super.addProperties() :+ createLinkProperties
}
