package com.alphasystem
package arabic
package morphologicalanalysis
package ui
package tokeneditor
package control

import commons.control.VerseSelectionView
import commons.service.ServiceFactory
import skin.TokenSelectionSkin
import javafx.scene.control.Skin
import scalafx.beans.property.BooleanProperty

class TokenSelectionView(serviceFactory: ServiceFactory)
    extends VerseSelectionView(serviceFactory, singleSelect = true) {

  val clearSelectionProperty: BooleanProperty = BooleanProperty(false)

  setSkin(createDefaultSkin())

  def clearSelection: Boolean = clearSelectionProperty.value
  def clearSelection_=(value: Boolean): Unit = clearSelectionProperty.value = value

  override def createDefaultSkin(): Skin[_] = TokenSelectionSkin(this)
}

object TokenSelectionView {

  def apply(serviceFactory: ServiceFactory): TokenSelectionView = new TokenSelectionView(serviceFactory)
}
