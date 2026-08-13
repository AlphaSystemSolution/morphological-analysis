package com.alphasystem
package arabic
package morphologicalengine
package ui
package control
package skin

import arabic.fx.ui.util.UIUserPreferences
import morphologicalengine.conjugation.model.{ MorphologicalTermType, VerbConjugationGroup, VerbConjugationGroupPair }

class VerbConjugationGroupPairSkin(control: VerbConjugationGroupPairView)(using preferences: UIUserPreferences)
    extends ConjugationGroupPairSkin[
      VerbConjugationGroup,
      VerbConjugationGroup,
      VerbConjugationGroupPair,
      VerbConjugationGroupPairView
    ](control) {

  override protected def createRightView(
    termType: MorphologicalTermType,
    term: VerbConjugationGroup
  ): VerbConjugationGroupView = verbView(termType, term)

  override protected def createLeftView(
    termType: MorphologicalTermType,
    term: VerbConjugationGroup
  ): VerbConjugationGroupView = verbView(termType, term)

  private def verbView(termType: MorphologicalTermType, term: VerbConjugationGroup) = {
    val view = VerbConjugationGroupView()
    view.term = termType.title
    view.group = term
    view
  }
}
