package com.alphasystem
package arabic
package examples_generator
package ui
package control
package file
package skin

import com.alphasystem.arabic.fx.ui.util.*
import javafx.collections.ListChangeListener
import javafx.scene.control.SkinBase
import scalafx.geometry.Pos
import scalafx.scene.control.ComboBox
import scalafx.scene.layout.{ BorderPane, GridPane }

import scala.jdk.CollectionConverters.*

class ExampleFileGeneratorSkin private (control: ExampleFileGeneratorView)
    extends SkinBase[ExampleFileGeneratorView](control) {

  private val tableTagComboBox = ComboBox[String](control.tableTagsProperty)

  tableTagComboBox
    .getItems
    .addListener(new ListChangeListener[String] {
      override def onChanged(change: ListChangeListener.Change[? <: String]): Unit = {
        while change.next do {
          if change.wasAdded then {
            tableTagComboBox.getSelectionModel.select(change.getList.asScala.head)
          }
        }
      }
    })

  private val gridPane = {
    val gridPane = new GridPane {
      vgap = DefaultGap
      hgap = DefaultGap
      alignment = Pos.Center
    }

    var row = 0
    gridPane.add(createLabel("Tags:"), 0, row)
    gridPane.add(tableTagComboBox, 1, row)

    row += 1

    gridPane
  }

  private val mainPanel = new BorderPane {
    center = gridPane
    BorderPane.setAlignment(gridPane, Pos.Center)
  }

  getChildren.addAll(mainPanel)
}

object ExampleFileGeneratorSkin {
  private[file] def apply(control: ExampleFileGeneratorView) = new ExampleFileGeneratorSkin(control)
}
