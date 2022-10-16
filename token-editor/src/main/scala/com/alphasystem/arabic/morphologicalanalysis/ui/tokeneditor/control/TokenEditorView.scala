package com.alphasystem.arabic.morphologicalanalysis.ui.tokeneditor.control

import com.alphasystem.arabic.morphologicalanalysis.morphology.model.Token
import com.alphasystem.arabic.morphologicalanalysis.ui.tokeneditor.service.ServiceFactory
import javafx.scene.control.{ Control, Skin }
import scalafx.beans.property.ObjectProperty
import com.alphasystem.arabic.morphologicalanalysis.ui.tokeneditor.control.skin.TokenEditorSkin

class TokenEditorView(serviceFactory: ServiceFactory) extends Control {

  setSkin(createDefaultSkin())

  override def createDefaultSkin(): Skin[_] =
    new TokenEditorSkin(this, serviceFactory)
}

object TokenEditorView {

  def apply(serviceFactory: ServiceFactory): TokenEditorView =
    new TokenEditorView(serviceFactory)
}
