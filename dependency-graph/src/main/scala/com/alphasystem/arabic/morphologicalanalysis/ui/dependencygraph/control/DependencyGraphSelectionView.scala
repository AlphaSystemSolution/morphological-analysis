package com.alphasystem
package arabic
package morphologicalanalysis
package ui
package dependencygraph
package control

import model.ArabicLabel
import com.alphasystem.arabic.morphologicalanalysis.ui.commons.control.ChapterVersesLoader
import morphology.persistence.cache.GetDependencyGraphRequest
import skin.DependencyGraphSelectionSkin
import commons.service.ServiceFactory
import javafx.application.Platform
import morphology.graph.model.DependencyGraph
import javafx.scene.control.{ Control, Skin }
import scalafx.Includes.*
import scalafx.beans.property.{ ReadOnlyObjectProperty, ReadOnlyObjectWrapper }
import scalafx.collections.ObservableBuffer

class DependencyGraphSelectionView(override protected val serviceFactory: ServiceFactory)
    extends Control
    with ChapterVersesLoader {

  private[control] val dependencyGraphs: ObservableBuffer[ArabicLabel[DependencyGraph]] =
    ObservableBuffer.empty[ArabicLabel[DependencyGraph]]

  private val selectedGraphWrapperProperty =
    new ReadOnlyObjectWrapper[ArabicLabel[DependencyGraph]](this, "selectedGraph")

  bindVersesOnSelectedChapter
  loadChapters()
  selectedVerseProperty.onChange((_, _, nv) => {
    if Option(selectedChapter).isDefined && Option(nv).isDefined then {
      val chapterNumber = selectedChapter.userData.chapterNumber
      val verseNumber = nv.userData
      loadGraphs(chapterNumber, verseNumber)
    } else selectedGraphWrapperProperty.value = null
  })
  setSkin(createDefaultSkin())

  def selectedGraph: ArabicLabel[DependencyGraph] = selectedGraphProperty.value
  private[control] def selectedGraph_=(value: ArabicLabel[DependencyGraph]): Unit = selectedGraphWrapperProperty.value =
    value
  def selectedGraphProperty: ReadOnlyObjectProperty[ArabicLabel[DependencyGraph]] =
    selectedGraphWrapperProperty.readOnlyProperty

  override def createDefaultSkin(): Skin[?] = DependencyGraphSelectionSkin(this)

  private def loadGraphs(chapterNumber: Int, verseNumber: Int): Unit = {
    val service = serviceFactory.getDependencyGraphByChapterAndVerseNumberService(
      GetDependencyGraphRequest(
        chapterNumber = chapterNumber,
        verseNumber = verseNumber
      )
    )

    service.onSucceeded = event => {
      dependencyGraphs.clear()
      val graphs = event.getSource.getValue.asInstanceOf[Seq[DependencyGraph]].map(_.toArabicLabel)
      dependencyGraphs.addAll(graphs*)
      selectedGraphWrapperProperty.value = graphs.headOption.orNull
      event.consume()
    }

    service.onFailed = event => {
      Console.err.println(s"Unable to load dependency graph for chapter: $chapterNumber, verse: $verseNumber")
      event.getSource.getException.printStackTrace()
      event.consume()
    }

    Platform.runLater(() => service.start())
  }
}

object DependencyGraphSelectionView {
  def apply(serviceFactory: ServiceFactory): DependencyGraphSelectionView =
    new DependencyGraphSelectionView(serviceFactory)
}
