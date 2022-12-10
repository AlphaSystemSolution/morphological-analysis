package com.alphasystem
package arabic
package morphologicalanalysis
package ui
package dependencygraph
package utils

import morphologicalanalysis.morphology.graph.model.DependencyGraph
import morphologicalanalysis.graph.model.GraphNodeType
import morphologicalanalysis.morphology.model.Token

import java.util.UUID

sealed trait GraphOperationRequest

case class AddNodeRequest(dependencyGraph: DependencyGraph, inputs: Seq[TerminalNodeInput])
    extends GraphOperationRequest

case class TerminalNodeInput(
  id: UUID = UUID.randomUUID(),
  graphNodeType: GraphNodeType = GraphNodeType.Terminal,
  token: Token)
