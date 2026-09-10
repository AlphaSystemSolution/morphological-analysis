package com.alphasystem
package arabic
package morphologicalengine
package ui
package control

import fx.ui.util.UIUserPreferences
import morphologicalengine.conjugation.model.NounConjugationGroup
import javafx.scene.control.Skin

class NounConjugationGroupView(using preferences: UIUserPreferences)
    extends ConjugationGroupView[NounConjugationGroup] {

  setSkin(createDefaultSkin())

  override def createDefaultSkin(): Skin[?] = skin.NounConjugationGroupSkin(this)
}

object NounConjugationGroupView {
  def apply()(using preferences: UIUserPreferences): NounConjugationGroupView = new NounConjugationGroupView()
}
