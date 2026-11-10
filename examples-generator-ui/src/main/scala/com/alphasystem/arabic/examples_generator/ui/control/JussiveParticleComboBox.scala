package com.alphasystem
package arabic
package examples_generator
package ui
package control

import ui.model.JussiveParticle
import arabic.fx.ui.util.EmDash
import javafx.geometry.NodeOrientation
import javafx.scene.control.{ ComboBox, ContentDisplay, ListCell, ListView }
import scalafx.collections.ObservableBuffer

class JussiveParticleComboBox extends ComboBox[JussiveParticle] {

  import JussiveParticleComboBox.*

  setItems(ObservableBuffer.from(JussiveParticle.values))
  setCellFactory((_: ListView[JussiveParticle]) => new EnumListCell())
  setButtonCell(new EnumListCell())
  getSelectionModel.select(0)
  setPrefWidth(150)
  setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT)
}

object JussiveParticleComboBox {
  def apply(): JussiveParticleComboBox = new JussiveParticleComboBox()

  private class EnumListCell extends ListCell[JussiveParticle] {
    setContentDisplay(ContentDisplay.TEXT_ONLY)

    override def updateItem(item: JussiveParticle, empty: Boolean): Unit = {
      super.updateItem(item, empty)
      setText(null)
      setFont(preferences.arabicFont(20))

      if !empty && Option(item).isDefined then {
        val text =
          item match {
            case JussiveParticle.NoValue => EmDash.toString
            case _                       => item.arabicJussiveParticle.map(_.word.unicode).getOrElse(EmDash.toString)
          }
        setText(text)
      }
    }
  }
}
