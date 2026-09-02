package com.alphasystem
package arabic
package morphologicalengine
package ui
package control

import fx.ui.util.UIUserPreferences
import morphologicalengine.conjugation.model.VerbConjugationGroup
import com.alphasystem.arabic.morphologicalengine.conjugation.model.ConjugationTuple
import javafx.scene.control.Skin

class VerbConjugationGroupView(using preferences: UIUserPreferences)
    extends ConjugationGroupView[VerbConjugationGroup] {

  setSkin(createDefaultSkin())

  def isEmpty: Boolean =
    Option(group).isEmpty || (
      isEmpty(group.masculineSecondPerson) && isEmpty(group.feminineSecondPerson) &&
        group.masculineThirdPerson.forall(isEmpty) &&
        group.feminineThirdPerson.forall(isEmpty) &&
        group.firstPerson.forall(isEmpty)
    )

  private def isEmpty(tuple: ConjugationTuple) = Option(tuple).isEmpty || tuple.isEmpty

  override def createDefaultSkin(): Skin[?] = skin.VerbConjugationGroupSkin(this)
}

object VerbConjugationGroupView {
  def apply()(using preferences: UIUserPreferences): VerbConjugationGroupView = new VerbConjugationGroupView()
}
