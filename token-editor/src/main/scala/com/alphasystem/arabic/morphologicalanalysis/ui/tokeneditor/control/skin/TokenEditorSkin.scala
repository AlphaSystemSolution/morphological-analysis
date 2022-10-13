package com.alphasystem.arabic.morphologicalanalysis.ui.tokeneditor.control.skin

import com.alphasystem.arabic.model.ArabicWord
import com.alphasystem.arabic.morphologicalanalysis.morphology.model.Location
import com.alphasystem.arabic.morphologicalanalysis.ui.tokeneditor.*
import com.alphasystem.arabic.morphologicalanalysis.ui.tokeneditor.control.TokenEditorView
import com.alphasystem.arabic.morphologicalanalysis.ui.tokeneditor.control.skin.TokenEditorSkin.Gap
import com.alphasystem.morphologicalanalysis.ui.{
  ArabicLabelToggleGroup,
  ArabicLabelView,
  ListType
}
import javafx.util.Callback
import javafx.beans.binding.Bindings
import javafx.scene.control.{ ComboBox, ListCell, ListView, SkinBase }
import scalafx.Includes.*
import scalafx.collections.ObservableBuffer
import scalafx.event.subscriptions.Subscription
import scalafx.geometry.{ Insets, NodeOrientation, Pos }
import scalafx.scene.control.Alert.AlertType
import scalafx.scene.control.{ Alert, Label, TextArea }
import scalafx.scene.layout.{ BorderPane, GridPane, HBox, Priority }
import scalafx.scene.text.{ Font, FontPosture, FontWeight, TextAlignment }

import scala.collection.mutable.ListBuffer

class TokenEditorSkin(control: TokenEditorView)
    extends SkinBase[TokenEditorView](control) {

  private val lettersToggleGroup = ArabicLabelToggleGroup("TokenEditor")

  private val lettersBox = new HBox() {
    spacing = Gap
    alignment = Pos.Center
    nodeOrientation = NodeOrientation.RightToLeft
    padding = Insets(Gap, Gap, Gap, Gap)
  }

  private val subscriptions = ListBuffer.empty[Subscription]

  getChildren.add(initializeSkin)

  import TokenEditorSkin.*

  private def initializeSkin = {
    val gridPane = new GridPane() {
      hgap = Gap
      vgap = Gap
      padding = Insets(Gap, Gap, Gap, Gap)
      alignment = Pos.Center
      styleClass = ObservableBuffer[String]("border")
    }

    val locationLabel = Label("Location:")
    gridPane.add(locationLabel, 0, 0)

    val locationsComboBox = createLocationComboBox
    locationLabel.labelFor = locationsComboBox
    gridPane.add(locationsComboBox, 1, 0)

    val translationLabel = Label("Translation:")
    gridPane.add(translationLabel, 0, 1)

    val translationArea = createTranslationArea
    translationLabel.labelFor = translationArea
    gridPane.add(translationArea, 1, 1)

    val lettersPane = new BorderPane() {
      styleClass = ObservableBuffer[String]("border")
      center = lettersBox
    }
    initializeLettersPane(null)
    control
      .selectedLocationProperty
      .onChange((_, ov, nv) => initializeLettersPane(nv))
    gridPane.add(lettersPane, 0, 2, 2, 2)

    val borderPane = new BorderPane() {
      styleClass = ObservableBuffer[String]("border")
      center = gridPane
    }
    BorderPane.setAlignment(gridPane, Pos.Center)
    borderPane
  }

  private def createLocationComboBox = {
    val locationsComboBox = new ComboBox[Location]()
    locationsComboBox.setButtonCell(new LocationListCell())
    locationsComboBox.setCellFactory((_: ListView[Location]) =>
      new LocationListCell()
    )
    locationsComboBox.setDisable(true)
    control
      .selectedLocationProperty
      .bindBidirectional(locationsComboBox.valueProperty())
    control
      .locationsProperty
      .onChange((_, changes) => {
        changes.foreach {
          case ObservableBuffer.Add(_, added) =>
            locationsComboBox.getItems.addAll(added.toSeq*)
            if added.nonEmpty then {
              locationsComboBox.setValue(added.head)
              locationsComboBox.setDisable(false)
            }

          case ObservableBuffer.Remove(_, removed) =>
            locationsComboBox.getItems.removeAll(removed.toSeq*)
            if control.locationsProperty.isEmpty then
              locationsComboBox.setDisable(true)
            else {
              locationsComboBox.setValue(control.locationsProperty.head)
              locationsComboBox.setDisable(false)
            }

          case ObservableBuffer.Reorder(_, _, _) => ()
          case ObservableBuffer.Update(_, _)     => ()
        }
      })
    locationsComboBox
  }

  private def createTranslationArea = {
    val textArea = new TextArea(control.translationText) {
      prefRowCount = 2
      prefColumnCount = 2
    }
    textArea.setFont(BaseFont)
    control.translationTextProperty.bindBidirectional(textArea.textProperty())
    textArea
  }

  private def initializeLettersPane(location: Location): Unit = {
    // remove all subscriptions before removing labels to prevent "Invalid Operation" warning alert to appear
    subscriptions.foreach(_.cancel())
    subscriptions.clear()
    lettersToggleGroup.clearToggles()
    lettersBox.getChildren.clear()

    val labels =
      if Option(location).isEmpty then {
        // location is empty, display empty values
        // just draw 5 boxes
        (0 until 5).map { index =>
          val labelView = ArabicLabelView(null)
          labelView.setId(index.toString)
          labelView.group = lettersToggleGroup
          labelView.disable = true
          labelView
        }
      } else {
        val word = ArabicWord(location.alternateText)
        val startIndex = location.startIndex
        val endIndex = location.endIndex
        word.letters.zipWithIndex.map { case (letter, index) =>
          val labelView = ArabicLabelView(letter)
          labelView.setId(index.toString)
          labelView.group = lettersToggleGroup
          val selected = index >= startIndex && index < endIndex
          labelView.select = selected
          labelView.disable = !selected

          locationSelectionHandler(location, labelView)
          labelView
        }
      }
    lettersBox.getChildren.addAll(labels*)
  }

  private def locationSelectionHandler(
    location: Location,
    labelView: ArabicLabelView
  ): Unit = {
    val subscription =
      labelView
        .selectedProperty
        .onChange((_, ov, nv) =>
          if !nv then {
            val index = labelView.getId.toInt
            if location.startIndex == index then {
              val label = new Label(
                s"""This operation will leave current location without any element, 
                   |a location must have at least one element in it."""
                  .stripMargin
                  .replaceAll(System.lineSeparator(), "")
              ) {
                wrapText = true
                maxWidth = 5
                maxHeight = 5
                vgrow = Priority.Always
                hgrow = Priority.Always
                alignment = Pos.Center
                textAlignment = TextAlignment.Center
                font = BaseFont
              }

              new Alert(AlertType.Warning) {
                title = "Invalid Operation"
                headerText = "Operation not allowed"
                dialogPane().content = label
              }.showAndWait()

              labelView.select = true
            } else {}
          }
        )
    subscriptions.addOne(subscription)
  }
}

object TokenEditorSkin {

  private val Gap = 10.0

  private val BaseFont = Font(
    tokenEditorPreferences.englishFontName,
    FontWeight.Normal,
    FontPosture.Regular,
    14.0
  )

  def apply(control: TokenEditorView): TokenEditorSkin =
    new TokenEditorSkin(control)
}
