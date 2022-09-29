package com.alphasystem.arabic.morphologicalanalysis.ui.tokeneditor.control.skin

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
    val ctl = getSkinnable

    val gridPane = new GridPane() {
      styleClass = ObservableBuffer("border")
      vgap = Gap
      hgap = Gap
      padding = Insets(Gap, Gap, Gap, Gap)
    }

    gridPane.add(Label("Chapter:"), 0, 0)
    val chapterComboBox =
      ArabicSupportEnumComboBox(ctl.chapters.toArray, ListType.LABEL_AND_CODE)
    ctl
      .selectedChapterProperty
      .bindBidirectional(chapterComboBox.valueProperty())
    gridPane.add(chapterComboBox, 0, 1)

    gridPane.add(Label("Verse:"), 1, 0)
    val verseComboBox =
      ArabicSupportEnumComboBox(ctl.versesProperty.toArray, ListType.CODE_ONLY)
    ctl.selectedVerseProperty.bindBidirectional(verseComboBox.valueProperty())
    ctl.versesProperty.onChange { (_, changes) =>
      changes.foreach { change =>
        change match
          case ObservableBuffer.Add(_, added) =>
            verseComboBox.getItems.addAll(added.toSeq*)
            if added.nonEmpty then verseComboBox.setValue(added.head)
          case ObservableBuffer.Remove(_, removed) =>
            verseComboBox.getItems.removeAll(removed.toSeq*)
            verseComboBox.setValue(null)
          case ObservableBuffer.Reorder(_, _, _) => ()
          case ObservableBuffer.Update(_, _)     => ()
      }
    }
    gridPane.add(verseComboBox, 1, 1)

    new BorderPane() {
      center = gridPane
    }
  }
}

object ChapterVerseSelectionSkin {

  private val Gap = 10.0

  def apply(control: ChapterVerseSelectionView) =
    new ChapterVerseSelectionSkin(control)
}
