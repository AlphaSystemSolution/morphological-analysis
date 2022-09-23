package com.alphasystem.arabic.morphologicalanalysis.ui.tokeneditor.control.skin

import com.alphasystem.arabic.morphologicalanalysis.ui.tokeneditor.*
import com.alphasystem.arabic.morphologicalanalysis.ui.tokeneditor.control.NounPropertiesView
import com.alphasystem.morphologicalanalysis.morphology.model.*
import com.alphasystem.morphologicalanalysis.ui.{
  ArabicSupportEnumComboBox,
  ListType
}
import javafx.scene.control.SkinBase
import scalafx.Includes.*
import scalafx.collections.ObservableBuffer
import scalafx.geometry.Insets
import scalafx.scene.control.Label
import scalafx.scene.layout.{ BorderPane, GridPane }

class NounPropertiesSkin(control: NounPropertiesView)
    extends SkinBase[NounPropertiesView](control) {

  getChildren.add(initializeSkin)

  import NounPropertiesSkin.*

  private def initializeSkin = {
    val borderPane = new BorderPane()

    val gridPane = new GridPane()
    gridPane.styleClass = ObservableBuffer("border")
    gridPane.vgap = Gap
    gridPane.hgap = Gap
    gridPane.padding = Insets(Gap, Gap, Gap, Gap)

    val skin = getSkinnable

    gridPane.add(Label("Part of Speech"), 0, 0)
    val partOfSpeechTypeComboBox = ArabicSupportEnumComboBox(
      NounPartOfSpeechType.values,
      ListType.LABEL_AND_CODE
    )
    partOfSpeechTypeComboBox
      .valueProperty()
      .bindBidirectional(skin.partOfSpeechTypeProperty)
    gridPane.add(partOfSpeechTypeComboBox, 1, 0)

    gridPane.add(Label("Status"), 0, 1)
    val statusComboBox = ArabicSupportEnumComboBox(
      NounStatus.values,
      ListType.LABEL_AND_CODE
    )
    statusComboBox.valueProperty().bindBidirectional(skin.nounStatusProperty)
    gridPane.add(statusComboBox, 1, 1)

    gridPane.add(Label("Number"), 0, 2)
    val numberTypeComboBox = ArabicSupportEnumComboBox(
      NumberType.values,
      ListType.LABEL_AND_CODE
    )
    numberTypeComboBox
      .valueProperty()
      .bindBidirectional(skin.numberTypeProperty)
    gridPane.add(numberTypeComboBox, 1, 2)

    gridPane.add(Label("Gender"), 0, 3)
    val genderTypeComboBox = ArabicSupportEnumComboBox(
      GenderType.values,
      ListType.LABEL_AND_CODE
    )
    genderTypeComboBox
      .valueProperty()
      .bindBidirectional(skin.genderTypeProperty)
    gridPane.add(genderTypeComboBox, 1, 3)

    gridPane.add(Label("Type"), 0, 4)

    val nounTypeComboBox =
      ArabicSupportEnumComboBox(
        NounType.values,
        ListType.LABEL_AND_CODE
      )
    nounTypeComboBox.valueProperty().bindBidirectional(skin.nounTypeProperty)
    gridPane.add(nounTypeComboBox, 1, 4)

    gridPane.add(Label("Kind"), 0, 5)
    val nounKindComboBox =
      ArabicSupportEnumComboBox(
        NounKind.values,
        ListType.LABEL_AND_CODE
      )
    nounKindComboBox.valueProperty().bindBidirectional(skin.nounKindProperty)
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
