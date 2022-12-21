package com.alphasystem
package arabic
package morphologicalanalysis
package morphology
package persistence
package nitrite
package collections

import com.alphasystem.arabic.morphologicalanalysis.morphology.model.{ Location, Token }
import morphologicalanalysis.graph.model.GraphNodeType
import morphologicalanalysis.graph.model.GraphNodeType.*
import morphology.graph.model.*
import io.circe.generic.auto.*
import io.circe.parser.*
import io.circe.syntax.*
import org.dizitart.no2.*
import org.dizitart.no2.filters.Filters

import java.util.UUID
import scala.jdk.CollectionConverters.*

class GraphNodeCollection private (db: Nitrite) {

  import GraphNodeCollection.*

  private[persistence] val collection = db.getCollection("graph_node")
  if !collection.hasIndex(DependencyGraphIdField) then {
    collection.createIndex(DependencyGraphIdField, IndexOptions.indexOptions(IndexType.NonUnique))
    collection.createIndex(TokenIdField, IndexOptions.indexOptions(IndexType.NonUnique))
  }

  private[persistence] def upsertNodes(nodes: Seq[GraphNode]): Unit =
    nodes.foreach { node =>
      findNode(node.id) match
        case Some(document) =>
          val updatedDocument =
            node.graphNodeType match
              case Terminal | Hidden | Implied | Reference => node.asInstanceOf[TerminalNode].updateDocument(document)
              case Phrase                                  => node.asInstanceOf[PhraseNode].updateDocument(document)
              case Relationship => node.asInstanceOf[RelationshipNode].updateDocument(document)
              case _            =>
                // don't need to do anything
                document

          collection.update(updatedDocument)

        case None =>
          val maybeDocument =
            node match
              case n: TerminalNode     => Some(n.toDocument)
              case n: PhraseNode       => Some(n.toDocument)
              case n: RelationshipNode => Some(n.toDocument)
              case _                   => None

          maybeDocument.foreach(collection.insert(_))
    }

  private[persistence] def findByDependencyGraphId(dependencyGraphId: UUID): Seq[GraphNode] =
    collection.find(Filters.eq(DependencyGraphIdField, dependencyGraphId.toString)).asScalaList.flatMap { document =>
      GraphNodeType.valueOf(document.getString(NodeTypeField)) match
        case Terminal | Hidden | Implied | Reference => Some(document.toTerminalNode)
        case Phrase                                  => Some(document.toPhraseNode)
        case Relationship                            => Some(document.toRelationshipNode)
        case _                                       => None
    }

  private[persistence] def removeNode(nodeId: UUID): Int =
    collection.remove(Filters.eq(IdField, nodeId.toString)).getAffectedCount

  private[persistence] def removeByDependencyGraphId(dependencyGraphId: UUID): Int =
    collection.remove(Filters.eq(DependencyGraphIdField, dependencyGraphId.toString)).getAffectedCount

  private def findNode(id: UUID) = collection.find(Filters.eq(IdField, id.toString)).asScalaList.headOption

}

object GraphNodeCollection {

  private val ArrowField = "arrow"
  private val Control1Field = "control1"
  private val Control2Field = "control2"
  private val CircleField = "circle"
  private val DependencyGraphIdField = "dependency_graph_id"
  private val FontField = "font"
  private val IdField = "id"
  private val LineField = "line"
  private val LocationField = "location"
  private val LocationIdField = "location_id"
  private val TokenIdField = "token_id"
  private val NodeTypeField = "node_type"
  private val PartOfSpeechNodesField = "part_of_speech_nodes"
  private val PhraseInfoField = "phrase_info"
  private val RelationshipInfoField = "relationship_info"
  private val TextPointField = "text_point"
  private val TokenField = "token"
  private val TranslateField = "translate"
  private val TranslationPointField = "translation_point"
  private val TranslationFontField = "translation_font"

  extension (src: Document) {
    private def toPartOfSpeechNode: PartOfSpeechNode =
      PartOfSpeechNode(
        id = src.getUUID(IdField),
        dependencyGraphId = src.getUUID(DependencyGraphIdField),
        textPoint = src.getString(TextPointField).toPoint,
        translate = src.getString(TranslateField).toPoint,
        circle = src.getString(CircleField).toPoint,
        font = src.getString(FontField).toFont,
        location = src.getDocument(LocationField).toLocation,
        hidden = src.getOptionalBoolean(HiddenField).getOrElse(false)
      )

    private def toTerminalNode: TerminalNode =
      TerminalNode(
        id = src.getUUID(IdField),
        dependencyGraphId = src.getUUID(DependencyGraphIdField),
        graphNodeType = GraphNodeType.valueOf(src.getString(NodeTypeField)),
        textPoint = src.getString(TextPointField).toPoint,
        translate = src.getString(TranslateField).toPoint,
        line = src.getString(LineField).toLine,
        translationPoint = src.getString(TranslationPointField).toPoint,
        font = src.getString(FontField).toFont,
        translationFont = src.getString(TranslationFontField).toFont,
        token = src.getDocument(TokenField).toToken,
        partOfSpeechNodes = src.getDocuments(PartOfSpeechNodesField).map(_.toPartOfSpeechNode)
      )

    private def toRelationshipNode: RelationshipNode =
      RelationshipNode(
        id = src.getUUID(IdField),
        dependencyGraphId = src.getUUID(DependencyGraphIdField),
        textPoint = src.getString(TextPointField).toPoint,
        translate = src.getString(TranslateField).toPoint,
        control1 = src.getString(Control1Field).toPoint,
        control2 = src.getString(Control2Field).toPoint,
        arrow = src.getString(ArrowField).toPoint,
        font = src.getString(FontField).toFont,
        relationshipInfo = src.getString(RelationshipInfoField).toRelationshipInfo
      )

