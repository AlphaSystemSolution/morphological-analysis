package com.alphasystem
package arabic
package morphologicalanalysis
package ui
package tokeneditor
package control
package skin

import tokeneditor.*
import control.ParticlePropertiesView
import morphology.model.ParticlePartOfSpeechType
import ui.{ ArabicSupportEnumComboBox, ListType }
import javafx.scene.control.SkinBase
import scalafx.Includes.*
import scalafx.collections.ObservableBuffer
import scalafx.geometry.Insets
import scalafx.scene.control.Label
import scalafx.scene.layout.{ BorderPane, GridPane }

class ParticlePropertiesSkin(control: ParticlePropertiesView) extends SkinBase[ParticlePropertiesView](control) {

  getChildren.add(initializeSkin)

  import ParticlePropertiesSkin.*

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
      ParticlePartOfSpeechType.values,
      ListType.LABEL_AND_CODE
    )
    partOfSpeechTypeComboBox
      .valueProperty()
      .bindBidirectional(skin.partOfSpeechTypeProperty)
    gridPane.add(partOfSpeechTypeComboBox, 1, 0)

    borderPane.center = gridPane

    borderPane
  }
}

object ParticlePropertiesSkin {

  private val Gap = 10.0

  def apply(control: ParticlePropertiesView): ParticlePropertiesSkin =
    new ParticlePropertiesSkin(control)
}
