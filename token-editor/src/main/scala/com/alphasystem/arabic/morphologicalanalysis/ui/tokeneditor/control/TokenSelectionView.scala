package com.alphasystem
package arabic
package morphologicalanalysis
package ui
package tokeneditor
package control

import com.alphasystem.arabic.morphologicalanalysis.morphology.model.Token
import commons.control.VerseSelectionView
import commons.service.ServiceFactory
import skin.TokenSelectionSkin
import javafx.scene.control.Skin
import scalafx.beans.property.BooleanProperty

class TokenSelectionView(serviceFactory: ServiceFactory)
    extends VerseSelectionView(serviceFactory, singleSelect = true) {

  val clearSelectionProperty: BooleanProperty = BooleanProperty(false)

  setSkin(createDefaultSkin())

  private def clearSelection: Boolean = clearSelectionProperty.value
  private def clearSelection_=(value: Boolean): Unit = clearSelectionProperty.value = value

  def doClearSelection(): Unit = {
    clearSelection = false
    clearSelection = true
  }

  def refresh(oldToken: Token, updatedToken: Token): Unit = {
    tokensProperty.replaceAll(oldToken.toArabicLabel, updatedToken.toArabicLabel)
    selectedToken = updatedToken.toArabicLabel
  }

  override def createDefaultSkin(): Skin[?] = TokenSelectionSkin(this)
}

object TokenSelectionView {

  def apply(serviceFactory: ServiceFactory): TokenSelectionView = new TokenSelectionView(serviceFactory)
}
