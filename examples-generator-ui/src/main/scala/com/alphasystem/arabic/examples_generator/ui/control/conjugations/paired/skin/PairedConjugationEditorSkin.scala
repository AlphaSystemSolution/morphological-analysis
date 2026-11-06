package com.alphasystem
package arabic
package examples_generator
package ui
package control
package conjugations
package paired
package skin

import com.alphasystem.arabic.examples_generator.ui.control.conjugations.single.SingleConjugationEditorDialog
import com.alphasystem.arabic.fx.ui.util.{ DefaultGap, DefaultInsets, EmDash, createLabel }
import com.alphasystem.arabic.morphologicalengine.conjugation.model.{
  MorphologicalTermType,
  NamedTemplate,
  RootLetters
}
import javafx.scene.control.SkinBase
import scalafx.geometry.NodeOrientation.RightToLeft
import scalafx.geometry.Orientation
import scalafx.scene.control.{ Label, Separator, TextField }
import scalafx.scene.layout.{ BorderPane, GridPane }
import scalafx.scene.paint.Color

private class PairedConjugationEditorSkin(control: PairedConjugationEditorView)
    extends SkinBase[PairedConjugationEditorView](control) {

  private val dialog = SingleConjugationEditorDialog()

  private val tagField = new TextField() {
    prefColumnCount = 25
  }
  private val firstMorphologicalTermTypeField = createArabicLabel
  private val secondMorphologicalTermTypeField = createArabicLabel
  private val firstRootLettersField = createArabicLabel
  private val secondRootLettersField = createArabicLabel
  private val firstFamilyField = createArabicLabel
  private val secondFamilyField = createArabicLabel

  control
    .firstConjugationRequestPropertyWrapper
    .onChange((_, _, nv) => {
      if Option(nv).isDefined then {
        val conjugationInput = nv.conjugationInput
        firstMorphologicalTermTypeField.text = nv.morphologicalTermType.word.unicode
        firstRootLettersField.text = conjugationInput.rootLetters.rawString
        updateFamilyLabel(conjugationInput.namedTemplate, firstFamilyField)
      }
    })

  control
    .secondConjugationRequestPropertyWrapper
    .onChange((_, _, nv) => {
      if Option(nv).isDefined then {
        val secondTermType = nv.morphologicalTermType
        val conjugationInput = nv.conjugationInput
        updateSecondMorphologicalTerm(secondTermType)
        updateSecondRootLetters(conjugationInput.rootLetters)
        updateSecondFamily(conjugationInput.namedTemplate)
      } else {
        highlightError(secondMorphologicalTermTypeField, "Select morphological term of similar type")
        highlightError(secondRootLettersField, "Select new roots")
        highlightError(secondFamilyField, "Select new roots")
      }
    })

  private val gridPane = new GridPane {
    hgap = DefaultGap
    vgap = DefaultGap
    padding = DefaultInsets
    styleClass = Seq("border")

    private var row = 0
    add(createLabel("Tag: "), 0, row)
    add(tagField, 1, row)
    tagField.textProperty().bindBidirectional(control.tagProperty)

    row += 1
    add(createDescriptionLabel("First Root Detail"), 0, row)

    row += 1
    add(Separator(Orientation.Horizontal), 0, row, 2, 1)

    row += 1
    add(createLabel("Morphological Term Type: "), 0, row)
    add(firstMorphologicalTermTypeField, 1, row)

    row += 1
    add(createLabel("Root Letters:"), 0, row)
    add(firstRootLettersField, 1, row)

    row += 1
    add(createLabel("Family:"), 0, row)
    add(firstFamilyField, 1, row)

    row += 1
    add(Separator(Orientation.Horizontal), 0, row, 2, 1)

    row += 1
    add(createDescriptionLabel("Second Root Detail"), 0, row)

    row += 1
    add(createLabel("Morphological Term Type: "), 0, row)
    add(secondMorphologicalTermTypeField, 1, row)

    row += 1
    add(createLabel("Root Letters:"), 0, row)
    add(secondRootLettersField, 1, row)

    row += 1
    add(createLabel("Family:"), 0, row)
    add(secondFamilyField, 1, row)
  }

  private val mainPanel = new BorderPane() {
    center = gridPane
  }

  getChildren.add(mainPanel)

  private def createDescriptionLabel(text: String): Label = {
    val label = createLabel(text)
    label.font = preferences.englishFont(16)
    label.textFill = Color.Teal
    label
  }

  private def createArabicLabel =
    new Label() {
      font = preferences.arabicFont(20)
    }

  private def updateFamilyLabel(family: NamedTemplate, label: Label): Unit = {
    label.text = s"${family.form} $EmDash ${family.word.unicode}"
    label.nodeOrientation = RightToLeft
  }

  private def updateSecondMorphologicalTerm(termType: MorphologicalTermType): Unit =
    if control.hasSimilarTypes then {
      secondMorphologicalTermTypeField.text = termType.word.unicode
      secondMorphologicalTermTypeField.font = preferences.arabicFont(20)
      secondMorphologicalTermTypeField.textFill = Color.Black
      secondMorphologicalTermTypeField.style = null
    } else highlightError(secondMorphologicalTermTypeField, "Select morphological term of similar type")

  private def updateSecondRootLetters(rootLetters: RootLetters): Unit =
    if control.hasSimilarTypes then {
      secondRootLettersField.text = rootLetters.rawString
      secondRootLettersField.font = preferences.arabicFont(20)
      secondRootLettersField.textFill = Color.Black
      secondRootLettersField.style = null
    } else highlightError(secondRootLettersField, "Select new roots")

  private def updateSecondFamily(family: NamedTemplate): Unit =
    if control.hasSimilarTypes then {
      updateFamilyLabel(family, secondFamilyField)
      secondFamilyField.font = preferences.arabicFont(20)
      secondFamilyField.textFill = Color.Black
      secondFamilyField.style = null
    } else highlightError(secondFamilyField, "Select familySelect new roots")

  private def highlightError(label: Label, text: String): Unit = {
    label.text = text
    label.font = preferences.englishFont(12)
    label.textFill = Color.Red
    label.style = "-fx-font-weight: bold;"
  }
}

object PairedConjugationEditorSkin {
  private[paired] def apply(control: PairedConjugationEditorView): PairedConjugationEditorSkin =
    new PairedConjugationEditorSkin(control)
}
