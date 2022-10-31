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
import scalafx.scene.control.{ Button, Label, Tooltip }

import scala.jdk.OptionConverters.*

abstract class TerminalNodeSupportSkin[N <: TerminalNodeSupport, C <: TerminalNodeSupportView[N]](control: C)
    extends LineSupportSkin[N, C](control) {

  private var fontSelectorDialog: FontSelectorDialog = _
  control.translationFontProperty.onChange((_, _, nv) => fontSelectorDialog = new FontSelectorDialog(nv.toFont))

  override protected def addProperties(): Unit = {
    super.addProperties()
    fontSelectorDialog = new FontSelectorDialog(control.translationFont.toFont)
    addTranslationTextProperty()
    addDoubleProperty(control.translationXProperty, "Translation Text x:")
    addDoubleProperty(control.translationYProperty, "Translation Text y:")
  }

  private def addTranslationTextProperty(): Unit = {
    addNode(Label("Translation Text Font:"))

    val translationText = control.translationText
    val labelText =
      if Option(translationText).isDefined && !translationText.isBlank then translationText
      else "Sample"

    val field = new Label(labelText) {
      font = control.translationFont.toFont
      prefWidth = 100
      tooltip = Tooltip(control.translationFont.toFont.toDisplayText)
    }
    gridPane.add(field, 0, rowIndex)
    control
      .translationFontProperty
      .onChange((_, _, nv) => {
        val font = nv.toFont
        field.setFont(font)
        field.setTooltip(Tooltip(font.toDisplayText))
      })

    val button = new Button() {
      text = " ... "
      onAction = event => {
        fontSelectorDialog.showAndWait().toScala match
          case Some(updateFont) =>
            control.font = updateFont.toFontMetaInfo
            field.setFont(updateFont)
            field.setTooltip(Tooltip(updateFont.toDisplayText))

          case None => ()
        event.consume()
      }
    }
    gridPane.add(button, 1, rowIndex)
    rowIndex += 1
  }
}
