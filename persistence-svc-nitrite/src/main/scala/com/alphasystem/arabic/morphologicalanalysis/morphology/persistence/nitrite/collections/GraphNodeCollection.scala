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

  private[persistence] val collection = db.getCollection("graph_node_meta_info")
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
              case Terminal | Hidden | Implied => node.asInstanceOf[TerminalNode].updateDocument(document)
              case Phrase                      =>
                // TODO: to be implemented
                document

              case Relationship =>
                // TODO: to be implemented
                document

              case Reference =>
                // TODO: to be implemented
                document

              case _ =>
                // don't need to do anything
                document

          collection.update(updatedDocument)

        case None =>
          val maybeDocument =
            node match
              case n: TerminalNode => Some(n.toDocument)
              case n: PhraseNode   =>
                // TODO: to be implemented
                None
              case n: RelationshipNode =>
                // TODO: to be implemented
                None
              case _ => None

          maybeDocument.foreach(collection.insert(_))
    }

  private[persistence] def findByDependencyGraphId(dependencyGraphId: UUID): Seq[GraphNode] = {
    collection.find(Filters.eq(DependencyGraphIdField, dependencyGraphId.toString)).asScalaList.flatMap { document =>
      GraphNodeType.valueOf(document.getString(NodeTypeField)) match
        case Terminal | Hidden | Implied | Reference => Some(document.toTerminalNodeMetaInfo)

        case Phrase =>
          // TODO: to be implemented
          None

        case Relationship =>
          // TODO: to be implemented
          None

        case _ => None
    }
  }

  private def findNode(id: UUID) = collection.find(Filters.eq(IdField, id.toString)).asScalaList.headOption

}

object GraphNodeCollection {

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
  private val TextPointField = "text_point"
  private val TokenField = "token"
  private val TranslateField = "translate"
  private val TranslationPointField = "translation_point"
  private val TranslationFontField = "translation_font"

  extension (src: Document) {
    private def toPartOfSpeechNodeMetaInfo: PartOfSpeechNode =
      PartOfSpeechNode(
        id = src.getUUID(IdField),
        dependencyGraphId = src.getUUID(DependencyGraphIdField),
        textPoint = src.getString(TextPointField).toPoint,
        translate = src.getString(TranslateField).toPoint,
        circle = src.getString(CircleField).toPoint,
        font = src.getString(FontField).toFont,
        location = src.getDocument(LocationField).toLocation
      )

    private def toTerminalNodeMetaInfo: TerminalNode = {
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
        partOfSpeechNodes = src.getDocuments(PartOfSpeechNodesField).map(_.toPartOfSpeechNodeMetaInfo)
      )
    }

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

  private[persistence] def apply(db: Nitrite): GraphNodeCollection = new GraphNodeCollection(db)
}
