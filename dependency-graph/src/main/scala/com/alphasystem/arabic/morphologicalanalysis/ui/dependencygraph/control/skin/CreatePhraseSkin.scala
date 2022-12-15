package com.alphasystem
package arabic
package morphologicalanalysis
package ui
package dependencygraph
package control
package skin

import arabic.model.{ ArabicLabel, ArabicLetters }
import de.jensd.fx.glyphs.fontawesome.{ FontAwesomeIcon, FontAwesomeIconView }
import javafx.application.Platform
import morphologicalanalysis.morphology.model.{ NounStatus, PhraseType }
import javafx.scene.control.SkinBase
import scalafx.Includes.*
import scalafx.collections.ObservableBuffer
import scalafx.scene.control.{ Button, ContentDisplay, Label, ListView, Tooltip }
import scalafx.geometry.{ Insets, NodeOrientation, Pos }
import scalafx.scene.layout.{ BorderPane, GridPane, HBox, VBox }
import scalafx.scene.text.TextAlignment

import scala.util.Try

class CreatePhraseSkin(control: CreatePhraseView) extends SkinBase[CreatePhraseView](control) {

  import CreatePhraseSkin.*

  private val defaultStatus =
    ArabicLabel[String](userData = "None", code = "None", label = ArabicLetters.WordTatweel.unicode)
  private val statuses = defaultStatus +: NounStatus.values.map(_.toArabicLabel)

  private val phraseTypes = PhraseType.values.toSeq.tail
  private val allPhrasesListView = new ListView[PhraseType](phraseTypes) {
    nodeOrientation = NodeOrientation.RightToLeft
    cellFactory = ArabicSupportEnumCellFactory[PhraseType](ListType.LABEL_ONLY)
  }

  private val selectedPhrasesListView = new ListView[PhraseType](control.selectedPhraseTypesProperty) {
    nodeOrientation = NodeOrientation.RightToLeft
    cellFactory = ArabicSupportEnumCellFactory[PhraseType](ListType.LABEL_ONLY)
  }

  getChildren.addAll(initializeSkin)

  private def initializeSkin = {
    val gridPane = new GridPane() {
      vgap = DefaultGap
      hgap = DefaultGap
      alignment = Pos.Center
      padding = Insets(DefaultGap, DefaultGap, DefaultGap, DefaultGap)
    }

    initializeTextLabel(gridPane)
    gridPane.add(initializePhraseSelectionPane, 0, 1, 2, 1)
    initializeNounStatusPane(gridPane)

    val pane = new BorderPane()
    pane.center = gridPane
    BorderPane.setAlignment(gridPane, Pos.Center)
    pane
  }

  private def initializeTextLabel(gridPane: GridPane): Unit = {
    val label = new Label() {
      text = control.displayText
      textAlignment = TextAlignment.Center
      alignmentInParent = Pos.Center
      nodeOrientation = NodeOrientation.RightToLeft
      font = dependencyGraphPreferences.arabicFont
    }
    gridPane.add(label, 0, 0, 2, 1)
    control.displayTextProperty.onChange((_, _, nv) => label.text = nv)
  }

  private def initializePhraseSelectionPane =
    new HBox() {
      spacing = DefaultGap
      padding = Insets(DefaultGap, DefaultGap, DefaultGap, DefaultGap)
      children = Seq(initializeSelectedPhraseTypes, initializeButtonPane, initializeAllPhraseTypes)
    }

  private def initializeSelectedPhraseTypes = {
    val gridPane = new GridPane() {
      vgap = DefaultGap
      hgap = DefaultGap
      padding = Insets(DefaultGap, DefaultGap, DefaultGap, DefaultGap)
    }

    gridPane.add(Label("Selected Phrase Types:"), 0, 0)
    gridPane.add(selectedPhrasesListView, 0, 1)

    gridPane
  }

  private def initializeButtonPane = {
    val copySelectedPhrase = new Button() {
      graphic = new FontAwesomeIconView(FontAwesomeIcon.ANGLE_LEFT, "2em")
      contentDisplay = ContentDisplay.GraphicOnly
      tooltip = Tooltip("Copy Selected Phrase")
      onAction = event => {
        val selectedItem = allPhrasesListView.selectionModel().selectedItemProperty().value
        if Option(selectedItem).isDefined then control.selectedPhraseTypesProperty.addOne(selectedItem)
        allPhrasesListView.selectionModel().clearSelection()
        event.consume()
      }
    }
    copySelectedPhrase.disableProperty().bind(allPhrasesListView.selectionModel().selectedItemProperty().isNull)

    val removeSelectedPhrase = new Button() {
      graphic = new FontAwesomeIconView(FontAwesomeIcon.TRASH, "2em")
      contentDisplay = ContentDisplay.GraphicOnly
      tooltip = Tooltip("Remove Selected Phrase")
      onAction = event => {
        val selectedItem = selectedPhrasesListView.selectionModel().selectedItemProperty().value
        if Option(selectedItem).isDefined then control.selectedPhraseTypesProperty.remove(selectedItem)
        selectedPhrasesListView.selectionModel().clearSelection()
        event.consume()
      }
    }
    removeSelectedPhrase.disableProperty().bind(selectedPhrasesListView.selectionModel().selectedItemProperty().isNull)

    new VBox() {
      spacing = DefaultGap
      padding = Insets(DefaultGap, DefaultGap, DefaultGap, DefaultGap)
      children = Seq(copySelectedPhrase, removeSelectedPhrase)
      alignment = Pos.Center
      alignmentInParent = Pos.Center
    }
  }

  private def initializeAllPhraseTypes = {
    val gridPane = new GridPane() {
      vgap = DefaultGap
      hgap = DefaultGap
      padding = Insets(DefaultGap, DefaultGap, DefaultGap, DefaultGap)
    }

    gridPane.add(Label("Phrase Types:"), 0, 0)
    gridPane.add(allPhrasesListView, 0, 1)

    gridPane
  }

  private def initializeNounStatusPane(gridPane: GridPane): Unit = {
    gridPane.add(Label("Status:"), 0, 2)
    val statusComboBox = ArabicSupportEnumComboBox(statuses, ListType.LABEL_ONLY)
    control
      .nounStatusProperty
      .onChange((_, _, nv) =>
        val label =
          nv match
            case Some(value) => value.toArabicLabel
            case None =>
              defaultStatus

        statusComboBox.getSelectionModel.select(label)
      )
    statusComboBox
      .valueProperty()
      .onChange((_, _, nv) => control.nounStatus = Try(NounStatus.valueOf(nv.userData)).toOption)
    gridPane.add(statusComboBox, 1, 2)
  }
}

object CreatePhraseSkin {

  extension (src: NounStatus) {
    def toArabicLabel: ArabicLabel[String] =
      ArabicLabel[String](userData = src.name(), code = src.code, label = src.shortLabel.unicode)
  }

  def apply(control: CreatePhraseView): CreatePhraseSkin = new CreatePhraseSkin(control)
}
