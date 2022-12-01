package com.alphasystem
package arabic
package morphologicalanalysis
package ui
package commons
package control

import javafx.application.Platform
import service.ServiceFactory
import model.ArabicLabel
import morphology.model.Chapter
import scalafx.Includes.*
import scalafx.beans.property.{ ObjectProperty, ReadOnlyObjectWrapper }
import scalafx.collections.ObservableBuffer
import scalafx.event.subscriptions.Subscription

trait ChapterVersesLoader {

  protected val serviceFactory: ServiceFactory

  val chaptersProperty: ObservableBuffer[ArabicLabel[Chapter]] = ObservableBuffer[ArabicLabel[Chapter]]()

  val versesProperty: ObservableBuffer[ArabicLabel[Int]] = ObservableBuffer[ArabicLabel[Int]]()

  val selectedChapterProperty: ReadOnlyObjectWrapper[ArabicLabel[Chapter]] =
    new ReadOnlyObjectWrapper[ArabicLabel[Chapter]](this, "selectedChapter")

  val selectedVerseProperty: ReadOnlyObjectWrapper[ArabicLabel[Int]] =
    new ReadOnlyObjectWrapper[ArabicLabel[Int]](this, "selectedVerse")

  def chapters: Seq[ArabicLabel[Chapter]] = chaptersProperty.toSeq

  def selectedChapter: ArabicLabel[Chapter] = selectedChapterProperty.value
  def selectedChapter_=(value: ArabicLabel[Chapter]): Unit = selectedChapterProperty.value = value

  def selectedVerse: ArabicLabel[Int] = selectedVerseProperty.value
  def selectedVerse_=(value: ArabicLabel[Int]): Unit = selectedVerseProperty.value = value

  protected def loadChapters(): Unit = {
    val chapterService = serviceFactory.chapterService
    Platform.runLater { () =>
      chapterService.onSucceeded = event => {
        val result = event.getSource.getValue.asInstanceOf[Seq[Chapter]]
        chaptersProperty.addAll(result.map(_.toArabicLabel))
        event.consume()
      }

      chapterService.onFailed = event => {
        Console.err.println("Failed to load chapters")
        event.consume()
      }

      chapterService.start()
    }
  }

  protected def bindVersesOnSelectedChapter: Subscription =
    selectedChapterProperty.onChange((_, _, nv) => {
      val verses = loadedVerses(
        Option(nv)
          .map(_.userData.verseCount)
          .getOrElse(0)
      )
      versesProperty.clear()
      versesProperty.addAll(verses*)
    })

  private def loadedVerses(totalVerseCount: Int) =
    (1 to totalVerseCount).map(i => ArabicLabel(i, i.toString, ""))
}
