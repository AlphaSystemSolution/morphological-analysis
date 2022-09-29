package com.alphasystem.arabic.morphologicalanalysis.ui.tokeneditor.control

import com.alphasystem.arabic.model.{ ArabicLabel, ArabicWord }
import com.alphasystem.arabic.morphologicalanalysis.morphology.model.{
  Chapter,
  Verse
}
import com.alphasystem.arabic.morphologicalanalysis.ui.tokeneditor.control.skin.ChapterVerseSelectionSkin
import javafx.scene.control.{ Control, Skin }
import scalafx.beans.property.{ ObjectProperty, ReadOnlyIntegerWrapper }
import scalafx.collections.ObservableBuffer

class ChapterVerseSelectionView(chaptersData: Seq[Chapter]) extends Control {

  private[control] val chaptersProperty
    : ObservableBuffer[ArabicLabel[Chapter]] =
    ObservableBuffer[ArabicLabel[Chapter]](chaptersData.map(_.toArabicLabel)*)

  val selectedChapterProperty: ObjectProperty[ArabicLabel[Chapter]] =
    ObjectProperty[ArabicLabel[Chapter]](this, "selectedChapter")

  val selectedVerseProperty: ObjectProperty[ArabicLabel[Int]] =
    ObjectProperty[ArabicLabel[Int]](this, "selectedVerse")

  private[control] val versesProperty: ObservableBuffer[ArabicLabel[Int]] =
    ObservableBuffer[ArabicLabel[Int]](
      loadedVerses(chaptersData.headOption.map(_.verseCount).getOrElse(0))*
    )

  selectedChapterProperty.onChange((_, _, nv) => {
    val verses = loadedVerses(
      Option(nv)
        .map(_.userData.verseCount)
        .getOrElse(0)
    )
    versesProperty.remove(0, versesProperty.size)
    versesProperty.addAll(verses*)
  })

  setSkin(createDefaultSkin())

  def chapters: Seq[ArabicLabel[Chapter]] = chaptersProperty.toSeq

  def chapters_=(elems: Seq[ArabicLabel[Chapter]]): Unit =
    chaptersProperty.addAll(elems)

  def selectedChapter: ArabicLabel[Chapter] = selectedChapterProperty.value

  def selectedChapter_=(value: ArabicLabel[Chapter]): Unit =
    selectedChapterProperty.value = value

  def selectedVerse: ArabicLabel[Int] = selectedVerseProperty.value

  def selectedVerse_=(value: ArabicLabel[Int]): Unit =
    selectedVerseProperty.value = value

  override def createDefaultSkin(): Skin[_] = ChapterVerseSelectionSkin(this)

  override def getUserAgentStylesheet: String = Thread
    .currentThread()
    .getContextClassLoader
    .getResource("application.css")
    .toExternalForm

  private def loadedVerses(totalVerseCount: Int) =
    (1 to totalVerseCount).map(i => ArabicLabel(i, i.toString, ArabicWord()))
}

object ChapterVerseSelectionView {

  def apply(chaptersData: Seq[Chapter]): ChapterVerseSelectionView =
    new ChapterVerseSelectionView(chaptersData)
}
