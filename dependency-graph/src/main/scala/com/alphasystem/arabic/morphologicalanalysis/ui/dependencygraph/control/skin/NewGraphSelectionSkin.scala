package com.alphasystem
package arabic
package morphologicalanalysis
package ui
package dependencygraph
package control
package skin

import de.jensd.fx.glyphs.fontawesome.{ FontAwesomeIcon, FontAwesomeIconView }
import model.ArabicLabel
import morphology.model.Token
import javafx.application.Platform
import javafx.scene.control.{ SkinBase, ListView as JListView }
import org.controlsfx.control.CheckListView
import scalafx.Includes.*
import scalafx.collections.ObservableBuffer
import scalafx.geometry.{ Insets, NodeOrientation, Pos }
import scalafx.scene.control.{ Button, ContentDisplay, Label, ListView, Tooltip }
import scalafx.scene.layout.{ BorderPane, GridPane, HBox, VBox }

class NewGraphSelectionSkin(control: NewGraphSelectionView) extends SkinBase[NewGraphSelectionView](control) {

  private lazy val checkListView = new CheckListView[ArabicLabel[Token]](control.tokens)

  getChildren.addAll(initializeSkin)

  private def initializeSkin: BorderPane = {
    val gridPane = new GridPane() {
      vgap = DefaultGap
      hgap = DefaultGap
      padding = Insets(DefaultGap, DefaultGap, DefaultGap, DefaultGap)
    }

    initializeChapterComboBox(gridPane)
    initializeVerseComboBox(gridPane)
    gridPane.add(initializeTokensPane, 0, 4)

    new BorderPane() {
      center = gridPane
    }
  }

  private def initializeChapterComboBox(gridPane: GridPane): Unit = {
    gridPane.add(Label("Chapter:"), 0, 0)
    val comboBox = ArabicSupportEnumComboBox(control.chapters.toArray, ListType.LABEL_AND_CODE)
    comboBox.setDisable(true)
    control
      .selectedChapterProperty
      .bindBidirectional(comboBox.valueProperty())
    control.chaptersProperty.onChange { (_, changes) =>
      changes.foreach {
        case ObservableBuffer.Add(_, added) =>
          comboBox.getItems.addAll(added.toSeq*)
          if added.nonEmpty then comboBox.setValue(added.head)
          if control.chapters.nonEmpty then comboBox.setDisable(false)

        case ObservableBuffer.Remove(_, removed) =>
          comboBox.getItems.removeAll(removed.toSeq*)
          comboBox.setValue(null)
          if control.chapters.isEmpty then comboBox.setDisable(true)

        case ObservableBuffer.Reorder(_, _, _) => ()
        case ObservableBuffer.Update(_, _)     => ()
      }
    }

    comboBox.valueProperty().onChange((_, _, _) => clearAll())

    gridPane.add(comboBox, 0, 1)
  }

  private def initializeVerseComboBox(gridPane: GridPane): Unit = {
    gridPane.add(Label("Verse:"), 0, 2)
    val comboBox = ArabicSupportEnumComboBox(control.versesProperty.toArray, ListType.CODE_ONLY)
    comboBox.setDisable(true)
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

    gridPane.add(comboBox, 0, 3)
  }

  private def initializeAllSelectedTokens = {
    val gridPane = new GridPane() {
      vgap = DefaultGap
      hgap = DefaultGap
      padding = Insets(DefaultGap, DefaultGap, DefaultGap, DefaultGap)
    }

    gridPane.add(Label("Selected Tokens:"), 0, 0)

    val allSelectedItemsView = new ListView(control.allSelectedTokens) {
      nodeOrientation = NodeOrientation.RightToLeft
      cellFactory = ArabicSupportEnumCellFactory[ArabicLabel[Token]](ListType.LABEL_ONLY)
    }
    gridPane.add(allSelectedItemsView, 0, 1)

    gridPane
  }

  private def initializeTokensPane =
    new HBox() {
      spacing = DefaultGap
      padding = Insets(DefaultGap, DefaultGap, DefaultGap, DefaultGap)
      children = Seq(initializeAllSelectedTokens, initializeButtonPane, initializeTokensList)
    }

  private def initializeTokensList = {
    val gridPane = new GridPane() {
      vgap = DefaultGap
      hgap = DefaultGap
      padding = Insets(DefaultGap, DefaultGap, DefaultGap, DefaultGap)
    }

    gridPane.add(Label("Tokens:"), 0, 0)
    checkListView.setNodeOrientation(NodeOrientation.RightToLeft)
    checkListView.setCellFactory((_: JListView[ArabicLabel[Token]]) =>
      ArabicSupportEnumCheckBoxListCell(
        ListType.LABEL_ONLY,
        (item: ArabicLabel[Token]) => checkListView.getItemBooleanProperty(item)
      )
    )
    val checkModel = checkListView.getCheckModel
    checkModel
      .getCheckedItems
      .onChange((_, changes) => {
        changes.foreach {
          case ObservableBuffer.Add(_, added) =>
            val currentTokenNumber = added.head.userData.tokenNumber
            val lastTokenNumber = control.selectedTokens.lastOption.map(_.userData.tokenNumber).getOrElse(-1)

            // we only allow consecutive selection
            if lastTokenNumber <= -1 || lastTokenNumber + 1 == currentTokenNumber then
              control.selectedTokens.addAll(added)
            else {
              // TOD0: show error message
              Platform.runLater(() => checkModel.clearCheck(currentTokenNumber - 1))
            }

          case ObservableBuffer.Remove(_, removed) => control.selectedTokens.removeAll(removed.toSeq*)

          case _ => ()
        }
      })

    gridPane.add(checkListView, 0, 1)

    gridPane
  }

  private def initializeButtonPane = {
    val copySelectedTokensButton = new Button() {
      graphic = new FontAwesomeIconView(FontAwesomeIcon.ANGLE_LEFT, "2em")
      contentDisplay = ContentDisplay.GraphicOnly
      tooltip = Tooltip("Copy Selected Tokens")
      onAction = event => {
        control.allSelectedTokens.addAll(control.selectedTokens)
        control.selectedTokens.clear()
        event.consume()
      }
    }

    val copyAllTokensButton = new Button() {
      graphic = new FontAwesomeIconView(FontAwesomeIcon.ANGLE_DOUBLE_LEFT, "2em")
      contentDisplay = ContentDisplay.GraphicOnly
      tooltip = Tooltip("Copy All Tokens")
      onAction = event => {
        control.allSelectedTokens.addAll(control.tokens)
        event.consume()
      }
    }

    val removeAll = new Button() {
      graphic = new FontAwesomeIconView(FontAwesomeIcon.TRASH, "2em")
      contentDisplay = ContentDisplay.GraphicOnly
      tooltip = Tooltip("Remove All Tokens")
      onAction = event => {
        clearAll()
        event.consume()
      }
    }

    new VBox() {
      spacing = DefaultGap
      padding = Insets(DefaultGap, DefaultGap, DefaultGap, DefaultGap)
      children = Seq(copySelectedTokensButton, copyAllTokensButton, removeAll)
      alignment = Pos.Center
      alignmentInParent = Pos.Center
    }
  }

  private def clearAll(): Unit =
    Platform.runLater(() => {
      control.allSelectedTokens.clear()
      control.selectedTokens.clear()
    })

}

object NewGraphSelectionSkin {
  def apply(control: NewGraphSelectionView): NewGraphSelectionSkin = new NewGraphSelectionSkin(control)
}
