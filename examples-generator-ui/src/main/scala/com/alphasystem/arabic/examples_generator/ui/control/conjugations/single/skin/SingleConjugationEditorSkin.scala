package com.alphasystem
package arabic
package examples_generator
package ui
package control
package conjugations
package single
package skin

import arabic.fx.ui.util.*
import arabic.morphologicalanalysis.ui.{ ArabicSupportEnumComboBox, ListType }
import arabic.morphologicalengine.conjugation.model.MorphologicalTermType
import com.alphasystem.arabic.model.ProNoun
import ui.model.VerbTranslationResult
import javafx.scene.control.SkinBase
import scalafx.scene.layout.{ BorderPane, GridPane }
import scalafx.Includes.*
import scalafx.geometry.NodeOrientation
import scalafx.scene.control.{ Button, TextField }
import scalafx.scene.text.{ Text, TextFlow }

private class SingleConjugationEditorSkin(control: SingleConjugationEditorView)
    extends SkinBase[SingleConjugationEditorView](control) {

  private var previousTranslations = Map.empty[ProNoun, Seq[String]]
  private val verbalTranslationsDialog = VerbalTranslationsDialog()
  private val morphologicalTermComboBox =
    ArabicSupportEnumComboBox[MorphologicalTermType](MorphologicalTermType.values, ListType.LABEL_ONLY)
  morphologicalTermComboBox.valueProperty().bindBidirectional(control.morphologicalTermTypeProperty)
  morphologicalTermComboBox.nodeOrientation = NodeOrientation.RightToLeft

  private val tagField = new TextField()
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

  private val translationsTextField = new TextField() {
    text = "0 of 14 values are set"
    editable = false
  }

  control
    .conjugationInputProperty
    .onChange((_, _, nv) => {
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
    styleClass = Seq("border")

    private var row = 0
    private val tagLabel = createLabel("Tag: ")
    add(tagLabel, 0, row)
    add(tagField, 1, row)
    tagField.textProperty().bindBidirectional(control.tagProperty)
    control
      .showTagProperty
      .onChange((_, _, nv) => {
        tagLabel.visible = nv
        tagField.visible = nv
      })

    row += 1

    add(createLabel("Morphological Term Type: "), 0, row)
    add(morphologicalTermComboBox, 1, row)

    row += 1
    add(createLabel("Root Letters: "), 0, row)
    add(createRootLettersLabel, 1, row)

    row += 1
    add(createLabel("Family: "), 0, row)
    add(createFamilyLabel, 1, row)

    row += 1
    add(createLabel("Translations: "), 0, row)
    add(translationsTextField, 1, row)
    private val button = new Button() {
      text = " ... "
      onAction = _ => {
        verbalTranslationsDialog.update(control.translationResult.translations)
        verbalTranslationsDialog.showAndWait() match {
          case Some(Some(result: VerbTranslationResult)) =>
            control.translationResult = result
            previousTranslations = result.translations
            updateTranslationFieldText(control.morphologicalTermType, result)
          case _ => // do nothing
        }
      }

    }
    control
      .morphologicalTermTypeProperty
      .onChange((_, _, nv) => {
        val isNounBasedType = MorphologicalTermType.NounBasedTypes.contains(nv)
        button.disable = isNounBasedType
        control.translationResult =
          if isNounBasedType then VerbTranslationResult() else VerbTranslationResult(previousTranslations)
        updateTranslationFieldText(nv, control.translationResult)
      })
    add(button, 2, row)
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

  private def updateTranslationFieldText(
    morphologicalTermType: MorphologicalTermType,
    result: VerbTranslationResult
  ): Unit = {
    val isNounBasedType = MorphologicalTermType.NounBasedTypes.contains(morphologicalTermType)
    val text =
      if isNounBasedType then "Unsupported"
      else s"${result.sanitizedTranslations.count(_._2.nonEmpty)} of ${ProNoun.values.length} values are set"
    translationsTextField.text = text
  }

}

object SingleConjugationEditorSkin {
  private[single] def apply(control: SingleConjugationEditorView): SingleConjugationEditorSkin =
    new SingleConjugationEditorSkin(control)
}
