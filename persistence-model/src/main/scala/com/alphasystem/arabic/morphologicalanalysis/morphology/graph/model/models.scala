package com.alphasystem.arabic.morphologicalanalysis.morphology.graph.model

import com.alphasystem.arabic.morphologicalanalysis.morphology.model.*
import com.alphasystem.morphologicalanalysis.graph.model.GraphNodeType
import com.alphasystem.morphologicalanalysis.morphology.model.{
  PartOfSpeechType,
  RelationshipType
}

case class DependencyGraph(
  override val id: String,
  chapterNumber: Int,
  metaInfo: GraphMetaInfo
  /*nodes: Seq[GraphNode]*/)
    extends AbstractDocument

case class GraphMetaInfo(
  override val id: String,
  width: Double,
  height: Double,
  tokenWidth: Double,
  tokenHeight: Double,
  gapBetweenTokens: Double,
  showGridLines: Boolean,
  showOutLines: Boolean,
  debugMode: Boolean,
  backgroundColor: String,
  terminalFont: FontMetaInfo,
  partOfSpeechFont: FontMetaInfo,
  translationFont: FontMetaInfo)
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
  val chapterNumber: Int
  val verseNumber: Int
  val tokenNumber: Int
  val version: Int
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
  val translationX: Double
  val translationY: Double
  val tokenId: String
  val translationFont: FontMetaInfo
}

case class PartOfSpeechNode(
  override val id: String,
  override val chapterNumber: Int,
  override val verseNumber: Int,
  override val tokenNumber: Int,
  override val version: Int,
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
  override val graphNodeType: GraphNodeType = GraphNodeType.PART_OF_SPEECH
}

case class HiddenNode(
  override val id: String,
  override val chapterNumber: Int,
  override val verseNumber: Int,
  override val tokenNumber: Int,
  override val version: Int,
  override val x: Double,
  override val y: Double,
  override val translateX: Double,
  override val translateY: Double,
  override val x1: Double,
  override val y1: Double,
  override val x2: Double,
  override val y2: Double,
  override val translationX: Double,
  override val translationY: Double,
  override val tokenId: String,
  override val font: FontMetaInfo,
  override val translationFont: FontMetaInfo)
    extends TerminalNodeSupport {
  override val graphNodeType: GraphNodeType = GraphNodeType.HIDDEN
}

case class TerminalNode(
  override val id: String,
  override val chapterNumber: Int,
  override val verseNumber: Int,
  override val tokenNumber: Int,
  override val version: Int,
  override val x: Double,
  override val y: Double,
  override val translateX: Double,
  override val translateY: Double,
  override val x1: Double,
  override val y1: Double,
  override val x2: Double,
  override val y2: Double,
  override val translationX: Double,
  override val translationY: Double,
  override val tokenId: String,
  override val font: FontMetaInfo,
  override val translationFont: FontMetaInfo
  /*partOfSpeechNodes: Seq[PartOfSpeechNode]*/)
    extends TerminalNodeSupport {
  override val graphNodeType: GraphNodeType = GraphNodeType.TERMINAL
}

case class PhraseNode(
  override val id: String,
  override val chapterNumber: Int,
  override val verseNumber: Int,
  override val tokenNumber: Int,
  override val version: Int,
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
  override val graphNodeType: GraphNodeType = GraphNodeType.PHRASE
}

case class ReferenceNode(
  override val id: String,
  override val chapterNumber: Int,
  override val verseNumber: Int,
  override val tokenNumber: Int,
  override val version: Int,
  override val x: Double,
  override val y: Double,
  override val translateX: Double,
  override val translateY: Double,
  override val x1: Double,
  override val y1: Double,
  override val x2: Double,
  override val y2: Double,
  override val translationX: Double,
  override val translationY: Double,
  override val tokenId: String,
  override val font: FontMetaInfo,
  override val translationFont: FontMetaInfo)
    extends TerminalNodeSupport {
  override val graphNodeType: GraphNodeType = GraphNodeType.REFERENCE
}

case class RelationshipNode(
  override val id: String,
  relationshipType: RelationshipType,
  override val chapterNumber: Int,
  override val verseNumber: Int,
  override val tokenNumber: Int,
  override val version: Int,
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
  override val graphNodeType: GraphNodeType = GraphNodeType.RELATIONSHIP
}

case class RootNode(
  override val id: String,
  override val chapterNumber: Int,
  override val verseNumber: Int,
  override val tokenNumber: Int,
  override val version: Int,
  override val x: Double,
  override val y: Double,
  override val translateX: Double,
  override val translateY: Double,
  override val font: FontMetaInfo,
  childNodeType: GraphNodeType)
    extends GraphNode {
  override val graphNodeType: GraphNodeType = GraphNodeType.ROOT
}
