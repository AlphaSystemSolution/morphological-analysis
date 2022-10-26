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

class DependencyGraphVerseSelectionView(serviceFactory: ServiceFactory)
    extends VerseSelectionView(serviceFactory, singleSelect = false) {

  setSkin(createDefaultSkin())

  override def createDefaultSkin(): Skin[_] = DependencyGraphVerseSelectionSkin(this)
}

object DependencyGraphVerseSelectionView {

  def apply(serviceFactory: ServiceFactory): DependencyGraphVerseSelectionView =
    new DependencyGraphVerseSelectionView(serviceFactory)
}
