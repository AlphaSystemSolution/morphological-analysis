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

  private lazy val allPanes = Seq(
    createTitledPane("Graph Settings", control.graphSettingsView),
    propertiesEditorView
  )
  private lazy val accordion = new Accordion() {
    panes = allPanes
    expandedPane = allPanes.head
  }

  getChildren.add(initializeSkin)

  private def initializeSkin = {
    control
      .selectedNodeProperty
      .onChange((_, _, nv) => {
        val (text, content, disable) =
          if Option(nv).isDefined then {
            nv match
              case n: PartOfSpeechNode =>
                subscription.cancel()
                subscription = partOfSpeechNodeView
                  .sourceProperty
                  .onChange((_, _, nv) => if Option(nv).isDefined then control.selectedNode = nv)
                partOfSpeechNodeView.source = n
                ("PartOfSpeech Node Properties:", partOfSpeechNodeView, false)

              case n: PhraseNode =>
                subscription.cancel()
                subscription = phraseNodeView
                  .sourceProperty
                  .onChange((_, _, nv) => if Option(nv).isDefined then control.selectedNode = nv)
                phraseNodeView.source = n
                ("Phrase Node Properties:", phraseNodeView, false)

              case n: TerminalNode =>
                subscription.cancel()
                subscription = terminalNodeView
                  .sourceProperty
                  .onChange((_, _, nv) => if Option(nv).isDefined then control.selectedNode = nv)
                terminalNodeView.source = n
                ("Terminal Node Properties:", terminalNodeView, false)

              case n: RelationshipNode =>
                subscription.cancel()
                subscription = relationshipNodeView
                  .sourceProperty
                  .onChange((_, _, nv) => if Option(nv).isDefined then control.selectedNode = nv)
                relationshipNodeView.source = n
                ("Relationship Node Properties:", relationshipNodeView, false)

          } else {
            subscription.cancel()
            terminalNodeView.source = defaultTerminalNode
            accordion.expandedPane = allPanes.head
            ("Properties:", terminalNodeView, true)
          }

        propertiesEditorView.text = text
        propertiesEditorView.content = content
        propertiesEditorView.disable = disable
      })

    val splitPane = new SplitPane() {
      items.addAll(control.canvasView, accordion)
    }
    splitPane.setDividerPosition(0, 0.70)
    new BorderPane() {
      center = splitPane
    }
  }
}

object DependencyGraphSkin {

  def apply(control: DependencyGraphView): DependencyGraphSkin = new DependencyGraphSkin(control)
}
