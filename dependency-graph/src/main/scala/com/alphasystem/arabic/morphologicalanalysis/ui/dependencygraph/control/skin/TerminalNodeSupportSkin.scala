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

  private var fontSelectorDialog = new FontSelectorDialog(control.translationFont.toFont)
  control.fontProperty.onChange((_, _, nv) => fontSelectorDialog = new FontSelectorDialog(nv.toFont))

  override protected def addProperties(): Unit = {
    super.addProperties()
    addTranslationTextProperty()
    addDoubleProperty(control.translationXProperty, "Translation Text x:")
    addDoubleProperty(control.translationYProperty, "Translation Text y:")
  }

  private def addTranslationTextProperty(): Unit = {
    addNode(Label("Translation Text Font:"))

    val labelText =
      if Option(control.translationText).isDefined && !control.translationText.isBlank then control.translationText
      else "Sample"

    val field = new Label(labelText) {
      font = control.translationFont.toFont
      prefWidth = 100
      tooltip = Tooltip(control.translationFont.toFont.toDisplayText)
    }
    addNode(field)
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
  }
}
