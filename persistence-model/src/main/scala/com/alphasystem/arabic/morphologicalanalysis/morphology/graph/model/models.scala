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
  verseNumber: Int,
  chapterName: String,
  metaInfo: GraphMetaInfo,
  tokens: Seq[Token],
  nodes: Seq[GraphNodeMetaInfo]) {
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
  relationshipType: RelationshipType,
  locations: Seq[Location])
    extends Linkable {
  val graphNodeType: GraphNodeType = GraphNodeType.Phrase
}

case class RelationshipInfo[Owner <: Linkable, Dependent <: Linkable](
  id: UUID = UUID.randomUUID(),
  text: String,
  relationshipType: RelationshipType,
  owner: Owner,
  dependent: Dependent) {
  val graphNodeType: GraphNodeType = GraphNodeType.Relationship
}

case class Point(x: Double, y: Double)
case class Line(p1: Point, p2: Point)

sealed trait GraphNodeMetaInfo {
  val id: UUID
  val dependencyGraphId: UUID
  val text: String
  val textPoint: Point
  val translate: Point
  val font: FontMetaInfo
  val graphNodeType: GraphNodeType
}

sealed trait LineSupport extends GraphNodeMetaInfo {
  val line: Line
}

sealed trait LinkSupport extends LineSupport {
  val circle: Point
}

case class TerminalNodeMetaInfo(
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
  partOfSpeechNodes: Seq[PartOfSpeechNodeMetaInfo])
    extends LineSupport {
  override val text: String = token.token
  val translationText: String = token.translation.getOrElse("")
}

case class PartOfSpeechNodeMetaInfo(
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
  val hidden: Boolean = location.hidden
  val partOfSpeechType: PartOfSpeechType = location.partOfSpeechType
}

case class PhraseNodeMetaInfo(
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

case class RelationshipNodeMetaInfo(
  override val id: UUID = UUID.randomUUID(),
  override val dependencyGraphId: UUID,
  override val textPoint: Point,
  override val translate: Point,
  control1: Point,
  control2: Point,
  t: Point,
  override val font: FontMetaInfo,
  relationshipInfo: RelationshipInfo[Linkable, Linkable])
    extends GraphNodeMetaInfo {
  override val graphNodeType: GraphNodeType = relationshipInfo.graphNodeType
  override val text: String = relationshipInfo.text
}
