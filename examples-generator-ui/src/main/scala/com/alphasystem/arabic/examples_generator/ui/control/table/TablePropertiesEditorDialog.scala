package com.alphasystem
package arabic
package examples_generator
package ui
package control
package table

import ui.model.TableProperties
import scalafx.Includes.*
import scalafx.application.JFXApp3
import scalafx.scene.control.ButtonBar.ButtonData
import scalafx.scene.control.{ ButtonType, Dialog }

class TablePropertiesEditorDialog extends Dialog[Option[TableProperties]] {

  private val dialogContent = TablePropertiesEditorView()
  private val okButtonType = new ButtonType("OK", ButtonData.OKDone)

  initOwner(JFXApp3.Stage)
  title = "Create / Edit Table Properties"
  headerText = "Create or edit table properties."
  dialogPane().buttonTypes = Seq(okButtonType, ButtonType.Cancel)
  dialogPane().content = dialogContent
  dialogPane().lookupButton(okButtonType).disableProperty().bind(dialogContent.enableProperty.not())

  resultConverter = dialogButtonType =>
    if dialogButtonType == okButtonType then Some(dialogContent.tableProperties)
    else None

  def setTableProperties(value: TableProperties): Unit = dialogContent.tableProperties = value

  def reset(): Unit = dialogContent.reset()
}
