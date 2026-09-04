package com.alphasystem
package arabic
package morphologicalengine
package ui
package control

import arabic.fx.ui.util.UIUserPreferences
import morphologicalengine.conjugation.model.{ NounConjugationGroup, NounConjugationGroupPair }
import javafx.scene.control.Skin

class NounConjugationGroupPairView(using preferences: UIUserPreferences)
    extends ConjugationGroupPairView[NounConjugationGroup, NounConjugationGroup, NounConjugationGroupPair] {

  setSkin(createDefaultSkin())

  override def createDefaultSkin(): Skin[?] = skin.NounConjugationGroupPairSkin(this)
}

object NounConjugationGroupPairView {
  def apply()(using preferences: UIUserPreferences): NounConjugationGroupPairView = new NounConjugationGroupPairView()
}
