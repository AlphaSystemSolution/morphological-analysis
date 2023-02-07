package com.alphasystem
package arabic
package morphologicalanalysis
package ui
package dependencygraph
package control
package skin

import arabic.model.{ ArabicLabel, ProNoun }
import morphologicalanalysis.graph.model.GraphNodeType.*
import morphologicalanalysis.morphology.model.WordType.*
import morphologicalanalysis.graph.model.GraphNodeType
import morphologicalanalysis.morphology.model.{
  NounProperties,
  NounStatus,
  ProNounProperties,
  VerbProperties,
  MorphologyVerbType,
  WordType,
  defaultPronounToken,
  defaultToken,
  defaultVerbToken
}
import morphologicalanalysis.morphology.utils.DerivedTerminalNodeTypes
import javafx.application.Platform
import javafx.scene.control.SkinBase
import scalafx.Includes.*
import scalafx.collections.ObservableBuffer
import scalafx.geometry.{ Insets, NodeOrientation, Pos }
import scalafx.scene.control.{ ComboBox, Label, TextField }
import scalafx.scene.layout.{ BorderPane, GridPane }

class AddNodeSkin(control: AddNodeView) extends SkinBase[AddNodeView](control) {

  import AddNodeView.*

  private val gridPane = new GridPane() {
    vgap = DefaultGap
    hgap = DefaultGap
    alignment = Pos.Center
    padding = Insets(DefaultGap, DefaultGap, DefaultGap, DefaultGap)
  }

  private val wordTypes = WordType.values.filterNot(_ == WordType.PARTICLE)
  private val verbTypes = Array(MorphologyVerbType.Imperfect, MorphologyVerbType.Perfect)
  control.showReferenceTypeProperty.onChange((_, _, _) => reInitializeGridPane())

  getChildren.add(initializeSkin)
  private def initializeSkin = {
    initializeGridPane()

    val pane = new BorderPane()
    pane.center = gridPane
    BorderPane.setAlignment(gridPane, Pos.Center)
    pane
  }

  private def initializeGridPane(): Unit = {
    gridPane.getChildren.clear()

    val selectedType = control.selectedType
    val showReferenceType = control.showReferenceType
    initializeTypeComboBox(selectedType, showReferenceType)

    if selectedType == GraphNodeType.Reference then {
      initializeVerseComboBox()
      initializeTokensList()
    } else {
      initializeWordTypeComboBox()

      control.wordType match
        case NOUN =>
          initializeNounStatusComboBox()
          initializeTextField()

        case PRO_NOUN => initializeProNounComboBox()

        case VERB =>
          initializeVerbTypeComboBox()
          initializeTextField()

        case PARTICLE => ()
    }
  }

  private def initializeTypeComboBox(
    selectedType: GraphNodeType,
    showReferenceType: Boolean
  ): Unit = {
    val comboBox = ArabicSupportEnumComboBox(
      toGraphNodeTypesLabels(showReferenceType),
      ListType.CODE_ONLY
    )
    comboBox.getSelectionModel.select(selectedType.toArabicLabel)

    comboBox
      .valueProperty()
      .onChange((_, _, nv) => {
        val selectedType = nv.userData
        control.selectedType = selectedType
        control.selectedToken = defaultToken.toArabicLabel

        reInitializeGridPane()
      })

    gridPane.add(Label("Node Type:"), 0, 0)
    gridPane.add(comboBox, 1, 0)
  }

  private def initializeWordTypeComboBox(): Unit = {
    gridPane.add(Label("Word Type:"), 0, 1)

    val comboBox = ArabicSupportEnumComboBox(wordTypes, ListType.LABEL_AND_CODE)
    comboBox.valueProperty().bindBidirectional(control.wordTypeProperty)

    control
      .wordTypeProperty
      .onChange((_, _, nv) => {
        nv match
          case NOUN     => control.selectedToken = defaultToken.toArabicLabel
          case PRO_NOUN => control.selectedToken = defaultPronounToken.toArabicLabel
          case VERB     => control.selectedToken = defaultVerbToken.toArabicLabel
          case PARTICLE =>

        reInitializeGridPane()
      })

    gridPane.add(comboBox, 1, 1)
  }

  private def initializeNounStatusComboBox(): Unit = {
    gridPane.add(Label("Status:"), 0, 2)
    val comboBox = ArabicSupportEnumComboBox(NounStatus.values, ListType.LABEL_ONLY)

    comboBox
      .valueProperty()
      .onChange((_, _, nv) => {
        val token = control.selectedToken.userData
        val location = token.locations.head
        if location.wordType == WordType.NOUN then {
          val properties = location.properties.asInstanceOf[NounProperties].copy(status = nv)
          val updatedToken = token.copy(locations = Seq(location.copy(properties = properties)))
          control.selectedToken = updatedToken.toArabicLabel
        }
      })

    control
      .selectedTokenProperty
      .onChange((_, _, nv) => {
        if Option(nv).isDefined then {
          val location = nv.userData.locations.head
          if location.wordType == WordType.NOUN then
            comboBox.getSelectionModel.select(location.properties.asInstanceOf[NounProperties].status)
        }
      })

    gridPane.add(comboBox, 1, 2)
  }

