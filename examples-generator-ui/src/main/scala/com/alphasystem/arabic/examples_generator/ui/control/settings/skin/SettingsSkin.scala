package com.alphasystem
package arabic
package examples_generator
package ui
package control
package settings
package skin

import arabic.fx.ui.util.*
import arabic.morphologicalengine.conjugation.model.MorphologicalTermType
import javafx.scene.control.SkinBase
import org.controlsfx.control.ToggleSwitch
import scalafx.Includes.*
import scalafx.beans.property.BooleanProperty
import scalafx.geometry.Pos
import scalafx.scene.layout.{ BorderPane, ColumnConstraints, GridPane }

class SettingsSkin private (control: SettingsView) extends SkinBase[SettingsView](control) {

  private val gridPane = new GridPane {
    hgap = DefaultGap
    vgap = DefaultGap
    alignment = Pos.TopLeft
    padding = DefaultInsets
    columnConstraints = Seq(
      new ColumnConstraints {
        minWidth <== control.labelColumnWidthProperty
        prefWidth <== control.labelColumnWidthProperty
        maxWidth <== control.labelColumnWidthProperty
      },
      new ColumnConstraints()
    )
  }

  private var rowIndex = 0

  addOptionalBooleanRow("Show Pronouns:", control.showPronounsProperty, verbType)
  addOptionalBooleanRow("Show Numbers:", control.showNumbersProperty, () => true)
  addOptionalBooleanRow("Show Genders:", control.showGendersProperty, verbType)
  addOptionalBooleanRow("Show Conversation Types:", control.showConversationTypesProperty, verbType)
  addOptionalBooleanRow("Show Noun Status:", control.showNounStatusProperty, nounType)
  addOptionalBooleanRow("Show Term Type Caption:", control.showTermTypeCaptionProperty, () => true)
  addTableWidthRow()

  private val mainPane = new BorderPane {
    center = gridPane
    BorderPane.setAlignment(gridPane, Pos.Center)
  }

  getChildren.add(mainPane)

  private def verbType(): Boolean =
    MorphologicalTermType.VerbBasedTypes.contains(control.morphologicalTermType)

  private def nounType(): Boolean =
    MorphologicalTermType.NounBasedTypes.contains(control.morphologicalTermType)

  private def addOptionalBooleanRow(
    label: String,
    property: BooleanProperty,
    enabledBy: () => Boolean
  ): Unit = {
    val toggleField = new ToggleSwitch()
    toggleField.selectedProperty().bindBidirectional(property)
    toggleField.setSelected(property.value)
    toggleField.disable = !enabledBy()

    control
      .morphologicalTermTypeProperty
      .onChange((_, _, _) => {
        val enabled = enabledBy()
        toggleField.setSelected(!enabled)
        toggleField.disable = !enabled
      })

    gridPane.add(createLabel(label), 0, rowIndex)
    gridPane.add(toggleField, 1, rowIndex)
    rowIndex += 1
  }

  private def addTableWidthRow(): Unit = {
    val widthField = createPositiveIntegerTextField
    widthField.text = control.tableWidth.map(_.toString).getOrElse("")
    widthField
      .textProperty()
      .onChange((_, _, nv) =>
        if Option(nv).exists(_.nonEmpty) then {
          val newValue = nv.toInt
          control.tableWidth = if newValue < 100 then Some(newValue) else None
        }
      )

    control
      .tableWidthProperty
      .onChange((_, _, nv) => {
        widthField.text = nv.map(_.toString).getOrElse("")
      })

    gridPane.add(createLabel("Table Width:"), 0, rowIndex)
    gridPane.add(widthField, 1, rowIndex)
    rowIndex += 1
  }
}

object SettingsSkin {
  private[settings] def apply(control: SettingsView): SettingsSkin = new SettingsSkin(control)
}
