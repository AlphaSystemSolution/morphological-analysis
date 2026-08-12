package com.alphasystem
package arabic
package morphologicalengine
package ui
package control

import fx.ui.util.UIUserPreferences
import morphologicalengine.conjugation.model.NounConjugationGroup
import com.alphasystem.arabic.morphologicalengine.conjugation.model.ConjugationTuple
import skin.NounConjugationGroupViewSkin
import javafx.scene.control.Skin

class NounConjugationGroupView(using preferences: UIUserPreferences)
    extends ConjugationGroupView[NounConjugationGroup] {

  setSkin(createDefaultSkin())

  def isEmpty: Boolean =
    Option(group).isEmpty || (isEmpty(group.nominative) && isEmpty(group.accusative) && isEmpty(group.genitive))

  private def isEmpty(tuple: ConjugationTuple) = Option(tuple).isEmpty && tuple.isEmpty

  override def createDefaultSkin(): Skin[?] = NounConjugationGroupViewSkin(this)
}

object NounConjugationGroupView {
  def apply()(using preferences: UIUserPreferences): NounConjugationGroupView = new NounConjugationGroupView()
}
