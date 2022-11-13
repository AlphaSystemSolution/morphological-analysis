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
  override val id: String = UUID.randomUUID().toString,
  chapterNumber: Int,
  chapterName: String,
  verseNumber: Int,
  startTokenNumber: Int,
  endTokenNumber: Int,
  text: String,
  metaInfo: GraphMetaInfo)
    extends AbstractDocument

case class GraphMetaInfo(
  override val id: String = UUID.randomUUID().toString,
  width: Double = 900.0,
  height: Double = 600.0,
  tokenWidth: Double = 80.0,
  tokenHeight: Double = 100.0,
  gapBetweenTokens: Double = 80.0,
  showGridLines: Boolean = false,
  showOutLines: Boolean = false,
  debugMode: Boolean = false,
  backgroundColor: String = "#F5F5DC",
  terminalFont: FontMetaInfo =
    FontMetaInfo(id = "", family = "Arial", weight = "NORMAL", posture = "REGULAR", size = 14.0),
  partOfSpeechFont: FontMetaInfo =
    FontMetaInfo(id = "", family = "Arial", weight = "NORMAL", posture = "REGULAR", size = 14.0),
  translationFont: FontMetaInfo =
    FontMetaInfo(id = "", family = "Arial", weight = "NORMAL", posture = "REGULAR", size = 14.0))
    extends AbstractSimpleDocument

case class FontMetaInfo(
  override val id: String,
  family: String,
  weight: String,
  posture: String,
  size: Double)
    extends AbstractSimpleDocument

sealed trait GraphNode extends AbstractDocument {
  val graphNodeType: GraphNodeType
  val dependencyGraphId: String
  val chapterNumber: Int
  val verseNumber: Int
  val tokenNumber: Int
  val version: Int
  val text: String
  val x: Double
  val y: Double
  val translateX: Double
  val translateY: Double
  val font: FontMetaInfo
}

sealed trait LineSupport extends GraphNode {
  val x1: Double
  val y1: Double
  val x2: Double
  val y2: Double
}

sealed trait LinkSupport extends LineSupport {
  val cx: Double
  val cy: Double
  val linkId: String
}

sealed trait TerminalNodeSupport extends LineSupport {
  val translationText: String
  val translationX: Double
  val translationY: Double
  val tokenId: String
  val translationFont: FontMetaInfo
}

case class PartOfSpeechNode(
  override val id: String = UUID.randomUUID().toString,
  override val dependencyGraphId: String,
  override val chapterNumber: Int,
  override val verseNumber: Int,
  override val tokenNumber: Int,
  override val version: Int,
  override val text: String,
  override val x: Double,
  override val y: Double,
  override val translateX: Double,
  override val translateY: Double,
  override val x1: Double,
  override val y1: Double,
  override val x2: Double,
  override val y2: Double,
  override val cx: Double,
  override val cy: Double,
  override val font: FontMetaInfo,
  override val linkId: String,
  hidden: Boolean)
    extends LinkSupport {
  override val graphNodeType: GraphNodeType = GraphNodeType.PartOfSpeech
}

case class TerminalNode(
  override val id: String = UUID.randomUUID().toString,
  override val graphNodeType: GraphNodeType = GraphNodeType.Terminal,
  override val dependencyGraphId: String,
  override val chapterNumber: Int,
  override val verseNumber: Int,
  override val tokenNumber: Int,
  override val version: Int,
  override val text: String,
  override val x: Double,
  override val y: Double,
  override val translateX: Double,
  override val translateY: Double,
  override val x1: Double,
  override val y1: Double,
  override val x2: Double,
  override val y2: Double,
  override val translationText: String,
  override val translationX: Double,
  override val translationY: Double,
  override val tokenId: String,
  override val font: FontMetaInfo,
  override val translationFont: FontMetaInfo)
    extends TerminalNodeSupport

case class PhraseNode(
  override val id: String = UUID.randomUUID().toString,
  override val dependencyGraphId: String,
  override val chapterNumber: Int,
  override val verseNumber: Int,
  override val tokenNumber: Int,
  override val version: Int,
  override val text: String,
  override val x: Double,
  override val y: Double,
  override val x1: Double,
  override val y1: Double,
  override val x2: Double,
  override val y2: Double,
  override val cx: Double,
  override val cy: Double,
  override val translateX: Double,
  override val translateY: Double,
  override val linkId: String,
  override val font: FontMetaInfo)
    extends LinkSupport {
  override val graphNodeType: GraphNodeType = GraphNodeType.Phrase
}

case class RelationshipNode(
  override val id: String = UUID.randomUUID().toString,
  override val dependencyGraphId: String,
  relationshipType: RelationshipType,
  override val chapterNumber: Int,
  override val verseNumber: Int,
  override val tokenNumber: Int,
  override val version: Int,
  override val text: String,
  override val x: Double,
  override val y: Double,
  controlX1: Double,
  controlY1: Double,
  controlX2: Double,
  controlY2: Double,
  t1: Double,
  t2: Double,
  override val translateX: Double,
  override val translateY: Double,
  dependentId: String,
  ownerId: String,
  override val font: FontMetaInfo)
    extends GraphNode {
  override val graphNodeType: GraphNodeType = GraphNodeType.Relationship
}

case class RootNode(
  override val id: String = UUID.randomUUID().toString,
  override val dependencyGraphId: String,
  override val chapterNumber: Int,
  override val verseNumber: Int,
  override val tokenNumber: Int,
  override val version: Int,
  override val text: String,
  override val x: Double,
  override val y: Double,
  override val translateX: Double,
  override val translateY: Double,
  override val font: FontMetaInfo,
  childNodeType: GraphNodeType)
    extends GraphNode {
  override val graphNodeType: GraphNodeType = GraphNodeType.Root
}
