package com.alphasystem
package arabic
package morphologicalanalysis
package ui
package commons
package control

import model.ArabicLabel
import morphology.model.{ Chapter, Token }
import morphology.persistence.cache.TokenRequest
import javafx.application.Platform
import service.ServiceFactory
import javafx.scene.control.{ Control, Skin }
import scalafx.beans.property.ObjectProperty
import scalafx.collections.ObservableBuffer

abstract class VerseSelectionView(override protected val serviceFactory: ServiceFactory, singleSelect: Boolean = true)
    extends Control
    with ChapterVersesLoader {

  val tokensProperty: ObservableBuffer[ArabicLabel[Token]] = ObservableBuffer[ArabicLabel[Token]]()

  val selectedTokenProperty: ObjectProperty[ArabicLabel[Token]] =
    ObjectProperty[ArabicLabel[Token]](this, "selectedToken")

  val selectedTokens: ObservableBuffer[ArabicLabel[Token]] = ObservableBuffer[ArabicLabel[Token]]()

  private val tokenServiceF = serviceFactory.tokenService

  bindVersesOnSelectedChapter

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

  def tokens: Seq[ArabicLabel[Token]] = tokensProperty.toSeq

  def selectedToken: ArabicLabel[Token] = selectedTokenProperty.value
  def selectedToken_=(value: ArabicLabel[Token]): Unit = selectedTokenProperty.value = value

  override def getUserAgentStylesheet: String = Thread
    .currentThread()
    .getContextClassLoader
    .getResource("application.css")
    .toExternalForm

  private def loadTokens(chapterNumber: Int, verseNumber: Int): Unit = {
    val tokenService = tokenServiceF(TokenRequest(chapterNumber, verseNumber))
    Platform.runLater(() => {
      tokenService.onSucceeded = event => {
        selectedToken = null
        val result = event.getSource.getValue.asInstanceOf[Seq[Token]]
        val tokens = result.map(_.toArabicLabel)
        tokensProperty.clear()
        tokensProperty.addAll(tokens)
        if tokens.nonEmpty && singleSelect then selectedToken = tokens.head
        event.consume()
      }
      tokenService.start()
    })
  }
}
