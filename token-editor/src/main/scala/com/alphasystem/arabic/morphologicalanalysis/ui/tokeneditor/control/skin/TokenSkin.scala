package com.alphasystem
package arabic
package morphologicalanalysis
package ui
package tokeneditor
package control
package skin

import morphology.model.WordType
import model.ArabicWord
import morphology.model.Location
import tokeneditor.*
import control.TokenView
import com.alphasystem.morphologicalanalysis.ui.{
  ArabicLabelToggleGroup,
  ArabicLabelView,
  ArabicSupportEnumComboBox,
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

class TokenSkin(control: TokenView) extends SkinBase[TokenView](control) {

  import TokenSkin.*

  private val lettersToggleGroup = ArabicLabelToggleGroup("TokenEditor")

  private val lettersBox = new HBox() {
    spacing = Gap
    alignment = Pos.Center
    nodeOrientation = NodeOrientation.RightToLeft
    padding = Insets(Gap, Gap, Gap, Gap)
  }

  private val subscriptions = ListBuffer.empty[Subscription]

  getChildren.add(initializeSkin)

  import TokenSkin.*

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
      .onChange((_, _, nv) => initializeLettersPane(nv))
    gridPane.add(lettersPane, 0, 2, 2, 2)

    val borderPane = new BorderPane() {
      styleClass = ObservableBuffer[String]("border")
      center = gridPane
    }
    BorderPane.setAlignment(gridPane, Pos.Center)
    borderPane
  }

  private def createLocationComboBox = {
    val comboBox = new ComboBox[Location]()
    comboBox.setButtonCell(new LocationListCell())
    comboBox.setCellFactory((_: ListView[Location]) => new LocationListCell())
    comboBox.setDisable(true)

    comboBox.valueProperty().bindBidirectional(control.selectedLocationProperty)

    control
      .locationsProperty
      .onChange((_, changes) => {
        changes.foreach {
          case ObservableBuffer.Add(_, added) =>
            if added.nonEmpty then {
              val seq = added.toSeq
              comboBox.getItems.removeAll(seq*)
              comboBox.getItems.addAll(seq*)
              comboBox.setValue(added.head)
              comboBox.setDisable(false)
            }

          case ObservableBuffer.Remove(_, removed) =>
            if removed.nonEmpty then comboBox.getItems.removeAll(removed.toSeq*)
            if comboBox.getItems.isEmpty then comboBox.setDisable(true)
            else comboBox.setDisable(false)

          case _ => ()
        }
      })
    comboBox
  }

  private def createTranslationArea = {
    val textArea = new TextArea(control.translationText) {
      prefRowCount = 2
      prefColumnCount = 1
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
        val word = ArabicWord(control.token.token)
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
        .onChange((_, _, nv) =>
          if !nv then {
            val index = labelView.getId.toInt
            if location.startIndex == index then {
              showInvalidOperationAlert
              labelView.select = true
            } else
              control.refresh(
                updateLocation(location, index),
                Some(createLocation(location, index))
              )
          }
        )
    subscriptions.addOne(subscription)
  }

  private def showInvalidOperationAlert = {
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
  }

  private def updateLocation(location: Location, index: Int) = {
    val tokenText = control.token.token
    val locationText = tokenText.substring(location.startIndex, index)
    location.copy(
      endIndex = index,
      derivedText = locationText,
      alternateText = locationText,
      text = locationText
    )
  }

  private def createLocation(currentLocation: Location, index: Int) = {
    val tokenText = control.token.token
    val endIndex = tokenText.length
    val locationText = tokenText.substring(index, endIndex)
    Location(
      chapterNumber = currentLocation.chapterNumber,
      verseNumber = currentLocation.verseNumber,
      tokenNumber = currentLocation.tokenNumber,
      locationNumber = currentLocation.locationNumber + 1,
      hidden = false,
      startIndex = index,
      endIndex = endIndex,
      derivedText = locationText,
      text = locationText,
      alternateText = locationText
    )
  }
}

object TokenSkin {

  private val Gap = 10.0

  private val BaseFont = Font(
    tokenEditorPreferences.englishFontName,
    FontWeight.Normal,
    FontPosture.Regular,
    14.0
  )

  def apply(control: TokenView): TokenSkin =
    new TokenSkin(control)
}
