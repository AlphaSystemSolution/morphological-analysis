package com.alphasystem
package arabic
package morphologicalanalysis
package ui
package dependencygraph
package control

import com.alphasystem.arabic.model.ArabicLabel
import morphology.persistence.cache.TokenRequest
import skin.NewGraphSelectionSkin
import morphology.model.Token
import commons.control.ChapterVersesLoader
import commons.service.ServiceFactory
import javafx.application.Platform
import javafx.scene.control.{ Control, Skin }
import scalafx.collections.ObservableBuffer

class NewGraphSelectionView(override protected val serviceFactory: ServiceFactory)
    extends Control
    with ChapterVersesLoader {

  private[control] val tokens: ObservableBuffer[ArabicLabel[Token]] = ObservableBuffer.empty[ArabicLabel[Token]]

  private[control] val selectedTokens: ObservableBuffer[ArabicLabel[Token]] = ObservableBuffer.empty[ArabicLabel[Token]]

  private[control] val allSelectedTokens: ObservableBuffer[ArabicLabel[Token]] =
    ObservableBuffer.empty[ArabicLabel[Token]]

  bindVersesOnSelectedChapter
  loadChapters()
  selectedVerseProperty.onChange((_, _, nv) => {
    if Option(nv).isDefined then {
      val chapterNumber = selectedChapter.userData.chapterNumber
      val verseNumber = nv.userData
      loadTokens(chapterNumber, verseNumber)
    }
  })
  setSkin(createDefaultSkin())

  override def createDefaultSkin(): Skin[?] = NewGraphSelectionSkin(this)

  private def loadTokens(chapterNumber: Int, verseNumber: Int): Unit = {
    val service = serviceFactory.tokenService(TokenRequest(chapterNumber, verseNumber))

    service.onFailed = event => {
      Console.err.println("Unable to load tokens")
      event.getSource.getException.printStackTrace()
      event.consume()
    }

    service.onSucceeded = event => {
      val result = event.getSource.getValue.asInstanceOf[Seq[Token]].map(_.toArabicLabel)
      tokens.clear()
      tokens.addAll(result)
      event.consume()
    }

    Platform.runLater(() => service.start())
  }

  def clearAll(): Unit = {
    selectedChapter = chapters.head
    selectedVerse = versesProperty.head
    selectedTokens.clear()
    allSelectedTokens.clear()
  }
}

object NewGraphSelectionView {
  def apply(serviceFactory: ServiceFactory): NewGraphSelectionView = new NewGraphSelectionView(serviceFactory)
}
