package com.alphasystem
package arabic
package morphologicalanalysis
package morphology

import com.alphasystem.arabic.morphologicalanalysis.graph.model.GraphNodeType
import morphology.model.{ PartOfSpeechType, ParticlePartOfSpeechType }

import java.nio.file.{ Files, Path, Paths }
import scala.annotation.targetName

package object utils {

  val TerminalNodeTypes: Seq[GraphNodeType] =
    Seq(GraphNodeType.Terminal, GraphNodeType.Hidden, GraphNodeType.Implied, GraphNodeType.Reference)

  val DerivedTerminalNodeTypes: Seq[GraphNodeType] =
    Seq(GraphNodeType.Hidden, GraphNodeType.Implied, GraphNodeType.Reference)

  def isTerminalNode(graphNodeType: GraphNodeType): Boolean = TerminalNodeTypes.contains(graphNodeType)

  def isPhraseNode(graphNodeType: GraphNodeType): Boolean = GraphNodeType.Phrase == graphNodeType

  def isRelationshipNode(graphNodeType: GraphNodeType): Boolean = GraphNodeType.Relationship == graphNodeType

  extension (src: String) {
    def toPath: Path = Paths.get(src)
  }

  extension (src: Path) {
    @targetName("appendAsDirectory")
    def +(others: Seq[String]) = {
      val path = Paths.get(src.toString, others*)
      createDirectories(path)
      path
    }

    @targetName("appendAsFile")
    def ->(others: String*): Path = Paths.get(src.toString, others*)
  }

  private def createDirectories(path: Path): Unit = if Files.notExists(path) then Files.createDirectories(path)
}
