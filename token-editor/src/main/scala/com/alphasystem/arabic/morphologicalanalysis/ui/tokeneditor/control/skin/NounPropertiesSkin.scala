package com.alphasystem
package arabic
package morphologicalanalysis
package ui
package tokeneditor
package control
package skin

import tokeneditor.*
import control.NounPropertiesView
import morphology.model.{ GenderType, NounKind, NounPartOfSpeechType, NounStatus, NounType, NumberType }
import ui.{ ArabicSupportEnumComboBox, ListType }
import javafx.scene.control.SkinBase
import scalafx.Includes.*
import scalafx.collections.ObservableBuffer
import scalafx.geometry.Insets
import scalafx.scene.control.Label
import scalafx.scene.layout.{ BorderPane, GridPane }

class NounPropertiesSkin(control: NounPropertiesView) extends SkinBase[NounPropertiesView](control) {

  getChildren.add(initializeSkin)

  import NounPropertiesSkin.*

  private def initializeSkin = {
    val borderPane = new BorderPane()

    val gridPane = new GridPane() {
      styleClass = ObservableBuffer("border")
      vgap = Gap
      hgap = Gap
      padding = Insets(Gap, Gap, Gap, Gap)
    }

    gridPane.add(Label("Noun Type:"), 0, 0)
    val partOfSpeechTypeComboBox = ArabicSupportEnumComboBox(
      NounPartOfSpeechType.values,
      ListType.LABEL_AND_CODE
    )
    partOfSpeechTypeComboBox
      .valueProperty()
      .bindBidirectional(control.partOfSpeechTypeProperty)
    gridPane.add(partOfSpeechTypeComboBox, 1, 0)

    gridPane.add(Label("Status:"), 0, 1)
    val statusComboBox = ArabicSupportEnumComboBox(
      NounStatus.values,
      ListType.LABEL_AND_CODE
    )
    statusComboBox.valueProperty().bindBidirectional(control.nounStatusProperty)
    gridPane.add(statusComboBox, 1, 1)

    gridPane.add(Label("Number:"), 0, 2)
    val numberTypeComboBox = ArabicSupportEnumComboBox(
      NumberType.values,
      ListType.LABEL_AND_CODE
    )
    numberTypeComboBox
      .valueProperty()
      .bindBidirectional(control.numberTypeProperty)
    gridPane.add(numberTypeComboBox, 1, 2)

    gridPane.add(Label("Gender:"), 0, 3)
    val genderTypeComboBox = ArabicSupportEnumComboBox(
      GenderType.values,
      ListType.LABEL_AND_CODE
    )
    genderTypeComboBox
      .valueProperty()
      .bindBidirectional(control.genderTypeProperty)
    gridPane.add(genderTypeComboBox, 1, 3)

    gridPane.add(Label("Type:"), 0, 4)

    val nounTypeComboBox =
      ArabicSupportEnumComboBox(
        NounType.values,
        ListType.LABEL_AND_CODE
      )
    nounTypeComboBox.valueProperty().bindBidirectional(control.nounTypeProperty)
    gridPane.add(nounTypeComboBox, 1, 4)

    gridPane.add(Label("Kind:"), 0, 5)
    val nounKindComboBox =
      ArabicSupportEnumComboBox(
        NounKind.values,
        ListType.LABEL_AND_CODE
      )
    nounKindComboBox.valueProperty().bindBidirectional(control.nounKindProperty)
    gridPane.add(nounKindComboBox, 1, 5)

    borderPane.center = gridPane

    borderPane
  }
}

object NounPropertiesSkin {

  private val Gap = 10.0

  def apply(control: NounPropertiesView): NounPropertiesSkin =
    new NounPropertiesSkin(control)
}
