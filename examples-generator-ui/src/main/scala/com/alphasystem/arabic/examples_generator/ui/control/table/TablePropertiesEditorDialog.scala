package com.alphasystem
package arabic
package examples_generator
package ui
package control
package table

import ui.model.TableData
import scalafx.Includes.*
import scalafx.application.JFXApp3
import scalafx.scene.control.ButtonBar.ButtonData
import scalafx.scene.control.{ ButtonType, Dialog }

import scala.util.Try

class TablePropertiesEditorDialog extends Dialog[Option[TableData]] {

  private val dialogContent = TablePropertiesEditorView()
  private val okButtonType = new ButtonType("OK", ButtonData.OKDone)

  initOwner(JFXApp3.Stage)
  title = "Create / Edit Table Properties"
  headerText = "Create or edit table properties."
  dialogPane().buttonTypes = Seq(okButtonType, ButtonType.Cancel)
  dialogPane().content = dialogContent
  dialogPane().lookupButton(okButtonType).disableProperty().bind(dialogContent.enableProperty.not())

  resultConverter = dialogButtonType =>
    if dialogButtonType == okButtonType then
      Some(
        TableData(
          tag = dialogContent.tag,
          columns = Seq.empty,
          tableWidth = Try(dialogContent.tableWidth.toInt).getOrElse(100),
          frame = dialogContent.frame,
          grid = dialogContent.grid,
          role = if dialogContent.role.isBlank then None else Some(dialogContent.role)
        )
      )
    else None

}
