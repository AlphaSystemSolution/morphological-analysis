package com.alphasystem
package arabic
package morphologicalengine
package ui
package control
package skin

import arabic.fx.ui.util.UIUserPreferences
import morphologicalengine.conjugation.model.{ MorphologicalTermType, NounConjugationGroup, NounConjugationGroupPair }

class NounConjugationGroupPairSkin(control: NounConjugationGroupPairView)(using preferences: UIUserPreferences)
    extends ConjugationGroupPairSkin[
      NounConjugationGroup,
      NounConjugationGroup,
      NounConjugationGroupPair,
      NounConjugationGroupPairView
    ](control) {

  override protected def createRightView(
    termType: MorphologicalTermType,
    term: NounConjugationGroup
  ): NounConjugationGroupView = nounView(termType, term)

  override protected def createLeftView(
    termType: MorphologicalTermType,
    term: NounConjugationGroup
  ): NounConjugationGroupView = nounView(termType, term)

  private def nounView(termType: MorphologicalTermType, term: NounConjugationGroup): NounConjugationGroupView = {
    val view = NounConjugationGroupView()
    view.term = termType.title
    view.group = term
    view
  }
}
