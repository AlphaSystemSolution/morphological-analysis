package com.alphasystem
package arabic
package morphologicalanalysis
package ui
package dependencygraph
package control
package skin

import morphologicalanalysis.morphology.graph.model.PhraseInfo
import morphologicalanalysis.morphology.model.{ Linkable, Location, RelationshipType }
import javafx.scene.control.SkinBase
import scalafx.Includes.*
import scalafx.beans.property.ObjectProperty
import scalafx.geometry.{ Insets, NodeOrientation, Pos }
import scalafx.scene.control.{ Label, TextField }
import scalafx.scene.layout.{ BorderPane, GridPane }
import scalafx.scene.text.{ Text, TextFlow }

class CreateRelationshipSkin(control: CreateRelationshipView) extends SkinBase[CreateRelationshipView](control) {

  private val relationshipTypeComboBox = ArabicSupportEnumComboBox(RelationshipType.values, ListType.LABEL_ONLY)
  relationshipTypeComboBox
    .valueProperty()
    .onChange((_, _, nv) => {
      control.relationshipType = nv
      control.invalidSelection = nv == RelationshipType.None
    })

  control.relationshipTypeProperty.onChange((_, _, nv) => relationshipTypeComboBox.getSelectionModel.select(nv))

  getChildren.addAll(initializeSkin)

  private def initializeSkin = {
    val gridPane = new GridPane() {
      vgap = DefaultGap
      hgap = DefaultGap
      alignment = Pos.Center
      padding = Insets(DefaultGap, DefaultGap, DefaultGap, DefaultGap)
    }

    gridPane.add(Label("Owner:"), 0, 0)
    gridPane.add(getArabicTextLabel(control.ownerProperty), 1, 0)

    gridPane.add(Label("Dependent:"), 0, 1)
    gridPane.add(getArabicTextLabel(control.dependentProperty), 1, 1)

    gridPane.add(Label("Relationship Type:"), 0, 2)
    gridPane.add(relationshipTypeComboBox, 1, 2)

    gridPane.add(Label("Text:"), 0, 3)
    gridPane.add(initializeText, 1, 3)

    val pane = new BorderPane()
    pane.center = gridPane
    BorderPane.setAlignment(gridPane, Pos.Center)
    pane
  }

  private def getArabicTextLabel(property: ObjectProperty[Linkable]): TextFlow = {
    val locationText = new Text("") {
      font = dependencyGraphPreferences.arabicFont
    }
    val posText = new Text("") {
      font = dependencyGraphPreferences.arabicFont
    }
    val openBracket = new Text(" (") {
      font = dependencyGraphPreferences.englishFont
    }
    val closeBracket = new Text(") ") {
      font = dependencyGraphPreferences.englishFont
    }
    bindProperty(property, locationText, posText)
    new TextFlow() {
      children = Seq(openBracket, posText, closeBracket, locationText)
      prefWidth = 100
    }
  }

  private def bindProperty(property: ObjectProperty[Linkable], locationText: Text, posText: Text): Unit = {
    property.onChange((_, _, nv) => {
      if Option(nv).isDefined then {
        nv match
          case l: Location =>
            locationText.text = l.text
            posText.text = l.properties.toText

          case l: PhraseInfo =>
            locationText.text = l.text
            posText.text = ""
      } else {
        locationText.text = ""
        posText.text = ""
      }
    })
  }

  private def initializeText = {
    val textField = new TextField() {
      text = deriveRelationshipInfoText(control.relationshipType, control.owner)
      font = defaultArabicFont.toFont
      nodeOrientation = NodeOrientation.RightToLeft
    }

    control
      .relationshipTypeProperty
      .onChange((_, _, nv) => {
        val text =
          if RelationshipType.None == nv then ""
          else deriveRelationshipInfoText(nv, control.owner)
        textField.text = text
      })

    control.textProperty.bind(textField.textProperty())
    textField
  }
}

object CreateRelationshipSkin {
  def apply(control: CreateRelationshipView) = new CreateRelationshipSkin(control)
}
