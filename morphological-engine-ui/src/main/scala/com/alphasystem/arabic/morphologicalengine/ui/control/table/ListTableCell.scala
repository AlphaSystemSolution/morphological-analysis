package com.alphasystem
package arabic
package morphologicalengine
package ui
package control
package table

import arabic.model.ArabicSupport
import com.alphasystem.arabic.morphologicalanalysis.ui.ArabicSupportGroupPane
import javafx.scene.control.TableCell
import scalafx.Includes.*
import scalafx.collections.ObservableBuffer
import scalafx.geometry.{ NodeOrientation, Pos }
import scalafx.scene.Group
import scalafx.scene.control.ContentDisplay
import scalafx.scene.text.TextFlow
import scalafx.stage.Popup

abstract class ListTableCell[T <: ArabicSupport](values: Seq[T]) extends TableCell[TableModel, Seq[T]] {

  private val groupPane = ArabicSupportGroupPane(values = values)
  private val popup = new Popup() {
    autoHide = true
    onHiding = event => {
      commitEdit(groupPane.selectedValues)
      event.consume()
    }
    onAutoHide = event => {
      commitEdit(groupPane.selectedValues)
      event.consume()
    }
    content.addOne(groupPane)
  }

  setContentDisplay(ContentDisplay.GraphicOnly)
  setAlignment(Pos.Center)
  setNodeOrientation(NodeOrientation.RightToLeft)

  override def startEdit(): Unit = {
    super.startEdit()
    val bounds = localToScreen(getBoundsInLocal)
    popup.show(this, bounds.getMinX, bounds.getMinY + bounds.getHeight)
  }

  override def updateItem(item: Seq[T], empty: Boolean): Unit = {
    super.updateItem(item, empty)

    val group =
      if Option(item).isDefined && item.nonEmpty && !empty then {
        groupPane.reset(item)
        val textFlow = new TextFlow() {
          children.addOne(createLabel(item.head))
          item.tail.foreach { v =>
            children.addAll(createSpaceWithAndLabel(), createLabel(v))
          }
        }
        new Group() {
          children = textFlow
        }
      } else null

    setGraphic(group)
  }
}
