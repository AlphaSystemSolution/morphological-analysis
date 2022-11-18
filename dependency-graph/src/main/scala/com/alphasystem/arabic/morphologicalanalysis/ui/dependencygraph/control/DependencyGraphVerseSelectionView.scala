package com.alphasystem
package arabic
package morphologicalanalysis
package ui
package dependencygraph
package control

import commons.service.ServiceFactory
import commons.control.VerseSelectionView
import skin.DependencyGraphVerseSelectionSkin
import javafx.scene.control.Skin
import scalafx.beans.property.BooleanProperty

@deprecated(message = "Moving to new graph dialog", since = "1")
class DependencyGraphVerseSelectionView(serviceFactory: ServiceFactory)
    extends VerseSelectionView(serviceFactory, singleSelect = false) {

  val clearSelectionProperty: BooleanProperty = BooleanProperty(false)

  setSkin(createDefaultSkin())

  def clearSelection: Boolean = clearSelectionProperty.value
  def clearSelection_=(value: Boolean): Unit = clearSelectionProperty.value = value

  override def createDefaultSkin(): Skin[_] = DependencyGraphVerseSelectionSkin(this)
}

object DependencyGraphVerseSelectionView {

  @deprecated(message = "Moving to new graph dialog", since = "1")
  def apply(serviceFactory: ServiceFactory): DependencyGraphVerseSelectionView =
    new DependencyGraphVerseSelectionView(serviceFactory)
}
