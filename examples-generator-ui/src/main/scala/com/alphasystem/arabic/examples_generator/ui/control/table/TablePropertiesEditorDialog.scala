package com.alphasystem
package arabic
package examples_generator
package ui
package control
package table

import com.alphasystem.arabic.fx.ui.util.createEmptyPanel
import ui.model.TableProperties
import scalafx.Includes.*
import scalafx.application.JFXApp3
import scalafx.collections.ObservableBuffer
import scalafx.event.ActionEvent
import scalafx.scene.control.Alert.AlertType
import scalafx.scene.control.ButtonBar.ButtonData
import scalafx.scene.control.{ Alert, ButtonType, Dialog, Label, TextInputDialog }
import scalafx.scene.layout.{ HBox, VBox }

class TablePropertiesEditorDialog extends Dialog[Option[TableProperties]] {

  private val dialogContent = TablePropertiesEditorView()
  private val okButtonType = new ButtonType("OK", ButtonData.OKDone)

  initOwner(JFXApp3.Stage)
  title = "Create / Edit Table Properties"
  headerText = "Create or edit table properties."
  dialogPane().buttonTypes = Seq(okButtonType, ButtonType.Cancel)
  dialogPane().content = dialogContent

  private val okButton = dialogPane().lookupButton(okButtonType)
  okButton.disableProperty().bind(dialogContent.enableProperty.not())
  okButton.addEventFilter(
    ActionEvent.Action,
    event => {
      val added = dialogContent.addedColumnInfos
      if added.nonEmpty then
        promptInsertPositions(added.size) match {
          case Some(positions) => dialogContent.applyInsertPositions(positions)
          case None            => event.consume()
        }
    }
  )

  resultConverter = dialogButtonType =>
    if dialogButtonType == okButtonType then Some(dialogContent.tableProperties)
    else None

  def setTableProperties(value: TableProperties): Unit = dialogContent.tableProperties = value

  def reset(): Unit = dialogContent.reset()

  private def promptInsertPositions(numOfAddedColumns: Int): Option[Seq[Int]] = {
    val numOfColumns = dialogContent.columnInfos.size
    val headerLabel = new Label {
      text = s"$numOfAddedColumns new column(s) added. Enter a comma-separated 1-based position for each; " +
        s"use -1 to append at the end (e.g. 2, -1 inserts at column 2 and at the end)."
      wrapText = true
      styleClass = ObservableBuffer("bold")
      prefWidth = 600
    }
    val dialog = new TextInputDialog(Seq.fill(numOfAddedColumns)("-1").mkString(", ")) {
      initOwner(JFXApp3.Stage)
      title = "Insert Columns"
      headerText = None
    }
    dialog.getDialogPane.header = new HBox {
      spacing = 5
      private val vBox: VBox = new VBox {
        spacing = 10
        children = Seq(createEmptyPanel(10), headerLabel)
      }
      children = Seq(createEmptyPanel(10), vBox, createEmptyPanel(10))
    }
    dialog.showAndWait() match {
      case Some(input: String) =>
        val positions = input.split(",").toSeq.map(_.trim).map(_.toIntOption)
        if positions.forall(_.isDefined) && positions.size == numOfAddedColumns then {
          val parsed = positions.flatten
          if parsed.forall(p => p == -1 || (p >= 1 && p <= numOfColumns)) then Some(parsed)
          else invalidInput(s"Each position must be -1 or between 1 and $numOfColumns.")
        } else invalidInput(s"Expected $numOfAddedColumns comma-separated integer position(s).")
      case _ => None
    }
  }

  private def invalidInput(message: String): Option[Seq[Int]] = {
    new Alert(AlertType.Error) {
      initOwner(JFXApp3.Stage)
      title = "Invalid Positions"
      headerText = message
    }.showAndWait()
    None
  }
}
