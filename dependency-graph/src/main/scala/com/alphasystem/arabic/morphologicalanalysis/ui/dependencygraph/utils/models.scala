package com.alphasystem
package arabic
package morphologicalanalysis
package ui
package dependencygraph
package utils

import ui.dependencygraph.control.LinkSupportView
import morphologicalanalysis.morphology.graph.model.{
  DependencyGraph,
  GraphNode,
  Line,
  PhraseInfo,
  RelationshipInfo,
  RelationshipLink,
  TerminalNode
}
import morphologicalanalysis.graph.model.GraphNodeType
import morphologicalanalysis.morphology.model.Token

import java.util.UUID

sealed trait GraphOperationRequest

case object NoOp extends GraphOperationRequest

case class AddTerminalNodeRequest(dependencyGraph: DependencyGraph, inputs: Seq[TerminalNodeInput])
    extends GraphOperationRequest

case class RemoveTerminalNodeRequest(
  dependencyGraph: DependencyGraph,
  inputs: Seq[TerminalNodeInput],
  nodes: Seq[GraphNode])
    extends GraphOperationRequest

case class CreateRelationshipRequest(
  dependencyGraph: DependencyGraph,
  relationshipInfo: RelationshipInfo,
  owner: LinkSupportView[?],
  dependent: LinkSupportView[?])
    extends GraphOperationRequest

case class CreatePhraseRequest(dependencyGraph: DependencyGraph, phraseInfo: PhraseInfo, line: Line)
    extends GraphOperationRequest

case class RemoveNodeRequest(dependencyGraph: DependencyGraph, id: UUID) extends GraphOperationRequest

case object SaveGraphRequest extends GraphOperationRequest

case class TerminalNodeInput(
  id: UUID = UUID.randomUUID(),
  graphNodeType: GraphNodeType = GraphNodeType.Terminal,
  token: Token,
  maybeTerminalNode: Option[TerminalNode] = None)
