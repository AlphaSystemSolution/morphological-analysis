package com.alphasystem
package arabic
package morphologicalanalysis
package ui
package dependencygraph
package control
package skin

import morphology.graph.model.TerminalNodeSupport
import org.controlsfx.dialog.FontSelectorDialog
import scalafx.Includes.*

import scala.jdk.OptionConverters.*

abstract class TerminalNodeSupportSkin[N <: TerminalNodeSupport, C <: TerminalNodeSupportView[N]](control: C)
    extends LineSupportSkin[N, C](control) {

  private var fontSelectorDialog: FontSelectorDialog = _
  control.translationFontProperty.onChange((_, _, nv) => fontSelectorDialog = new FontSelectorDialog(nv.toFont))

  override protected def addProperties(): Unit = {
    super.addProperties()
    fontSelectorDialog = new FontSelectorDialog(control.translationFont.toFont)
    addTextAndFontSelectionProperty(
      "Translation Text Font:",
      control.translationText,
      control.translationFont,
      control.translationFontProperty,
      fontSelectorDialog
    )
    addDoubleProperty(control.translationXProperty, "Translation Text x:")
    addDoubleProperty(control.translationYProperty, "Translation Text y:")
  }
}
