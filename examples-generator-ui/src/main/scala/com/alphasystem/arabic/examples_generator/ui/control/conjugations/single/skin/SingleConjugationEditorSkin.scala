package com.alphasystem
package arabic
package examples_generator
package ui
package control
package conjugations
package single
package skin

import com.alphasystem.arabic.fx.ui.util.{ DefaultGap, DefaultInsets, createLabel }
import com.alphasystem.arabic.morphologicalanalysis.ui.{ ArabicSupportEnumComboBox, ListType }
import com.alphasystem.arabic.morphologicalengine.conjugation.model.MorphologicalTermType
import javafx.scene.control.SkinBase
import scalafx.scene.layout.{ BorderPane, GridPane }
import scalafx.Includes.*
import scalafx.geometry.NodeOrientation

private class SingleConjugationEditorSkin(control: SingleConjugationEditorView)
    extends SkinBase[SingleConjugationEditorView](control) {

  private val morphologicalTermComboBox =
    ArabicSupportEnumComboBox[MorphologicalTermType](MorphologicalTermType.values, ListType.LABEL_ONLY)
  morphologicalTermComboBox.valueProperty().bindBidirectional(control.morphologicalTermTypeProperty)
  morphologicalTermComboBox.nodeOrientation = NodeOrientation.RightToLeft

  private val gridPane = new GridPane {
    hgap = DefaultGap
    vgap = DefaultGap
    padding = DefaultInsets

    private var row = 0
    add(createLabel("Morphological Term Type: "), 0, row)
    add(morphologicalTermComboBox, 1, row)

    row += 1
  }

  private val mainPanel = new BorderPane() {
    center = gridPane
  }

  getChildren.add(mainPanel)
}

object SingleConjugationEditorSkin {
  private[single] def apply(control: SingleConjugationEditorView): SingleConjugationEditorSkin =
    new SingleConjugationEditorSkin(control)
}
