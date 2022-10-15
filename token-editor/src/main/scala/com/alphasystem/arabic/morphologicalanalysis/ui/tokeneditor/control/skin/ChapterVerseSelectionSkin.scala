package com.alphasystem.arabic.morphologicalanalysis.ui.tokeneditor.control.skin

import com.alphasystem.arabic.model.{ ArabicLabel, ArabicWord }
import com.alphasystem.arabic.morphologicalanalysis.ui.tokeneditor.*
import com.alphasystem.arabic.morphologicalanalysis.ui.tokeneditor.control.ChapterVerseSelectionView
import com.alphasystem.arabic.morphologicalanalysis.ui.tokeneditor.control.skin.NounPropertiesSkin.Gap
import com.alphasystem.morphologicalanalysis.ui.{
  ArabicSupportEnumComboBox,
  ListType
}
import javafx.beans.binding.Bindings
import scalafx.geometry.Pos
import javafx.scene.control.SkinBase
import scalafx.collections.ObservableBuffer
import scalafx.geometry.Insets
import scalafx.scene.control.Label
import scalafx.scene.layout.{ BorderPane, GridPane }

import scala.collection.immutable.{ AbstractSeq, LinearSeq }

class ChapterVerseSelectionSkin(control: ChapterVerseSelectionView)
    extends SkinBase[ChapterVerseSelectionView](control) {

  getChildren.add(initializeSkin)

  import ChapterVerseSelectionSkin.*

  private def initializeSkin = {
    val gridPane = new GridPane() {
      styleClass = ObservableBuffer("border")
      vgap = Gap
      hgap = Gap
      alignment = Pos.Center
      padding = Insets(Gap, Gap, Gap, Gap)
    }

    initializeChapterComboBox(gridPane)
    initializeVerseComboBox(gridPane)
    initializeTokenComboBox(gridPane)

    val pane = new BorderPane()
    BorderPane.setAlignment(gridPane, Pos.Center)
    pane.center = gridPane
    pane
  }

  private def initializeChapterComboBox(gridPane: GridPane): Unit = {
    gridPane.add(Label("Chapter:"), 0, 0)
    val comboBox =
      ArabicSupportEnumComboBox(
        control.chapters.toArray,
        ListType.LABEL_AND_CODE
      )
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
    gridPane.add(comboBox, 0, 1)
  }

  private def initializeVerseComboBox(gridPane: GridPane): Unit = {
    gridPane.add(Label("Verse:"), 1, 0)
    val comboBox =
      ArabicSupportEnumComboBox(
        control.versesProperty.toArray,
        ListType.CODE_ONLY
      )
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
    gridPane.add(comboBox, 1, 1)
  }

  private def initializeTokenComboBox(gridPane: GridPane): Unit = {
    gridPane.add(Label("Token:"), 2, 0)
    val comboBox =
      ArabicSupportEnumComboBox(
        control.tokens.toArray,
        ListType.LABEL_ONLY
      )
    comboBox.setDisable(true)
    control
      .selectedTokenProperty
      .bindBidirectional(comboBox.valueProperty())
    control
      .tokensProperty
      .onChange((_, changes) => {
        changes.foreach {
          case ObservableBuffer.Add(_, added) =>
            comboBox.getItems.addAll(added.toSeq*)
            if added.nonEmpty then comboBox.setValue(added.head)
            if control.tokensProperty.nonEmpty then comboBox.setDisable(false)

          case ObservableBuffer.Remove(_, removed) =>
            comboBox.getItems.removeAll(removed.toSeq*)
            comboBox.setValue(null)
            if control.tokensProperty.isEmpty then comboBox.setDisable(true)

          case ObservableBuffer.Reorder(_, _, _) => ()
          case ObservableBuffer.Update(_, _)     => ()
        }
      })
    gridPane.add(comboBox, 2, 1)
  }
}

object ChapterVerseSelectionSkin {

  private val Gap = 10.0

  def apply(control: ChapterVerseSelectionView) =
    new ChapterVerseSelectionSkin(control)
}
