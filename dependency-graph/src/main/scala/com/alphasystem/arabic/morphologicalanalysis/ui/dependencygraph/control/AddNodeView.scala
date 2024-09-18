package com.alphasystem
package arabic
package morphologicalanalysis
package ui
package dependencygraph
package control

import com.alphasystem.arabic.model.ArabicLabel
import com.alphasystem.arabic.morphologicalanalysis.graph.model.GraphNodeType
import com.alphasystem.arabic.morphologicalanalysis.morphology.model.{ Chapter, Token, WordType, defaultToken }
import com.alphasystem.arabic.morphologicalanalysis.morphology.persistence.cache.TokenRequest
import javafx.application.Platform
import ui.commons.service.ServiceFactory
import skin.AddNodeSkin
import javafx.scene.control.{ Control, Skin }
import scalafx.beans.property.{ BooleanProperty, ObjectProperty, ReadOnlyObjectWrapper }
import scalafx.collections.ObservableBuffer

class AddNodeView(serviceFactory: ServiceFactory) extends Control {

  import AddNodeView.*

  private val tokenServiceF = serviceFactory.tokenService
  private[control] val showReferenceTypeProperty: BooleanProperty = BooleanProperty(true)
  private[control] val selectedTypeProperty =
    new ObjectProperty[GraphNodeType](this, "selectedType", GraphNodeType.Implied)
  private[control] val wordTypeProperty = new ObjectProperty[WordType](this, "selectedWordType", WordType.NOUN)
  private[control] val currentChapterProperty = new ObjectProperty[Chapter](this, "currentChapter")
  private[control] val versesProperty = ObservableBuffer.empty[ArabicLabel[Int]]
  private[control] val selectedVerseProperty: ReadOnlyObjectWrapper[ArabicLabel[Int]] =
    new ReadOnlyObjectWrapper[ArabicLabel[Int]](this, "selectedVerse")
  private[control] val selectedTokenProperty: ObjectProperty[ArabicLabel[Token]] =
    ObjectProperty[ArabicLabel[Token]](this, "selectedToken", defaultToken.toArabicLabel)
  private[control] val tokensProperty = ObservableBuffer.empty[ArabicLabel[Token]]

  currentChapterProperty.onChange((_, _, nv) => {
    versesProperty.clear()
    if Option(nv).isDefined then {
      val verses = (1 to nv.verseCount).map(i => ArabicLabel(i, i.toString, ""))
      versesProperty.addAll(verses*)
    }
  })

  selectedVerseProperty.onChange((_, _, nv) => {
    if Option(nv).isDefined then {
      val chapterNumber = currentChapter.chapterNumber
      val verseNumber = nv.userData
      loadTokens(chapterNumber, verseNumber)
    } else {
      selectedToken = null
    }
  })

  setSkin(createDefaultSkin())

  def showReferenceType: Boolean = showReferenceTypeProperty.value
  def showReferenceType_=(value: Boolean): Unit = showReferenceTypeProperty.value = value

  def selectedType: GraphNodeType = selectedTypeProperty.value
  def selectedType_=(value: GraphNodeType): Unit = selectedTypeProperty.value = value

  def wordType: WordType = wordTypeProperty.value
  def wordType_=(value: WordType): Unit = wordTypeProperty.value = value

  def currentChapter: Chapter = currentChapterProperty.value
  def currentChapter_=(value: Chapter): Unit = currentChapterProperty.value = value

  def selectedVerse: ArabicLabel[Int] = selectedVerseProperty.value
  def selectedVerse_=(value: ArabicLabel[Int]): Unit = selectedVerseProperty.value = value

  def selectedToken: ArabicLabel[Token] = selectedTokenProperty.value
  def selectedToken_=(value: ArabicLabel[Token]): Unit = selectedTokenProperty.value = value

  override def createDefaultSkin(): Skin[?] = new AddNodeSkin(this)

  private[control] def loadTokens(chapterNumber: Int, verseNumber: Int): Unit = {
    val tokenService = tokenServiceF(TokenRequest(chapterNumber, verseNumber))

    tokenService.onSucceeded = event => {
      selectedToken = null
      val result = event.getSource.getValue.asInstanceOf[Seq[Token]]
      val tokens = result.map(_.toArabicLabel)
      tokensProperty.clear()
      tokensProperty.addAll(tokens)
      if tokens.nonEmpty then selectedToken = tokens.head
      event.consume()
    }

    tokenService.onFailed = event => {
      event.getSource.getException.printStackTrace()
      event.consume()
    }

    Platform.runLater(() => tokenService.start())
  }
}

object AddNodeView {

  extension (src: GraphNodeType) {
    def toArabicLabel: ArabicLabel[GraphNodeType] = ArabicLabel(src, src.name(), src.name())
  }

  def apply(serviceFactory: ServiceFactory): AddNodeView = new AddNodeView(serviceFactory)
}
