package com.alphasystem
package arabic
package examples_generator
package ui
package control
package column

import ui.model.ColumnData
import scalafx.Includes.*
import scalafx.application.JFXApp3
import scalafx.scene.control.ButtonBar.ButtonData
import scalafx.scene.control.{ ButtonType, Dialog }

class ColumnEditorDialog extends Dialog[Option[ColumnData]] {

  private val dialogContent = ColumnEditorView()
  private val okButtonType = new ButtonType("OK", ButtonData.OKDone)

  initOwner(JFXApp3.Stage)
  title = "Create / Edit Column"
  headerText = "Create or edit column data."
  dialogPane().buttonTypes = Seq(okButtonType, ButtonType.Cancel)
  dialogPane().content = dialogContent

  resultConverter = dialogButtonType =>
    if dialogButtonType == okButtonType then Some(dialogContent.columnData)
    else None

  def setColumnData(columnData: ColumnData): Unit = dialogContent.columnData = columnData

  def reset(): Unit = dialogContent.reset()
}
