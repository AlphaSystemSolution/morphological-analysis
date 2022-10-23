package com.alphasystem
package arabic
package morphologicalanalysis
package ui
package tokeneditor
package control

import model.ArabicLabel
import morphology.model.{ Chapter, Token, Verse }
import morphology.persistence.cache.TokenRequest
import skin.ChapterVerseSelectionSkin
import service.ServiceFactory
import javafx.application.Platform
import javafx.scene.control.{ Control, Skin }
import scalafx.beans.property.{ ObjectProperty, ReadOnlyIntegerWrapper }
import scalafx.collections.ObservableBuffer
import scalafx.concurrent.Service

class ChapterVerseSelectionView(serviceFactory: ServiceFactory) extends Control {

  private[control] val chaptersProperty: ObservableBuffer[ArabicLabel[Chapter]] =
    ObservableBuffer[ArabicLabel[Chapter]]()

  private[control] val versesProperty: ObservableBuffer[ArabicLabel[Int]] =
    ObservableBuffer[ArabicLabel[Int]]()

  private[control] val tokensProperty: ObservableBuffer[ArabicLabel[Token]] =
    ObservableBuffer[ArabicLabel[Token]]()

  val selectedChapterProperty: ObjectProperty[ArabicLabel[Chapter]] =
    ObjectProperty[ArabicLabel[Chapter]](this, "selectedChapter")

  val selectedVerseProperty: ObjectProperty[ArabicLabel[Int]] =
    ObjectProperty[ArabicLabel[Int]](this, "selectedVerse")

  val selectedTokenProperty: ObjectProperty[ArabicLabel[Token]] =
    ObjectProperty[ArabicLabel[Token]](this, "selectedToken")

  private val chapterService = serviceFactory.chapterService(None)
  private val tokenServiceF = serviceFactory.tokenService

  selectedChapterProperty.onChange((_, _, nv) => {
    val verses = loadedVerses(
      Option(nv)
        .map(_.userData.verseCount)
        .getOrElse(0)
    )
    versesProperty.clear()
    versesProperty.addAll(verses*)
  })

  selectedVerseProperty.onChange((_, _, nv) => {
    if Option(nv).isDefined then {
      val chapterNumber = selectedChapter.userData.chapterNumber
      val verseNumber = nv.userData
      loadTokens(chapterNumber, verseNumber)
    } else {
      selectedToken = null
    }
  })

  loadChapters()
  setSkin(createDefaultSkin())

  def chapters: Seq[ArabicLabel[Chapter]] = chaptersProperty.toSeq

  def tokens: Seq[ArabicLabel[Token]] = tokensProperty.toSeq

  def selectedChapter: ArabicLabel[Chapter] = selectedChapterProperty.value

  def selectedChapter_=(value: ArabicLabel[Chapter]): Unit =
    selectedChapterProperty.value = value

  def selectedVerse: ArabicLabel[Int] = selectedVerseProperty.value

  def selectedVerse_=(value: ArabicLabel[Int]): Unit =
    selectedVerseProperty.value = value

  def selectedToken: ArabicLabel[Token] = selectedTokenProperty.value

  def selectedToken_=(value: ArabicLabel[Token]): Unit =
    selectedTokenProperty.value = value

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
        chaptersProperty.addAll(result.map(_.toArabicLabel))
        event.consume()
      }
      chapterService.start()
    }
  }
  private def loadedVerses(totalVerseCount: Int) =
    (1 to totalVerseCount).map(i => ArabicLabel(i, i.toString, ""))

  private def loadTokens(chapterNumber: Int, verseNumber: Int): Unit = {
    val tokenService = tokenServiceF(TokenRequest(chapterNumber, verseNumber))
    Platform.runLater(() => {
      tokenService.onSucceeded = event => {
        selectedToken = null
        val result = event.getSource.getValue.asInstanceOf[Seq[Token]]
        val tokens = result.map(_.toArabicLabel)
        tokensProperty.clear()
        tokensProperty.addAll(tokens)
        if tokens.nonEmpty then selectedToken = tokens.head
        event.consume()
      }
      tokenService.start()
    })
  }
}

object ChapterVerseSelectionView {

  def apply(serviceFactory: ServiceFactory): ChapterVerseSelectionView =
    new ChapterVerseSelectionView(serviceFactory)
}
