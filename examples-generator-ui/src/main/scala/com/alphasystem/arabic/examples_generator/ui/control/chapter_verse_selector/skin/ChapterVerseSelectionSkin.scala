package com.alphasystem
package arabic
package examples_generator
package ui
package control
package chapter_verse_selector
package skin

import arabic.fx.ui.util.*
import arabic.morphologicalanalysis.ui.ArabicSupportEnumComboBox
import arabic.model.{ ArabicLabel, ArabicWord }
import arabic.morphologicalanalysis.ui.ListType.LABEL_ONLY
import com.alphasystem.arabic.examples_generator.ui.model.VerseRange
import scalafx.Includes.*
import javafx.scene.control.SkinBase
import scalafx.collections.ObservableBuffer
import scalafx.geometry.NodeOrientation.RightToLeft
import scalafx.geometry.{ Insets, Pos }
import scalafx.scene.control.TextArea
import scalafx.scene.layout.{ BorderPane, GridPane }

class ChapterVerseSelectionSkin private[chapter_verse_selector] (control: ChapterVerseSelectionView)
    extends SkinBase[ChapterVerseSelectionView](control) {

  private val verseStartCombobox = ArabicSupportEnumComboBox(Array.empty[ArabicLabel[Int]], LABEL_ONLY)
  private val verseEndCombobox = ArabicSupportEnumComboBox(Array.empty[ArabicLabel[Int]], LABEL_ONLY)
  verseStartCombobox.setDisable(true)
  verseEndCombobox.setDisable(true)
  private val selectedText = new TextArea {
    font = preferences.arabicFont(30)
    editable = true
    nodeOrientation = RightToLeft
    wrapText = true
  }

  // make sure start verse index is equal or less than verse end index
  verseStartCombobox.valueProperty().onChange((_, _, nv) => updateStartAndEndVerseSelection(nv, verseEndCombobox))
  verseEndCombobox.valueProperty().onChange((_, _, nv) => updateStartAndEndVerseSelection(nv, verseStartCombobox))
  control.selectedTextProperty.bindBidirectional(selectedText.textProperty())

  getChildren.addAll(mainPane)

  private lazy val mainPane = {
    new BorderPane {
      center = gridPane
      BorderPane.setAlignment(gridPane, Pos.Center)
    }
  }

  private lazy val gridPane = {
    val gridPane = new GridPane {
      styleClass = ObservableBuffer("border")
      vgap = 10
      hgap = 10
      alignment = Pos.Center
      padding = Insets(10, 10, 10, 10)
    }

    gridPane.add(createLabel("Chapter:"), 0, 0)
    gridPane.add(chaptersComboBox, 1, 0)

    gridPane.add(createLabel("Verse start:"), 0, 1)
    gridPane.add(verseStartCombobox, 1, 1)

    gridPane.add(createLabel("Verse end:"), 0, 2)
    gridPane.add(verseEndCombobox, 1, 2)

    gridPane.add(createLabel("Selected verse(s):"), 0, 3)
    gridPane.add(selectedText, 1, 3)

    gridPane
  }

  private lazy val chaptersComboBox = {
    val comboBox = ArabicSupportEnumComboBox(control.chapters.map(_.toArabicLabel).toArray, LABEL_ONLY)
    comboBox.setDisable(true)

    control.selectedChapterProperty.onChange { (_, _, nv) =>
      if Option(nv).isDefined then {
        comboBox.setValue(nv.toArabicLabel)
        refreshVerseCombobox(verseStartCombobox, nv.verseCount)
        refreshVerseCombobox(verseEndCombobox, nv.verseCount)
      } else {
        clearVerseCombobox(verseStartCombobox)
        clearVerseCombobox(verseEndCombobox)
      }
      updateSelectedText()
    }
    comboBox.valueProperty().onChange { (_, _, nv) =>
      if Option(nv).isDefined then {
        control.selectedChapter = nv.userData
      }
    }

    control.chaptersProperty.onChange { (_, changes) =>
      changes.foreach {
        case ObservableBuffer.Add(_, added) =>
          comboBox.getItems.addAll(added.map(_.toArabicLabel).toSeq*)
          if added.nonEmpty then comboBox.setValue(added.head.toArabicLabel)
          if control.chapters.nonEmpty then comboBox.setDisable(false)

        case ObservableBuffer.Remove(_, removed) =>
          comboBox.getItems.removeAll(removed.map(_.toArabicLabel).toSeq*)
          comboBox.setValue(null)
          if control.chapters.isEmpty then {
            comboBox.setDisable(true)
            clearVerseCombobox(verseStartCombobox)
            clearVerseCombobox(verseEndCombobox)
          }

        case ObservableBuffer.Reorder(_, _, _) => ()
        case ObservableBuffer.Update(_, _)     => ()
      }
    }
    comboBox
  }

  private def refreshVerseCombobox(combobox: ArabicSupportEnumComboBox[ArabicLabel[Int]], verseCount: Int): Unit = {
    combobox.getItems.clear()
    if verseCount > 0 then {
      val items = (1 to verseCount).map(value => ArabicLabel[Int](value, value.toString, ArabicWord(value).unicode))
      combobox.getItems.addAll(items*)
      combobox.setValue(items.head)
    }
    combobox.setDisable(verseCount <= 0)
  }

  private def clearVerseCombobox(combobox: ArabicSupportEnumComboBox[ArabicLabel[Int]]): Unit = {
    combobox.getItems.clear()
    combobox.setDisable(true)
  }

  private def updateStartAndEndVerseSelection(
    selectedValue: ArabicLabel[Int],
    otherCombobox: ArabicSupportEnumComboBox[ArabicLabel[Int]]
  ): Unit = {
    if Option(selectedValue).isDefined then {
      val selectedStartIndex = verseStartCombobox.getSelectionModel.getSelectedIndex
      val selectedEndIndex = verseEndCombobox.getSelectionModel.getSelectedIndex
      if selectedEndIndex < selectedStartIndex then verseEndCombobox.getSelectionModel.select(selectedStartIndex)
    } else otherCombobox.getSelectionModel.selectFirst()

    updateSelectedText()
  }

  private def updateSelectedText(): Unit = {
    control.verseRange = null
    val startValue = verseStartCombobox.getValue
    val endValue = verseEndCombobox.getValue
    if Option(startValue).isDefined && Option(endValue).isDefined then {
      control.verseRange = VerseRange(startValue.userData, endValue.userData)
    }
  }
}

object ChapterVerseSelectionSkin {
  private[chapter_verse_selector] def apply(control: ChapterVerseSelectionView) = new ChapterVerseSelectionSkin(control)
}
