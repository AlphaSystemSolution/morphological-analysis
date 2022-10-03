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
      padding = Insets(Gap, Gap, Gap, Gap)
    }

    initializeChapterComboBox(gridPane)
    initializeVerseComboBox(gridPane)

    new BorderPane() {
      center = gridPane
    }
  }

  private def initializeChapterComboBox(gridPane: GridPane): Unit = {
    gridPane.add(Label("Chapter:"), 0, 0)
    val chapterComboBox =
      ArabicSupportEnumComboBox(
        control.chapters.toArray,
        ListType.LABEL_AND_CODE
      )
    chapterComboBox.setDisable(true)
    control
      .selectedChapterProperty
      .bindBidirectional(chapterComboBox.valueProperty())
    control.chaptersProperty.onChange { (_, changes) =>
      changes.foreach { change =>
        change match
          case ObservableBuffer.Add(_, added) =>
            chapterComboBox.getItems.addAll(added.toSeq*)
            if added.nonEmpty then chapterComboBox.setValue(added.head)
            if control.chapters.nonEmpty then chapterComboBox.setDisable(false)

          case ObservableBuffer.Remove(_, removed) =>
            chapterComboBox.getItems.removeAll(removed.toSeq*)
            chapterComboBox.setValue(null)
            if control.chapters.isEmpty then chapterComboBox.setDisable(true)

          case ObservableBuffer.Reorder(_, _, _) => ()
          case ObservableBuffer.Update(_, _)     => ()
      }
    }
    gridPane.add(chapterComboBox, 0, 1)
  }

  private def initializeVerseComboBox(gridPane: GridPane): Unit = {
    gridPane.add(Label("Verse:"), 1, 0)
    lazy val verseComboBox =
      ArabicSupportEnumComboBox(
        control.versesProperty.toArray,
        ListType.CODE_ONLY
      )
    verseComboBox.setDisable(true)
    control
      .selectedVerseProperty
      .bindBidirectional(verseComboBox.valueProperty())
    control.versesProperty.onChange { (_, changes) =>
      changes.foreach { change =>
        change match
          case ObservableBuffer.Add(_, added) =>
            verseComboBox.getItems.addAll(added.toSeq*)
            if added.nonEmpty then verseComboBox.setValue(added.head)
            if control.versesProperty.nonEmpty then
              verseComboBox.setDisable(false)

          case ObservableBuffer.Remove(_, removed) =>
            verseComboBox.getItems.removeAll(removed.toSeq*)
            verseComboBox.setValue(null)
            if control.versesProperty.isEmpty then
              verseComboBox.setDisable(true)

          case ObservableBuffer.Reorder(_, _, _) => ()
          case ObservableBuffer.Update(_, _)     => ()
      }
    }
    gridPane.add(verseComboBox, 1, 1)
  }
}

object ChapterVerseSelectionSkin {

  private val Gap = 10.0

  def apply(control: ChapterVerseSelectionView) =
    new ChapterVerseSelectionSkin(control)
}
