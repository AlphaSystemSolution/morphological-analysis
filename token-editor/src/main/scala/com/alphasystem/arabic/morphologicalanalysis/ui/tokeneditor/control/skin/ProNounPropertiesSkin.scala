package com.alphasystem
package arabic
package morphologicalanalysis
package ui
package tokeneditor
package control
package skin

import tokeneditor.*
import control.ProNounPropertiesView
import com.alphasystem.morphologicalanalysis.morphology.model.{
  ProNounPartOfSpeechType,
  NounStatus,
  NumberType,
  GenderType,
  ConversationType,
  ProNounType
}
import com.alphasystem.morphologicalanalysis.ui.{ ArabicSupportEnumComboBox, ListType }
import javafx.scene.control.SkinBase
import scalafx.Includes.*
import scalafx.collections.ObservableBuffer
import scalafx.geometry.Insets
import scalafx.scene.control.Label
import scalafx.scene.layout.{ BorderPane, GridPane }

class ProNounPropertiesSkin(control: ProNounPropertiesView) extends SkinBase[ProNounPropertiesView](control) {

  getChildren.add(initializeSkin)

  import ProNounPropertiesSkin.*

  private def initializeSkin = {
    val borderPane = new BorderPane()

    val gridPane = new GridPane()
    gridPane.styleClass = ObservableBuffer("border")
    gridPane.vgap = Gap
    gridPane.hgap = Gap
    gridPane.padding = Insets(Gap, Gap, Gap, Gap)

    val skin = getSkinnable

    gridPane.add(Label("Part of Speech"), 0, 0)
    val partOfSpeechTypeComboBox =
      ArabicSupportEnumComboBox(
        ProNounPartOfSpeechType.values,
        ListType.LABEL_AND_CODE
      )
    partOfSpeechTypeComboBox
      .valueProperty()
      .bindBidirectional(skin.partOfSpeechTypeProperty)
    gridPane.add(partOfSpeechTypeComboBox, 1, 0)

    gridPane.add(Label("Status"), 0, 1)
    val statusComboBox =
      ArabicSupportEnumComboBox(
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
    val genderTypeComboBox =
      ArabicSupportEnumComboBox(
        GenderType.values,
        ListType.LABEL_AND_CODE
      )
    genderTypeComboBox
      .valueProperty()
      .bindBidirectional(skin.genderTypeProperty)
    gridPane.add(genderTypeComboBox, 1, 3)

    gridPane.add(Label("Conversation Type"), 0, 4)

    val converstationTypeComboBox =
      ArabicSupportEnumComboBox(
        ConversationType.values,
        ListType.LABEL_AND_CODE
      )
    converstationTypeComboBox
      .valueProperty()
      .bindBidirectional(skin.conversationTypeProperty)
    gridPane.add(converstationTypeComboBox, 1, 4)

    gridPane.add(Label("ProNoun Type"), 0, 5)
    val proNounTypeComboBox =
      ArabicSupportEnumComboBox(
        ProNounType.values,
        ListType.LABEL_AND_CODE
      )
    proNounTypeComboBox
      .valueProperty()
      .bindBidirectional(skin.proNounTypeProperty)
    gridPane.add(proNounTypeComboBox, 1, 5)

    borderPane.center = gridPane

    borderPane
  }
}

object ProNounPropertiesSkin {

  private val Gap = 10.0

  def apply(control: ProNounPropertiesView): ProNounPropertiesSkin =
    new ProNounPropertiesSkin(control)
}
