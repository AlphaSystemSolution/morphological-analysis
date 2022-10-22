package com.alphasystem
package arabic
package morphologicalanalysis
package ui
package tokeneditor
package control
package skin

import tokeneditor.*
import control.VerbPropertiesView
import morphology.model.{ VerbPartOfSpeechType, VerbType, NumberType, GenderType, ConversationType, VerbMode }
import ui.{ ArabicSupportEnumComboBox, ListType }
import javafx.scene.control.SkinBase
import scalafx.Includes.*
import scalafx.collections.ObservableBuffer
import scalafx.geometry.Insets
import scalafx.scene.control.Label
import scalafx.scene.layout.{ BorderPane, GridPane }

class VerbPropertiesSkin(control: VerbPropertiesView) extends SkinBase[VerbPropertiesView](control) {

  getChildren.add(initializeSkin)

  import VerbPropertiesSkin.*

  private def initializeSkin = {
    val borderPane = new BorderPane()

    val gridPane = new GridPane()
    gridPane.styleClass = ObservableBuffer("border")
    gridPane.vgap = Gap
    gridPane.hgap = Gap
    gridPane.padding = Insets(Gap, Gap, Gap, Gap)

    val skin = getSkinnable

    gridPane.add(Label("Type:"), 0, 0)
    val verbTypeComboBox =
      ArabicSupportEnumComboBox(
        VerbType.values,
        ListType.LABEL_AND_CODE
      )
    verbTypeComboBox.valueProperty().bindBidirectional(skin.verbTypeProperty)
    gridPane.add(verbTypeComboBox, 1, 0)

    gridPane.add(Label("Number:"), 0, 1)
    val numberTypeComboBox = ArabicSupportEnumComboBox(
      NumberType.values,
      ListType.LABEL_AND_CODE
    )
    numberTypeComboBox
      .valueProperty()
      .bindBidirectional(skin.numberTypeProperty)
    gridPane.add(numberTypeComboBox, 1, 1)

    gridPane.add(Label("Gender:"), 0, 2)
    val genderTypeComboBox =
      ArabicSupportEnumComboBox(
        GenderType.values,
        ListType.LABEL_AND_CODE
      )
    genderTypeComboBox
      .valueProperty()
      .bindBidirectional(skin.genderTypeProperty)
    gridPane.add(genderTypeComboBox, 1, 2)

    gridPane.add(Label("Conversation Type:"), 0, 3)

    val converstationTypeComboBox =
      ArabicSupportEnumComboBox(
        ConversationType.values,
        ListType.LABEL_AND_CODE
      )
    converstationTypeComboBox
      .valueProperty()
      .bindBidirectional(skin.conversationTypeProperty)
    gridPane.add(converstationTypeComboBox, 1, 3)

    gridPane.add(Label("Mode:"), 0, 4)
    val verbModeComboBox =
      ArabicSupportEnumComboBox(
        VerbMode.values,
        ListType.LABEL_AND_CODE
      )
    verbModeComboBox
      .valueProperty()
      .bindBidirectional(skin.verbModeProperty)
    gridPane.add(verbModeComboBox, 1, 4)

    borderPane.center = gridPane

    borderPane
  }
}

object VerbPropertiesSkin {

  private val Gap = 10.0

  def apply(control: VerbPropertiesView): VerbPropertiesSkin =
    new VerbPropertiesSkin(control)
}