  private def initializeProNounComboBox(): Unit = {
    gridPane.add(Label("Type:"), 0, 2)
    val comboBox = ArabicSupportEnumComboBox[ProNoun](ProNoun.values, ListType.LABEL_ONLY)

    comboBox
      .valueProperty()
      .onChange((_, _, nv) => {
        val token = control.selectedToken.userData
        val location = token.locations.head
        if location.wordType == WordType.PRO_NOUN then {
          val properties =
            location
              .properties
              .asInstanceOf[ProNounProperties]
              .copy(number = nv.number, gender = nv.gender, conversationType = nv.conversation)

          val label = nv.label
          val updatedLocation = location
            .copy(endIndex = label.length - 1, derivedText = label, text = label, properties = properties)
          val updatedToken = token.copy(token = label, locations = Seq(updatedLocation))
          control.selectedToken = updatedToken.toArabicLabel
        }
      })

    control
      .selectedTokenProperty
      .onChange((_, _, nv) => {
        if Option(nv).isDefined then {
          val location = nv.userData.locations.head
          if location.wordType == WordType.PRO_NOUN then {
            val properties = location.properties.asInstanceOf[ProNounProperties]
            val proNoun = ProNoun.fromProperties(properties.number, properties.gender, properties.conversationType)
            comboBox.getSelectionModel.select(proNoun)
          }
        }
      })

    gridPane.add(comboBox, 1, 2)
  }

  private def initializeVerbTypeComboBox(): Unit = {
    gridPane.add(Label("Type:"), 0, 2)
    val comboBox = ArabicSupportEnumComboBox[MorphologyVerbType](verbTypes, ListType.LABEL_ONLY)

    comboBox
      .valueProperty()
      .onChange((_, _, nv) => {
        val token = control.selectedToken.userData
        val location = token.locations.head
        if location.wordType == WordType.VERB then {
          val properties =
            location
              .properties
              .asInstanceOf[VerbProperties]
              .copy(verbType = nv)

          val updatedLocation = location.copy(properties = properties)
          val updatedToken = token.copy(locations = Seq(updatedLocation))
          control.selectedToken = updatedToken.toArabicLabel
        }
      })

    control
      .selectedTokenProperty
      .onChange((_, _, nv) => {
        if Option(nv).isDefined then {
          val location = nv.userData.locations.head
          if location.wordType == WordType.VERB then {
            val properties = location.properties.asInstanceOf[VerbProperties]
            comboBox.getSelectionModel.select(properties.verbType)
          }
        }
      })

    gridPane.add(comboBox, 1, 2)
  }

  private def initializeTextField(): Unit = {
    gridPane.add(Label("Text:"), 0, 3)

    val textField = new TextField() {
      text = Option(control.selectedToken).map(_.userData.token).getOrElse("")
      font = defaultArabicFont.toFont
      nodeOrientation = NodeOrientation.RightToLeft
      editable = false // TODO: have ability update text
    }

    gridPane.add(textField, 1, 3)
  }

  private def initializeVerseComboBox(): Unit = {
    gridPane.add(Label("Verse:"), 0, 1)
    val comboBox = ArabicSupportEnumComboBox(control.versesProperty.toArray, ListType.CODE_ONLY)
    control
      .selectedVerseProperty
      .bindBidirectional(comboBox.valueProperty())
    control.versesProperty.onChange { (_, changes) =>
      changes.foreach {
        case ObservableBuffer.Add(_, added) =>
          comboBox.getItems.addAll(added.toSeq*)
          if added.nonEmpty then comboBox.setValue(added.head)
          if control.versesProperty.nonEmpty then comboBox.setDisable(false)

        case ObservableBuffer.Remove(_, removed) =>
          comboBox.getItems.removeAll(removed.toSeq*)
          comboBox.setValue(null)
          if control.versesProperty.isEmpty then comboBox.setDisable(true)

        case ObservableBuffer.Reorder(_, _, _) => ()
        case ObservableBuffer.Update(_, _)     => ()
      }
    }
    gridPane.add(comboBox, 1, 1)
  }

  private def initializeTokensList(): Unit = {
    gridPane.add(Label("Token:"), 0, 2)
    val comboBox = ArabicSupportEnumComboBox(control.tokensProperty.toArray, ListType.LABEL_ONLY)
    control
      .tokensProperty
      .onChange((_, changes) => {
        changes.foreach {
          case ObservableBuffer.Add(_, added) =>
            comboBox.getItems.addAll(added.toSeq*)
            comboBox.getSelectionModel.select(0)
          case ObservableBuffer.Remove(_, _) => comboBox.getItems.clear()
          case _                             =>
        }
      })

    comboBox.valueProperty().onChange((_, _, nv) => control.selectedToken = nv)
    gridPane.add(comboBox, 1, 2)
  }

  private def toGraphNodeTypesLabels(showReferenceType: Boolean) = {
    val types =
      if showReferenceType then DerivedTerminalNodeTypes
      else DerivedTerminalNodeTypes.filterNot(_ == GraphNodeType.Reference)
    types.map(_.toArabicLabel).toArray
  }

  private def reInitializeGridPane(): Unit = Platform.runLater(() => initializeGridPane())

}

object AddNodeSkin {

  def apply(control: AddNodeView) = new AddNodeSkin(control)
}
