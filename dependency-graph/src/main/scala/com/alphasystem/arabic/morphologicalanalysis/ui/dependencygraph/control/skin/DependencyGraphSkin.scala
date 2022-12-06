package com.alphasystem
package arabic
package morphologicalanalysis
package ui
package dependencygraph
package control
package skin

import com.alphasystem.arabic.morphologicalanalysis.morphology.graph.model.*
import javafx.scene.control.SkinBase
import scalafx.Includes.*
import scalafx.scene.control.{ Accordion, SplitPane }
import scalafx.collections.ObservableBuffer
import scalafx.event.subscriptions.Subscription
import scalafx.scene.Node
import scalafx.scene.layout.BorderPane

class DependencyGraphSkin(control: DependencyGraphView) extends SkinBase[DependencyGraphView](control) {

  private lazy val terminalNodeView = TerminalNodeView()
  private lazy val partOfSpeechNodeView = PartOfSpeechNodeView()
  private lazy val phraseNodeView = PhraseNodeView()
  private lazy val relationshipNodeView = RelationshipNodeView()
  private lazy val propertiesEditorView = {
    val titledPane = createTitledPane("Terminal Node Properties:", terminalNodeView)
    titledPane.disable = true
    titledPane
  }
  private var subscription: Subscription =
    terminalNodeView.sourceProperty.onChange((_, _, nv) => control.selectedNode = nv)

  getChildren.add(initializeSkin)

  private def initializeSkin = {
    control
      .selectedNodeProperty
      .onChange((_, _, nv) => {
        val (text, content) =
          if Option(nv).isDefined then {
            nv match
              case n: PartOfSpeechNode =>
                subscription.cancel()
                subscription = partOfSpeechNodeView
                  .sourceProperty
                  .onChange((_, _, nv) =>
                    if Option(nv).isDefined then {
                      control.selectedNode = nv
                    }
                  )

                partOfSpeechNodeView.source = n
                ("PartOfSpeech Node Properties:", partOfSpeechNodeView)

              case n: PhraseNode =>
                subscription.cancel()
                subscription = phraseNodeView
                  .sourceProperty
                  .onChange((_, _, nv) =>
                    if Option(nv).isDefined then {
                      control.selectedNode = nv
                    }
                  )
                phraseNodeView.source = n
                ("Phrase Node Properties:", phraseNodeView)

              case n: TerminalNode =>
                subscription.cancel()
                subscription = terminalNodeView
                  .sourceProperty
                  .onChange((_, _, nv) =>
                    if Option(nv).isDefined then {
                      control.selectedNode = nv
                    }
                  )
                terminalNodeView.source = n
                ("Terminal Node Properties:", terminalNodeView)

              case n: RelationshipNode =>
                subscription.cancel()
                subscription = relationshipNodeView
                  .sourceProperty
                  .onChange((_, _, nv) =>
                    if Option(nv).isDefined then {
                      control.selectedNode = nv
                    }
                  )
                relationshipNodeView.source = n
                ("Relationship Node Properties:", relationshipNodeView)

          } else {
            terminalNodeView.source = defaultTerminalNodeMetaInfo
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
