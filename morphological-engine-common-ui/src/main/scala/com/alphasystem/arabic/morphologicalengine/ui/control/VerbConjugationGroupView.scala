package com.alphasystem
package arabic
package morphologicalengine
package ui
package control

import fx.ui.util.UIUserPreferences
import morphologicalengine.conjugation.model.VerbConjugationGroup
import javafx.scene.control.Skin

class VerbConjugationGroupView(val imperativeAndForbidden: Boolean)(using preferences: UIUserPreferences)
    extends ConjugationGroupView[VerbConjugationGroup] {

  setSkin(createDefaultSkin())

  override def createDefaultSkin(): Skin[?] = skin.VerbConjugationGroupSkin(this)
}

object VerbConjugationGroupView {
  def apply(imperativeAndForbidden: Boolean)(using preferences: UIUserPreferences): VerbConjugationGroupView =
    new VerbConjugationGroupView(imperativeAndForbidden)
}