    private def toPhraseNode: PhraseNode =
      PhraseNode(
        id = src.getUUID(IdField),
        dependencyGraphId = src.getUUID(DependencyGraphIdField),
        textPoint = src.getString(TextPointField).toPoint,
        translate = src.getString(TranslateField).toPoint,
        line = src.getString(LineField).toLine,
        circle = src.getString(CircleField).toPoint,
        phraseInfo = src.getString(PhraseInfoField).toPhraseInfo,
        font = src.getString(FontField).toFont
      )
  }

  extension (src: PartOfSpeechNode) {
    private def toDocument: Document =
      Document
        .createDocument(IdField, src.id.toString)
        .put(DependencyGraphIdField, src.dependencyGraphId.toString)
        .put(LocationIdField, src.location.id)
        .put(TokenIdField, src.location.tokenId)
        .put(NodeTypeField, src.graphNodeType.name())
        .put(TextPointField, src.textPoint.asJson.noSpaces)
        .put(TranslateField, src.translate.asJson.noSpaces)
        .put(CircleField, src.circle.asJson.noSpaces)
        .put(FontField, src.font.asJson.noSpaces)
        .put(LocationField, src.location.toLocationDocument)
        .put(HiddenField, src.hidden)
  }

  extension (src: TerminalNode) {
    private def toDocument: Document =
      Document
        .createDocument(IdField, src.id.toString)
        .put(DependencyGraphIdField, src.dependencyGraphId.toString)
        .put(TokenIdField, src.token.id)
        .put(NodeTypeField, src.graphNodeType.name())
        .put(TextPointField, src.textPoint.asJson.noSpaces)
        .put(TranslateField, src.translate.asJson.noSpaces)
        .put(LineField, src.line.asJson.noSpaces)
        .put(TranslationPointField, src.translationPoint.asJson.noSpaces)
        .put(FontField, src.font.asJson.noSpaces)
        .put(TranslationFontField, src.translationFont.asJson.noSpaces)
        .put(TokenField, src.token.toTokenDocument)
        .put(PartOfSpeechNodesField, src.partOfSpeechNodes.map(_.toDocument).asJava)

    private def updateDocument(document: Document): Document =
      document
        .put(TextPointField, src.textPoint.asJson.noSpaces)
        .put(TranslateField, src.translate.asJson.noSpaces)
        .put(LineField, src.line.asJson.noSpaces)
        .put(TranslationPointField, src.translationPoint.asJson.noSpaces)
        .put(FontField, src.font.asJson.noSpaces)
        .put(TranslationFontField, src.translationFont.asJson.noSpaces)
        .put(TokenField, src.token.toTokenDocument)
        .put(PartOfSpeechNodesField, src.partOfSpeechNodes.map(_.toDocument).asJava)
  }

  extension (src: RelationshipNode) {
    private def toDocument: Document =
      Document
        .createDocument(IdField, src.id.toString)
        .put(DependencyGraphIdField, src.dependencyGraphId.toString)
        .put(NodeTypeField, src.graphNodeType.name())
        .put(TextPointField, src.textPoint.asJson.noSpaces)
        .put(TranslateField, src.translate.asJson.noSpaces)
        .put(Control1Field, src.control1.asJson.noSpaces)
        .put(Control2Field, src.control2.asJson.noSpaces)
        .put(ArrowField, src.arrow.asJson.noSpaces)
        .put(RelationshipInfoField, src.relationshipInfo.asJson.noSpaces)
        .put(FontField, src.font.asJson.noSpaces)

    private def updateDocument(document: Document): Document =
      document
        .put(NodeTypeField, src.graphNodeType.name())
        .put(TextPointField, src.textPoint.asJson.noSpaces)
        .put(TranslateField, src.translate.asJson.noSpaces)
        .put(Control1Field, src.control1.asJson.noSpaces)
        .put(Control2Field, src.control2.asJson.noSpaces)
        .put(ArrowField, src.arrow.asJson.noSpaces)
        .put(RelationshipInfoField, src.relationshipInfo.asJson.noSpaces)
        .put(FontField, src.font.asJson.noSpaces)
  }

  extension (src: PhraseNode) {
    private def toDocument: Document =
      Document
        .createDocument(IdField, src.id.toString)
        .put(DependencyGraphIdField, src.dependencyGraphId.toString)
        .put(NodeTypeField, src.graphNodeType.name())
        .put(TextPointField, src.textPoint.asJson.noSpaces)
        .put(TranslateField, src.translate.asJson.noSpaces)
        .put(LineField, src.line.asJson.noSpaces)
        .put(CircleField, src.circle.asJson.noSpaces)
        .put(PhraseInfoField, src.phraseInfo.asJson.noSpaces)
        .put(FontField, src.font.asJson.noSpaces)

    private def updateDocument(document: Document): Document =
      document
        .put(TextPointField, src.textPoint.asJson.noSpaces)
        .put(TranslateField, src.translate.asJson.noSpaces)
        .put(LineField, src.line.asJson.noSpaces)
        .put(CircleField, src.circle.asJson.noSpaces)
        .put(PhraseInfoField, src.phraseInfo.asJson.noSpaces)
        .put(FontField, src.font.asJson.noSpaces)
  }

  private[persistence] def apply(db: Nitrite): GraphNodeCollection = new GraphNodeCollection(db)
}
