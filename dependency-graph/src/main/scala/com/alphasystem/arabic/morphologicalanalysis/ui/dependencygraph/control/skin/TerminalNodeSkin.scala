package com.alphasystem
package arabic
package morphologicalanalysis
package ui
package dependencygraph
package control
package skin

import morphology.graph.model.TerminalNode
import org.controlsfx.dialog.FontSelectorDialog
import scalafx.geometry.Insets
import scalafx.scene.control.TitledPane
import scalafx.scene.layout.GridPane

class TerminalNodeSkin(control: TerminalNodeView)
    extends LineSupportSkin[TerminalNode, TerminalNodeView](control) {

  getChildren.addAll(initializeSkin)

  private var fontSelectorDialog: FontSelectorDialog = _
  control.translationFontProperty.onChange((_, _, nv) => fontSelectorDialog = new FontSelectorDialog(nv.toFont))

  private def createTranslationProperties = {
    rowIndex = 0
    fontSelectorDialog = new FontSelectorDialog(control.translationFont.toFont)

    val gridPane =
      new GridPane() {
        vgap = DefaultGap
        hgap = DefaultGap
        padding = Insets(DefaultGap, DefaultGap, DefaultGap, DefaultGap)
      }

    addTextAndFontSelectionProperty(
      "Translation Text Font:",
      control.translationTextProperty,
      control.translationFontProperty,
      fontSelectorDialog,
      gridPane
    )
    addDoubleProperty(control.translationXProperty, screenWidth, "Translation Text x:", gridPane)
    addDoubleProperty(control.translationYProperty, screenHeight, "Translation Text y:", gridPane)
    createTitledPane("Translation Properties:", gridPane)
  }

  override protected def addProperties(): Seq[TitledPane] = super.addProperties() :+ createTranslationProperties
}

object TerminalNodeSkin {
  def apply(control: TerminalNodeView): TerminalNodeSkin = new TerminalNodeSkin(control)
}
