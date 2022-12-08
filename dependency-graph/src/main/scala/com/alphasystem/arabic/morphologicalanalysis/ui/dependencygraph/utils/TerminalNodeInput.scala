package com.alphasystem
package arabic
package morphologicalanalysis
package ui
package dependencygraph
package utils

import morphologicalanalysis.graph.model.GraphNodeType
import morphologicalanalysis.morphology.model.Token

import java.util.UUID

case class TerminalNodeInput(
  id: UUID = UUID.randomUUID(),
  graphNodeType: GraphNodeType = GraphNodeType.Terminal,
  token: Token)
