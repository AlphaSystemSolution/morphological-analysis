package com.alphasystem
package arabic
package morphologicalanalysis
package ui
package tokeneditor
package control
package skin

import morphologicalanalysis.morphology.model.*
import morphologicalanalysis.morphology.model.incomplete_verb.*
import ui.tokeneditor.*
import ui.{ ArabicSupportEnumComboBox, ListType }
import javafx.scene.control.SkinBase
import scalafx.Includes.*
import scalafx.collections.ObservableBuffer
import scalafx.geometry.{ Insets, NodeOrientation, Pos }
import scalafx.scene.control.{ Label, TextField }
import scalafx.scene.layout.{ BorderPane, GridPane }

class VerbPropertiesSkin(control: VerbPropertiesView) extends SkinBase[VerbPropertiesView](control) {

  private val incompleteVerbCategoryComboBox =
    ArabicSupportEnumComboBox(IncompleteVerbCategory.values, ListType.LABEL_ONLY)

  private val incompleteVerbTypeTextField = new TextField() {
    text = control.incompleteVerbType.map(_.label).getOrElse("")
    editable = false
    nodeOrientation = NodeOrientation.RightToLeft
    font = tokenEditorPreferences.arabicFont
  }

  control
    .verbTypeProperty
    .onChange((_, _, nv) => {
      if isValidSelection(nv) then {
        incompleteVerbCategoryComboBox.disable = false
        updateFields(
          incompleteVerbCategoryComboBox.getValue,
          nv,
          control.numberType,
          control.genderType,
          control.conversationType
        )
      } else {
        incompleteVerbCategoryComboBox.getSelectionModel.select(0)
        incompleteVerbCategoryComboBox.disable = true
      }
    })

  control
    .numberTypeProperty
    .onChange((_, _, nv) =>
      updateFields(
        incompleteVerbCategoryComboBox.getValue,
        control.verbType,
        nv,
        control.genderType,
        control.conversationType
      )
    )

  control
    .genderTypeProperty
    .onChange((_, _, nv) =>
      updateFields(
        incompleteVerbCategoryComboBox.getValue,
        control.verbType,
        control.numberType,
        nv,
        control.conversationType
      )
    )

  control
    .conversationTypeProperty
    .onChange((_, _, nv) =>
      updateFields(
        incompleteVerbCategoryComboBox.getValue,
        control.verbType,
        control.numberType,
        control.genderType,
        nv
      )
    )

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
    addIncompleteVerbCategoryComboBox(gridPane)
    addIncompleteVerbTypesComboBox(gridPane)

    val borderPane = new BorderPane() {
      center = gridPane
    }
    BorderPane.setAlignment(gridPane, Pos.Center)
    borderPane
  }

  private def addTypeComboBox(gridPane: GridPane): Unit = {
    gridPane.add(Label("Type:"), 0, 0)
    val comboBox = ArabicSupportEnumComboBox(VerbType.values, ListType.LABEL_AND_CODE)
    comboBox.valueProperty().bindBidirectional(control.verbTypeProperty)
    gridPane.add(comboBox, 1, 0)
  }

  private def addNumberComboBox(gridPane: GridPane): Unit = {
    gridPane.add(Label("Number:"), 0, 1)
    val comboBox = ArabicSupportEnumComboBox(NumberType.values, ListType.LABEL_AND_CODE)
    comboBox.valueProperty().bindBidirectional(control.numberTypeProperty)
    gridPane.add(comboBox, 1, 1)
  }

  private def addGenderComboBox(gridPane: GridPane): Unit = {
    gridPane.add(Label("Gender:"), 0, 2)
    val comboBox = ArabicSupportEnumComboBox(GenderType.values, ListType.LABEL_AND_CODE)
    comboBox.valueProperty().bindBidirectional(control.genderTypeProperty)
    gridPane.add(comboBox, 1, 2)
  }

  private def addConversationTypeComboBox(gridPane: GridPane): Unit = {
    gridPane.add(Label("Conversation Type:"), 0, 3)
    val comboBox = ArabicSupportEnumComboBox(ConversationType.values, ListType.LABEL_AND_CODE)
    comboBox.valueProperty().bindBidirectional(control.conversationTypeProperty)
    gridPane.add(comboBox, 1, 3)
  }

  private def addVerbModeComboBox(gridPane: GridPane): Unit = {
    gridPane.add(Label("Mode:"), 0, 4)
    val comboBox = ArabicSupportEnumComboBox(VerbMode.values, ListType.LABEL_AND_CODE)
    comboBox.valueProperty().bindBidirectional(control.verbModeProperty)
    gridPane.add(comboBox, 1, 4)
  }

  private def addIncompleteVerbCategoryComboBox(gridPane: GridPane): Unit = {
    gridPane.add(Label("Incomplete Verb Category:"), 0, 5)
    gridPane.add(incompleteVerbCategoryComboBox, 1, 5)
  }

  private def addIncompleteVerbTypesComboBox(gridPane: GridPane): Unit = {
    gridPane.add(Label("Incomplete Verb Type:"), 0, 6)

    control
      .incompleteVerbTypeProperty
      .onChange((_, _, nv) => {
        nv match
          case Some(value) => incompleteVerbTypeTextField.text = value.label
          case None =>
            incompleteVerbTypeTextField.text = ""
            incompleteVerbCategoryComboBox.getSelectionModel.select(0)
      })
    incompleteVerbCategoryComboBox
      .valueProperty()
      .onChange((_, _, nv) =>
        updateFields(nv, control.verbType, control.numberType, control.genderType, control.conversationType)
      )
    gridPane.add(incompleteVerbTypeTextField, 1, 6)
  }

  private def updateFields(
    category: IncompleteVerbCategory,
    verbType: VerbType,
    numberType: NumberType,
    genderType: GenderType,
    conversationType: ConversationType
  ): Unit = {
    val maybeValue = getValue(category, verbType, numberType, genderType, conversationType)
    maybeValue match
      case Some(value) => incompleteVerbTypeTextField.text = value.label
      case None =>
        incompleteVerbTypeTextField.text = ""
        incompleteVerbCategoryComboBox.getSelectionModel.select(0)

    control.incompleteVerbType = maybeValue
  }

  private def getValue(
    category: IncompleteVerbCategory,
    verbType: VerbType,
    numberType: NumberType,
    genderType: GenderType,
    conversationType: ConversationType
  ) = {
    (category, verbType) match
      case (IncompleteVerbCategory.Kana, VerbType.Perfect) =>
        val value = KanaPastTense.fromProperties(numberType, genderType, conversationType)
        Some(value)

      case (IncompleteVerbCategory.Kana, VerbType.Imperfect) =>
        Some(KanaPresentTense.fromProperties(numberType, genderType, conversationType))

      case (IncompleteVerbCategory.Kana, VerbType.Command) =>
        KanaCommand.fromProperties(numberType, genderType, conversationType)

      case (IncompleteVerbCategory.Kana, VerbType.Forbidden) =>
        KanaForbidden.fromProperties(numberType, genderType, conversationType)

      case (IncompleteVerbCategory.IsNot, VerbType.Perfect) =>
        Some(IsNot.fromProperties(numberType, genderType, conversationType))

      case (_, _) => None
  }

  private def isValidSelection(verbType: VerbType): Boolean =
    Seq(VerbType.Perfect, VerbType.Imperfect, VerbType.Command, VerbType.Forbidden).contains(verbType)

}

object VerbPropertiesSkin {

  def apply(control: VerbPropertiesView): VerbPropertiesSkin = new VerbPropertiesSkin(control)
}
