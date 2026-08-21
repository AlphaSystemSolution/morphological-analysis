package com.alphasystem
package arabic
package morphologicalanalysis
package morphology

import com.alphasystem.arabic.morphologicalanalysis.graph.model.GraphNodeType

package object utils {

  val TerminalNodeTypes: Seq[GraphNodeType] =
    Seq(GraphNodeType.Terminal, GraphNodeType.Hidden, GraphNodeType.Implied, GraphNodeType.Reference)

  val DerivedTerminalNodeTypes: Seq[GraphNodeType] =
    Seq(GraphNodeType.Hidden, GraphNodeType.Implied, GraphNodeType.Reference)

  def isTerminalNode(graphNodeType: GraphNodeType): Boolean = TerminalNodeTypes.contains(graphNodeType)

  def isPhraseNode(graphNodeType: GraphNodeType): Boolean = GraphNodeType.Phrase == graphNodeType

  def isRelationshipNode(graphNodeType: GraphNodeType): Boolean = GraphNodeType.Relationship == graphNodeType
}
