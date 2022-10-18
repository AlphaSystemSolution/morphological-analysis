package com.alphasystem.arabic.morphologicalanalysis.ui.tokeneditor.control

import com.alphasystem.arabic.morphologicalanalysis.morphology.model.Token
import com.alphasystem.arabic.morphologicalanalysis.ui.tokeneditor.service.ServiceFactory
import javafx.scene.control.{ Control, Skin }
import scalafx.beans.property.ObjectProperty
import com.alphasystem.arabic.morphologicalanalysis.ui.tokeneditor.control.skin.TokenEditorSkin
import scalafx.beans.property.StringProperty

class TokenEditorView(serviceFactory: ServiceFactory) extends Control {

  val titleProperty: StringProperty = StringProperty("")

  setSkin(createDefaultSkin())

  def title: String = titleProperty.value
  def title_=(value: String): Unit = titleProperty.value = value

  override def createDefaultSkin(): Skin[_] =
    new TokenEditorSkin(this, serviceFactory)
}

object TokenEditorView {

  def apply(serviceFactory: ServiceFactory): TokenEditorView =
    new TokenEditorView(serviceFactory)
}
