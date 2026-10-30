package com.alphasystem
package arabic
package examples_generator
package ui
package control
package morphological_chart
package skin

import javafx.scene.control.SkinBase

class ConjugationsFileGeneratorSkin(control: ConjugationsFileGeneratorView)
    extends SkinBase[ConjugationsFileGeneratorView](control) {}

object ConjugationsFileGeneratorSkin {
  def apply(control: ConjugationsFileGeneratorView): ConjugationsFileGeneratorSkin =
    new ConjugationsFileGeneratorSkin(control)
}
