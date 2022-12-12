package com.alphasystem
package arabic
package morphologicalanalysis
package ui
package dependencygraph
package control

import dependencygraph.utils.TerminalNodeInput
import morphology.graph.model.DependencyGraph
import morphology.model.{ Chapter, RelationshipType, Token }

case class OpenDialogResult(chapter: Option[Chapter] = None, dependencyGraph: Option[DependencyGraph] = None)

case class NewDialogResult(chapter: Option[Chapter] = None, tokens: Seq[Token] = Seq.empty)

case class AddNodeResult(mayNewNode: Option[TerminalNodeInput])

case class CreateRelationshipResult(relationshipType: RelationshipType)
