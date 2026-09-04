package com.alphasystem
package arabic
package morphologicalengine
package ui
package control

import arabic.fx.ui.util.UIUserPreferences
import morphologicalengine.conjugation.model.{ VerbConjugationGroup, VerbConjugationGroupPair }
import javafx.scene.control.Skin

class VerbConjugationGroupPairView(using preferences: UIUserPreferences)
    extends ConjugationGroupPairView[VerbConjugationGroup, VerbConjugationGroup, VerbConjugationGroupPair] {

  setSkin(createDefaultSkin())

  override def createDefaultSkin(): Skin[?] = skin.VerbConjugationGroupPairSkin(this)
}

object VerbConjugationGroupPairView {
  def apply()(using preferences: UIUserPreferences): VerbConjugationGroupPairView = new VerbConjugationGroupPairView()
}
