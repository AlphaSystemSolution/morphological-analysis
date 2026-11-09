package com.alphasystem
package arabic
package examples_generator
package ui
package control
package conjugation_selector
package skin

import examples_generator.morphologicalchart.PairedConjugation
import control.root_info.RootInfoEditorView
import ui.model.VerbTranslationResult
import arabic.fx.ui.util.{ DefaultGap, DefaultInsets, createLabel }
import arabic.model.ProNoun
import arabic.morphologicalanalysis.ui.{ ArabicSupportEnumComboBox, ListType }
import arabic.morphologicalengine.conjugation.model.MorphologicalTermType
import javafx.scene.control.SkinBase
import scalafx.Includes.*
import scalafx.beans.property.DoubleProperty
import scalafx.collections.ObservableBuffer
import scalafx.geometry.{ NodeOrientation, Pos }
import scalafx.scene.control.{ Button, TextField }
import scalafx.scene.layout.{ BorderPane, ColumnConstraints, GridPane, VBox }

private class ConjugationTermSelectorSkin(control: ConjugationTermSelectorView)
    extends SkinBase[ConjugationTermSelectorView](control) {

  private val sharedLabelWidth = DoubleProperty(200.0)
  private val rootInfoView = RootInfoEditorView()
  rootInfoView.labelColumnWidthProperty <== sharedLabelWidth
  private val morphologicalTermComboBox = createMorphologicalTermComboBox
  private var previousTranslations = Map.empty[ProNoun, Seq[String]]
  private val verbalTranslationsDialog = VerbalTranslationsDialog()
  private val translationsTextField = new TextField() {
    text = "0 of 14 values are set"
    editable = false
  }

  private val gridPane = new GridPane {
    hgap = DefaultGap
    vgap = DefaultGap
    padding = DefaultInsets
    columnConstraints = Seq(
      new ColumnConstraints {
        minWidth <== sharedLabelWidth
        prefWidth <== sharedLabelWidth
        maxWidth <== sharedLabelWidth
      },
      new ColumnConstraints()
    )

    var row = 0
    add(createLabel("Morphological Term Type: "), 0, row)
    add(morphologicalTermComboBox, 1, row)

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

  private val mainPanel = {
    val vbox = new VBox()
    vbox.children.addAll(gridPane, rootInfoView)
    new BorderPane() {
      center = vbox
      BorderPane.setAlignment(vbox, Pos.Center)
    }
  }

  getChildren.add(mainPanel)

  private lazy val createMorphologicalTermComboBox = {
    val morphologicalTermComboBox =
      ArabicSupportEnumComboBox[MorphologicalTermType](MorphologicalTermType.values, ListType.LABEL_ONLY)
    morphologicalTermComboBox.valueProperty().bindBidirectional(control.morphologicalTermTypeProperty)
    morphologicalTermComboBox.nodeOrientation = NodeOrientation.RightToLeft

    control
      .otherMorphologicalTermTypeProperty
      .onChange((_, _, nv) => updateTermTypeItems(morphologicalTermComboBox, Option(nv).flatten))
    updateTermTypeItems(morphologicalTermComboBox, Option(control.otherMorphologicalTermType).flatten)

    morphologicalTermComboBox
  }

  private def updateTermTypeItems(
    morphologicalTermComboBox: ArabicSupportEnumComboBox[MorphologicalTermType],
    maybeOther: Option[MorphologicalTermType]
  ): Unit = {
    val items = maybeOther match {
      case Some(term) =>
        val similar = PairedConjugation.getSimilarTypes(term)
        if similar.nonEmpty then similar else MorphologicalTermType.values.toSeq
      case None => MorphologicalTermType.values.toSeq
    }
    morphologicalTermComboBox.items = ObservableBuffer.from(items)
    val termType = control.morphologicalTermType
    if items.contains(termType) then morphologicalTermComboBox.getSelectionModel.select(termType)
    else morphologicalTermComboBox.value = termType
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

object ConjugationTermSelectorSkin {
  private[conjugation_selector] def apply(control: ConjugationTermSelectorView): ConjugationTermSelectorSkin =
    new ConjugationTermSelectorSkin(control)
}
