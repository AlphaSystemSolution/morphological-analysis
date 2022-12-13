package com.alphasystem
package arabic
package morphologicalanalysis
package morphology
package graph
package model

import morphology.utils.*
import morphology.model.*
import morphologicalanalysis.graph.model.GraphNodeType
import morphology.model.{ PartOfSpeechType, RelationshipType }

import java.util.UUID

case class DependencyGraph(
  id: UUID = UUID.randomUUID(),
  chapterNumber: Int,
  chapterName: String,
  metaInfo: GraphMetaInfo,
  verseNumbers: Seq[Int],
  tokens: Seq[Token],
  nodes: Seq[GraphNode]) {
  val text: String = tokens.map(_.token).mkString(" ")
}

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

case class PhraseInfo(
  id: UUID = UUID.randomUUID(),
  text: String,
  phraseTypes: Seq[PhraseType],
  locations: Seq[Long],
  status: Option[NounStatus] = None)
    extends Linkable {
  val graphNodeType: GraphNodeType = GraphNodeType.Phrase
}

case class RelationshipLink(id: UUID, graphNodeType: GraphNodeType)

case class RelationshipInfo(
  id: UUID = UUID.randomUUID(),
  text: String,
  relationshipType: RelationshipType,
  owner: RelationshipLink,
  dependent: RelationshipLink) {
  val graphNodeType: GraphNodeType = GraphNodeType.Relationship
}

case class Point(x: Double, y: Double)
case class Line(p1: Point, p2: Point)

sealed trait GraphNode {
  val id: UUID
  val dependencyGraphId: UUID
  val text: String
  val textPoint: Point
  val translate: Point
  val font: FontMetaInfo
  val graphNodeType: GraphNodeType
}

sealed trait LineSupport extends GraphNode {
  val line: Line
}

sealed trait LinkSupport extends LineSupport {
  val circle: Point
}

case class TerminalNode(
  override val id: UUID = UUID.randomUUID(),
  override val dependencyGraphId: UUID,
  override val graphNodeType: GraphNodeType,
  override val textPoint: Point,
  override val translate: Point,
  override val line: Line,
  translationPoint: Point,
  override val font: FontMetaInfo,
  translationFont: FontMetaInfo,
  token: Token,
  index: Int = 0,
  partOfSpeechNodes: Seq[PartOfSpeechNode])
    extends LineSupport {
  override val text: String = token.token
  val translationText: String = token.translation.getOrElse("")
}

case class PartOfSpeechNode(
  override val id: UUID = UUID.randomUUID(),
  override val dependencyGraphId: UUID,
  override val textPoint: Point,
  override val translate: Point,
  override val circle: Point,
  override val font: FontMetaInfo,
  location: Location)
    extends LinkSupport {
  override val graphNodeType: GraphNodeType = GraphNodeType.PartOfSpeech
  override val text: String = location.properties.toText
  override val line: Line = Line(Point(0, 0), Point(0, 0))
  val partOfSpeechType: PartOfSpeechType = location.partOfSpeechType
  val hidden: Boolean = HiddenPartOfSpeeches.contains(partOfSpeechType)
}

case class PhraseNode(
  override val id: UUID = UUID.randomUUID(),
  override val dependencyGraphId: UUID,
  override val textPoint: Point,
  override val translate: Point,
  override val line: Line,
  override val circle: Point,
  phraseInfo: PhraseInfo,
  override val font: FontMetaInfo)
    extends LinkSupport {
  override val graphNodeType: GraphNodeType = phraseInfo.graphNodeType
  override val text: String = phraseInfo.text
}

case class RelationshipNode(
  override val id: UUID = UUID.randomUUID(),
  override val dependencyGraphId: UUID,
  override val textPoint: Point,
  override val translate: Point,
  control1: Point,
  control2: Point,
  arrow: Point,
  override val font: FontMetaInfo,
  relationshipInfo: RelationshipInfo)
    extends GraphNode {
  override val graphNodeType: GraphNodeType = relationshipInfo.graphNodeType
  override val text: String = relationshipInfo.text
}
