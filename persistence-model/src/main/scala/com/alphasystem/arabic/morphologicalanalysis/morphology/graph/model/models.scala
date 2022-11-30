package com.alphasystem
package arabic
package morphologicalanalysis
package morphology
package graph
package model

import morphology.model.*
import morphologicalanalysis.graph.model.GraphNodeType
import morphology.model.{ PartOfSpeechType, RelationshipType }

import java.util.UUID

case class DependencyGraph(
  id: UUID = UUID.randomUUID(),
  chapterNumber: Int,
  chapterName: String,
  text: String,
  metaInfo: GraphMetaInfo,
  verseTokensMap: Map[Int, Seq[Int]]) {}

case class GraphMetaInfo(
  width: Double = 900.0,
  height: Double = 600.0,
  tokenWidth: Double = 80.0,
  tokenHeight: Double = 100.0,
  gapBetweenTokens: Double = 80.0,
  showGridLines: Boolean = false,
  showOutLines: Boolean = false,
  debugMode: Boolean = false,
  backgroundColor: String = "#F5F5DC",
  terminalFont: FontMetaInfo = FontMetaInfo(family = "Arial", weight = "NORMAL", posture = "REGULAR", size = 14.0),
  partOfSpeechFont: FontMetaInfo = FontMetaInfo(family = "Arial", weight = "NORMAL", posture = "REGULAR", size = 14.0),
  translationFont: FontMetaInfo = FontMetaInfo(family = "Arial", weight = "NORMAL", posture = "REGULAR", size = 14.0))

case class FontMetaInfo(family: String, weight: String, posture: String, size: Double)

sealed trait GraphNode {
  val id: UUID
  val graphNodeType: GraphNodeType
  val text: String
}

case class TerminalNode(
  override val id: UUID = UUID.randomUUID(),
  override val graphNodeType: GraphNodeType,
  token: Token,
  partOfSpeechNodes: Seq[PartOfSpeechNode])
    extends GraphNode {
  override val text: String = token.token
  val translation: String = token.translation.getOrElse("")
}

object TerminalNode {

  def apply(token: Token, graphNodeType: GraphNodeType): TerminalNode = {
    val partOfSpeechNodes = token.locations.map(location => PartOfSpeechNode(location = location))
    TerminalNode(graphNodeType = graphNodeType, token = token, partOfSpeechNodes = partOfSpeechNodes)
  }
  def createTerminalNode(token: Token): TerminalNode =
    TerminalNode(graphNodeType = GraphNodeType.Terminal, token = token)

  def createHiddenNode(token: Token): TerminalNode = TerminalNode(graphNodeType = GraphNodeType.Hidden, token = token)
}

case class PartOfSpeechNode(
  override val id: UUID = UUID.randomUUID(),
  location: Location,
  hidden: Boolean = false)
    extends GraphNode {
  override val graphNodeType: GraphNodeType = GraphNodeType.PartOfSpeech
  override val text: String = location.properties.toText
  val partOfSpeechType: PartOfSpeechType = location.partOfSpeechType
}

case class Point(x: Double, y: Double)
case class Line(p1: Point, p2: Point)

case class GraphNodeWrapper[Node <: GraphNode, MetaInfo <: GraphNodeMetaInfo](
  id: UUID = UUID.randomUUID(),
  dependencyGraphId: UUID,
  node: Node,
  nodeMetaInfo: MetaInfo)

sealed trait GraphNodeMetaInfo {
  val id: UUID
  val text: Point
  val translate: Point
  val font: FontMetaInfo
}

sealed trait LineSupport extends GraphNodeMetaInfo {
  val line: Line
}

sealed trait LinkSupport extends GraphNodeMetaInfo {
  val circle: Point
}

case class TerminalNodeMetaInfo(
  override val id: UUID = UUID.randomUUID(),
  override val text: Point,
  override val translate: Point,
  override val line: Line,
  override val font: FontMetaInfo,
  translationFont: FontMetaInfo,
  partOfSpeechNodes: Seq[PartOfSpeechNodeMetaInfo])
    extends LineSupport

case class PartOfSpeechNodeMetaInfo(
  override val id: UUID = UUID.randomUUID(),
  override val text: Point,
  override val translate: Point,
  override val circle: Point,
  override val font: FontMetaInfo)
    extends LinkSupport
