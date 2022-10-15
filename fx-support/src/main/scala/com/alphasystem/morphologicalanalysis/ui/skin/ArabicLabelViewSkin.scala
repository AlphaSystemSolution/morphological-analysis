package com.alphasystem.morphologicalanalysis.ui.skin

import com.alphasystem.morphologicalanalysis.ui.ArabicLabelView
import javafx.scene.control.SkinBase
import scalafx.scene.text.TextAlignment
import scalafx.Includes.*
import scalafx.collections.ObservableBuffer
import scalafx.scene.Node
import scalafx.scene.layout.{ Region, StackPane }
import scalafx.scene.shape.Rectangle
import scalafx.scene.text.Text
import scalafx.scene.paint.Color

class ArabicLabelViewSkin(control: ArabicLabelView)
    extends SkinBase[ArabicLabelView](control) {

  getChildren.add(initializeSkin)

  private def initializeSkin: StackPane = {
    val label = new Text() {
      font = control.font
      stroke = control.stroke
      textAlignment = TextAlignment.Right
    }

    label.fontProperty().bind(control.fontProperty)
    label.textProperty().bind(control.textProperty)
    label.strokeProperty().bind(control.strokeProperty)
    label.onMouseClicked = e => {
      control.select = !control.selected
      e.consume()
    }

    val rect = new Rectangle() {
      fill = Color.Transparent
      strokeWidth = 1.0
      arcWidth = 6.0
      arcHeight = 6.0
    }

    rect.widthProperty().bind(control.widthProperty())
    rect.heightProperty().bind(control.heightProperty())
    control
      .selectedProperty
      .onChange((_, _, nv) => {
        val stroke =
          if control.isDisabled then control.disabledStroke
          else {
            if nv then control.selectedStroke
            else control.unselectedStroke
          }
        rect.stroke = stroke

        rect.strokeWidth = if nv then 2.0 else 1.0
      })

    rect.onMouseClicked = e => {
      control.select = !control.selected
      e.consume()
    }

    control
      .disabledProperty()
      .onChange((_, _, nv) => {
        val strokeColor =
          if nv then control.disabledStroke
          else {
            if control.selected then control.selectedStroke
            else control.unselectedStroke
          }
        rect.stroke = strokeColor
      })

    new StackPane() {
      alignment = control.alignment
      focusTraversable = true
      prefWidth = control.getWidth
      prefHeight = control.getHeight
      minWidth = Region.USE_PREF_SIZE
      minHeight = Region.USE_PREF_SIZE
      maxWidth = Region.USE_PREF_SIZE
      maxHeight = Region.USE_PREF_SIZE
      disable <== control.disabledProperty()
      alignment <== control.alignmentProperty
      children = ObservableBuffer[Node](rect, label)
    }
  }
}

object ArabicLabelViewSkin {

  def apply(control: ArabicLabelView): ArabicLabelViewSkin =
    new ArabicLabelViewSkin(control)
}
