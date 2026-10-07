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

class CreateColumnDialog extends Dialog[Option[ColumnData]] {

  private val dialogContent = CreateColumnView()
  private val okButtonType = new ButtonType("OK", ButtonData.OKDone)

  initOwner(JFXApp3.Stage)
  title = "Create / Edit Column"
  headerText = "Create or edit column data."
  dialogPane().buttonTypes = Seq(okButtonType, ButtonType.Cancel)
  dialogPane().content = dialogContent

  resultConverter = dialogButtonType =>
    if dialogButtonType == okButtonType then
      Some(
        ColumnData(
          `type` = dialogContent.columnType,
          text = dialogContent.text,
          settings = None,
          highlights = Seq.empty
        )
      )
    else None
}
