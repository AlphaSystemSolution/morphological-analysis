package com.alphasystem.arabic.morphologicalanalysis.ui.tokeneditor.control

import com.alphasystem.arabic.model.{ ArabicLabel, ArabicWord }
import com.alphasystem.arabic.morphologicalanalysis.morphology.model.{
  Chapter,
  Verse
}
import com.alphasystem.arabic.morphologicalanalysis.ui.tokeneditor.control.skin.ChapterVerseSelectionSkin
import com.alphasystem.arabic.morphologicalanalysis.ui.tokeneditor.service.ServiceFactory
import javafx.application.Platform
import javafx.scene.control.{ Control, Skin }
import scalafx.beans.property.{ ObjectProperty, ReadOnlyIntegerWrapper }
import scalafx.collections.ObservableBuffer

class ChapterVerseSelectionView(serviceFactory: ServiceFactory)
    extends Control {

  private[control] val chaptersProperty
    : ObservableBuffer[ArabicLabel[Chapter]] =
    ObservableBuffer[ArabicLabel[Chapter]]()

  val selectedChapterProperty: ObjectProperty[ArabicLabel[Chapter]] =
    ObjectProperty[ArabicLabel[Chapter]](this, "selectedChapter")

  val selectedVerseProperty: ObjectProperty[ArabicLabel[Int]] =
    ObjectProperty[ArabicLabel[Int]](this, "selectedVerse")

  private[control] val versesProperty: ObservableBuffer[ArabicLabel[Int]] =
    ObservableBuffer[ArabicLabel[Int]]()

  private lazy val chapterService = serviceFactory.chapterService(-1)

  selectedChapterProperty.onChange((_, _, nv) => {
    val verses = loadedVerses(
      Option(nv)
        .map(_.userData.verseCount)
        .getOrElse(0)
    )
    versesProperty.remove(0, versesProperty.size)
    versesProperty.addAll(verses*)
  })

  loadChapters()
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

  private def loadChapters(): Unit = {
    Platform.runLater { () =>
      chapterService.onSucceeded = event => {
        val result = event.getSource.getValue.asInstanceOf[Seq[Chapter]]
        chapters = result.map(_.toArabicLabel)
        event.consume()
      }
      chapterService.start()
    }
  }
  private def loadedVerses(totalVerseCount: Int) =
    (1 to totalVerseCount).map(i => ArabicLabel(i, i.toString, ArabicWord()))
}

object ChapterVerseSelectionView {

  def apply(serviceFactory: ServiceFactory): ChapterVerseSelectionView =
    new ChapterVerseSelectionView(serviceFactory)
}
