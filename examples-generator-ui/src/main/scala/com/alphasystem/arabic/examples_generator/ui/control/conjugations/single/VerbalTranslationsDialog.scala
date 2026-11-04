package com.alphasystem
package arabic
package examples_generator
package ui
package control
package conjugations
package single

import ui.model.VerbTranslationResult
import arabic.model.ProNoun
import scalafx.Includes.*
import scalafx.application.JFXApp3
import scalafx.scene.control.ButtonBar.ButtonData
import scalafx.scene.control.{ ButtonType, Dialog }

class VerbalTranslationsDialog extends Dialog[Option[VerbTranslationResult]] {

  private val dialogContent = VerbTranslationsView()
  private val okButtonType = new ButtonType("OK", ButtonData.OKDone)

  initOwner(JFXApp3.Stage)
  title = "Create / Edit Verbal Translations"
  headerText = "Create or edit verbal translations."
  dialogPane().buttonTypes = Seq(okButtonType, ButtonType.Cancel)
  dialogPane().content = dialogContent

  resultConverter = dialogButtonType =>
    if dialogButtonType == okButtonType then Option(VerbTranslationResult.fromStringValues(dialogContent.translations))
    else None

  def update(translationsMap: Map[ProNoun, Seq[String]]): Unit =
    dialogContent.update(VerbTranslationResult(translationsMap).sanitizedTranslations)
}

object VerbalTranslationsDialog {
  def apply(): VerbalTranslationsDialog = new VerbalTranslationsDialog()
}
