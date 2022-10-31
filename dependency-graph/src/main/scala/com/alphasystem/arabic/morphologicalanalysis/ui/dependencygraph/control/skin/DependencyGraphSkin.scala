package com.alphasystem
package arabic
package morphologicalanalysis
package ui
package dependencygraph
package control
package skin

import com.alphasystem.arabic.morphologicalanalysis.morphology.graph.model.{
  HiddenNode,
  LineSupport,
  LinkSupport,
  PartOfSpeechNode,
  PhraseNode,
  ReferenceNode,
  RelationshipNode,
  RootNode,
  TerminalNode,
  TerminalNodeSupport
}
import javafx.scene.control.SkinBase
import scalafx.Includes.*
import scalafx.scene.control.{ Accordion, SplitPane }
import scalafx.collections.ObservableBuffer
import scalafx.scene.Node
import scalafx.scene.layout.BorderPane

class DependencyGraphSkin(control: DependencyGraphView) extends SkinBase[DependencyGraphView](control) {

  private lazy val terminalNodeView = TerminalNodeView()
  private lazy val partOfSpeechNodeView = PartOfSpeechNodeView()
  private lazy val phraseNodeView = PhraseNodeView()
  private lazy val hiddenNodeView = HiddenNodeView()
  private lazy val referenceNodeView = ReferenceNodeView()
  private lazy val relationshipNodeView = RelationshipNodeView()
  private lazy val propertiesEditorView = {
    val titledPane = createTitledPane("Terminal Node Properties:", terminalNodeView)
    titledPane.disable = true
    titledPane
  }

  getChildren.add(initializeSkin)

  private def initializeSkin = {
    control
      .selectedNode
      .onChange((_, _, nv) => {
        val (text, content) =
          if Option(nv).isDefined then {
            nv match
              case n: PartOfSpeechNode =>
                partOfSpeechNodeView.source = n
                ("PartOfSpeech Node Properties:", partOfSpeechNodeView)

              case n: PhraseNode =>
                phraseNodeView.source = n
                ("Phrase Node Properties:", phraseNodeView)

              case n: HiddenNode =>
                hiddenNodeView.source = n
                ("Hidden Node Properties:", hiddenNodeView)

              case n: TerminalNode =>
                terminalNodeView.source = n
                ("Terminal Node Properties:", terminalNodeView)

              case n: ReferenceNode =>
                referenceNodeView.source = n
                ("Reference Node Properties:", referenceNodeView)

              case n: RelationshipNode =>
                relationshipNodeView.source = n
                ("Relationship Node Properties:", relationshipNodeView)

              case _: RootNode =>
                terminalNodeView.source = defaultTerminalNode
                ("Properties:", terminalNodeView)
          } else {
            terminalNodeView.source = defaultTerminalNode
            ("Properties:", terminalNodeView)
          }

        propertiesEditorView.text = text
        propertiesEditorView.content = content
        propertiesEditorView.disable = false
      })

    val splitPane = new SplitPane() {
      items.addAll(control.canvasView, initializeSelectionPane)
    }
    splitPane.setDividerPosition(0, 0.70)
    new BorderPane() {
      center = splitPane
    }
  }

  private def initializeSelectionPane = {
    val allPanes = Seq(
      createTitledPane("Verse Selection", control.verseSelectionView),
      createTitledPane("Graph Settings", control.graphSettingsView),
      propertiesEditorView
    )
    new Accordion() {
      panes = allPanes
      expandedPane = allPanes.head
    }
  }
}

object DependencyGraphSkin {

  def apply(control: DependencyGraphView): DependencyGraphSkin = new DependencyGraphSkin(control)
}
