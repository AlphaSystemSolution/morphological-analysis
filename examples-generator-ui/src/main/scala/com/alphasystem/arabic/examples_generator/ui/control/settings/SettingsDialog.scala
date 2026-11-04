package com.alphasystem
package arabic
package examples_generator
package ui
package control
package settings

import arabic.morphologicalengine.conjugation.model.MorphologicalTermType
import examples_generator.morphologicalchart.Settings
import scalafx.Includes.*
import scalafx.application.JFXApp3
import scalafx.scene.control.ButtonBar.ButtonData
import scalafx.scene.control.{ ButtonType, Dialog }

class SettingsDialog extends Dialog[Option[Settings]] {

  private val dialogContent = SettingsView()
  private val okButtonType = new ButtonType("OK", ButtonData.OKDone)

  initOwner(JFXApp3.Stage)
  title = "Settings"
  headerText = "Create or edit settings."
  dialogPane().buttonTypes = Seq(okButtonType, ButtonType.Cancel)
  dialogPane().content = dialogContent

  resultConverter = dialogButtonType =>
    if dialogButtonType == okButtonType then Option(dialogContent.settings)
    else None

  def reset(morphologicalTermType: MorphologicalTermType, settings: Settings): Unit = {
    dialogContent.morphologicalTermType = morphologicalTermType
    dialogContent.settings = settings
  }
}

object SettingsDialog {
  def apply(): SettingsDialog = new SettingsDialog()
}
