package com.alphasystem
package arabic
package examples_generator
package ui
package control
package conjugations
package single
package skin

import arabic.fx.ui.util.*
import arabic.morphologicalanalysis.ui.{ArabicSupportEnumComboBox, ListType}
import arabic.morphologicalengine.conjugation.model.MorphologicalTermType
import javafx.scene.control.SkinBase
import scalafx.scene.layout.{BorderPane, GridPane}
import scalafx.Includes.*
import scalafx.geometry.NodeOrientation
import scalafx.scene.text.{Text, TextFlow}

private class SingleConjugationEditorSkin(control: SingleConjugationEditorView)
    extends SkinBase[SingleConjugationEditorView](control) {

  private val morphologicalTermComboBox =
    ArabicSupportEnumComboBox[MorphologicalTermType](MorphologicalTermType.values, ListType.LABEL_ONLY)
  morphologicalTermComboBox.valueProperty().bindBidirectional(control.morphologicalTermTypeProperty)
  morphologicalTermComboBox.nodeOrientation = NodeOrientation.RightToLeft

  private val rootLettersText = new Text() {
    font = preferences.arabicFont(24)
    text = ""
  }

  private val familyNumberLabel = new Text() {
    font = preferences.englishFont(20)
    text = ""
  }

  private val familyNameLabel = new Text() {
    font = preferences.arabicFont(24)
    text = ""
  }

  control.conjugationInputProperty.onChange((_,_,nv) => {
    if Option(nv).isDefined then {
      rootLettersText.text = nv.rootLetters.rawString
      familyNumberLabel.text = nv.namedTemplate.form
      familyNameLabel.text = nv.namedTemplate.word.unicode
    }
  })

  private val gridPane = new GridPane {
    hgap = DefaultGap
    vgap = DefaultGap
    padding = DefaultInsets

    private var row = 0
    add(createLabel("Morphological Term Type: "), 0, row)
    add(morphologicalTermComboBox, 1, row)

    row += 1
    add(createLabel("Root Letters: "), 0, row)
    add(createRootLettersLabel, 1, row)

    row += 1
    add(createLabel("Family: "), 0, row)
    add(createFamilyLabel, 1, row)
  }

  private val mainPanel = new BorderPane() {
    center = gridPane
  }

  getChildren.add(mainPanel)

  private def createRootLettersLabel =
    new TextFlow() {
      nodeOrientation = NodeOrientation.RightToLeft
      children = List(rootLettersText)
    }

  private def createFamilyLabel = {
    val separatorBeginLabel = new Text(s" $EmDash (") {
      font = preferences.englishFont(20)
    }
    val separatorEndLabel = new Text(")") {
      font = preferences.englishFont(20)
    }
    new TextFlow() {
      nodeOrientation = NodeOrientation.RightToLeft
      children = List(familyNumberLabel, separatorBeginLabel, familyNameLabel, separatorEndLabel)
    }
  }

}

object SingleConjugationEditorSkin {
  private[single] def apply(control: SingleConjugationEditorView): SingleConjugationEditorSkin =
    new SingleConjugationEditorSkin(control)
}
