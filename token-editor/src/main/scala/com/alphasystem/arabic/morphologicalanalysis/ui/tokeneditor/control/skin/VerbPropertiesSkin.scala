package com.alphasystem
package arabic
package morphologicalanalysis
package ui
package tokeneditor
package control
package skin

import morphologicalanalysis.morphology.model.*
import ui.tokeneditor.*
import ui.{ ArabicSupportEnumComboBox, ListType }
import javafx.scene.control.SkinBase
import scalafx.Includes.*
import scalafx.collections.ObservableBuffer
import scalafx.geometry.{ Insets, Pos }
import scalafx.scene.control.Label
import scalafx.scene.layout.{ BorderPane, GridPane }

class VerbPropertiesSkin(control: VerbPropertiesView) extends SkinBase[VerbPropertiesView](control) {

  getChildren.add(initializeSkin)

  private def initializeSkin = {

    val gridPane = new GridPane()
    gridPane.styleClass = ObservableBuffer("border")
    gridPane.vgap = DefaultGap
    gridPane.hgap = DefaultGap
    gridPane.padding = Insets(DefaultGap, DefaultGap, DefaultGap, DefaultGap)

    addTypeComboBox(gridPane)
    addNumberComboBox(gridPane)
    addGenderComboBox(gridPane)
    addConversationTypeComboBox(gridPane)
    addVerbModeComboBox(gridPane)

    val borderPane = new BorderPane() {
      center = gridPane
    }
    BorderPane.setAlignment(gridPane, Pos.Center)
    borderPane
  }

  private def addTypeComboBox(gridPane: GridPane): Unit = {
    gridPane.add(Label("Type:"), 0, 0)
    val comboBox =
      ArabicSupportEnumComboBox(
        VerbType.values,
        ListType.LABEL_AND_CODE
      )
    comboBox.valueProperty().bindBidirectional(control.verbTypeProperty)
    gridPane.add(comboBox, 1, 0)
  }

  private def addNumberComboBox(gridPane: GridPane): Unit = {
    gridPane.add(Label("Number:"), 0, 1)
    val comboBox =
      ArabicSupportEnumComboBox(
        NumberType.values,
        ListType.LABEL_AND_CODE
      )
    comboBox.valueProperty().bindBidirectional(control.numberTypeProperty)
    gridPane.add(comboBox, 1, 1)
  }

  private def addGenderComboBox(gridPane: GridPane): Unit = {
    gridPane.add(Label("Gender:"), 0, 2)
    val comboBox =
      ArabicSupportEnumComboBox(
        GenderType.values,
        ListType.LABEL_AND_CODE
      )
    comboBox.valueProperty().bindBidirectional(control.genderTypeProperty)
    gridPane.add(comboBox, 1, 2)
  }

  private def addConversationTypeComboBox(gridPane: GridPane): Unit = {
    gridPane.add(Label("Conversation Type:"), 0, 3)
    val comboBox =
      ArabicSupportEnumComboBox(
        ConversationType.values,
        ListType.LABEL_AND_CODE
      )
    comboBox.valueProperty().bindBidirectional(control.conversationTypeProperty)
    gridPane.add(comboBox, 1, 3)
  }

  private def addVerbModeComboBox(gridPane: GridPane): Unit = {
    gridPane.add(Label("Mode:"), 0, 4)
    val comboBox =
      ArabicSupportEnumComboBox(
        VerbMode.values,
        ListType.LABEL_AND_CODE
      )
    comboBox.valueProperty().bindBidirectional(control.verbModeProperty)
    gridPane.add(comboBox, 1, 4)
  }
}

object VerbPropertiesSkin {

  def apply(control: VerbPropertiesView): VerbPropertiesSkin = new VerbPropertiesSkin(control)
}
