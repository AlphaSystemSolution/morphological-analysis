package com.alphasystem
package arabic
package morphologicalanalysis
package morphology
package persistence
package nitrite
package collections

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

class GraphNodeMetaInfoCollection private (db: Nitrite) {

  import GraphNodeMetaInfoCollection.*

  private val graphNodeCollection = GraphNodeNodeCollection(db)
  private[persistence] val collection = db.getCollection("graph_node_meta_info")
  if !collection.hasIndex(DependencyGraphIdField) then {
    collection.createIndex(DependencyGraphIdField, IndexOptions.indexOptions(IndexType.NonUnique))
    collection.createIndex(NodeIdField, IndexOptions.indexOptions(IndexType.NonUnique))
  }

  private[persistence] def upsertNodes(nodes: Seq[GraphNodeMetaInfo]): Unit =
    nodes.foreach { node =>
      findNode(node.id) match
        case Some(document) =>
          val updatedDocument =
            node.graphNodeType match
              case Terminal | Hidden | Implied => node.asInstanceOf[TerminalNodeMetaInfo].updateDocument(document)
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
              case n: TerminalNodeMetaInfo => Some(n.toDocument)
              case n: PhraseNodeMetaInfo   =>
                // TODO: to be implemented
                None
              case n: RelationshipNodeMetaInfo[?, ?] =>
                // TODO: to be implemented
                None
              case _ => None

          maybeDocument.foreach(collection.insert(_))
    }

  private[persistence] def findByDependencyGraphId(dependencyGraphId: UUID): Seq[GraphNodeMetaInfo] = {
    collection.find(Filters.eq(DependencyGraphIdField, dependencyGraphId.toString)).asScalaList.flatMap { document =>
      GraphNodeType.valueOf(document.getString(NodeTypeField)) match
        case Terminal | Hidden | Implied | Reference =>
          val terminalNode = graphNodeCollection.findTerminalNode(document.getLong(NodeIdField))
          Some(document.toTerminalNodeMetaInfo(terminalNode))

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

object GraphNodeMetaInfoCollection {

  private val CircleField = "circle"
  private val DependencyGraphIdField = "dependency_graph_id"
  private val FontField = "font"
  private val IdField = "id"
  private val LineField = "line"
  private val NodeIdField = "node_id"
  private val NodeTypeField = "node_type"
  private val PartOfSpeechNodesField = "part_of_speech_nodes"
  private val TerminalNodeIdField = "terminal_node_id"
  private val TextPointField = "text_point"
  private val TranslateField = "translate"
  private val TranslationFontField = "translation_font"

  extension (src: Document) {
    private def toPartOfSpeechNodeMetaInfo(partOfSpeechNode: PartOfSpeechNode): PartOfSpeechNodeMetaInfo =
      PartOfSpeechNodeMetaInfo(
        id = src.getUUID(IdField),
        dependencyGraphId = src.getUUID(DependencyGraphIdField),
        text = src.getString(TextPointField).toPoint,
        translate = src.getString(TranslateField).toPoint,
        circle = src.getString(CircleField).toPoint,
        font = src.getString(FontField).toFont,
        partOfSpeechNode = partOfSpeechNode
      )

    private def toTerminalNodeMetaInfo(terminalNode: TerminalNode): TerminalNodeMetaInfo = {
      val partOfSpeechNodes =
        terminalNode.partOfSpeechNodes.zip(src.getDocuments(PartOfSpeechNodesField)).map { case (node, document) =>
          document.toPartOfSpeechNodeMetaInfo(node)
        }
      TerminalNodeMetaInfo(
        id = src.getUUID(IdField),
        dependencyGraphId = src.getUUID(DependencyGraphIdField),
        text = src.getString(TextPointField).toPoint,
        translate = src.getString(TranslateField).toPoint,
        line = src.getString(LineField).toLine,
        font = src.getString(FontField).toFont,
        translationFont = src.getString(TranslationFontField).toFont,
        terminalNode = terminalNode,
        partOfSpeechNodes = partOfSpeechNodes
      )
    }

  }

  extension (src: PartOfSpeechNodeMetaInfo) {
    private def toDocument: Document =
      Document
        .createDocument(IdField, src.id.toString)
        .put(DependencyGraphIdField, src.dependencyGraphId.toString)
        .put(NodeIdField, src.partOfSpeechNode.id)
        .put(TerminalNodeIdField, src.partOfSpeechNode.location.tokenId)
        .put(NodeTypeField, src.graphNodeType.name())
        .put(TextPointField, src.text.asJson.noSpaces)
        .put(TranslateField, src.translate.asJson.noSpaces)
        .put(CircleField, src.circle.asJson.noSpaces)
        .put(FontField, src.font.asJson.noSpaces)
  }

  extension (src: TerminalNodeMetaInfo) {
    private def toDocument: Document =
      Document
        .createDocument(IdField, src.id.toString)
        .put(DependencyGraphIdField, src.dependencyGraphId.toString)
        .put(NodeIdField, src.terminalNode.id)
        .put(NodeTypeField, src.graphNodeType.name())
        .put(TextPointField, src.text.asJson.noSpaces)
        .put(TranslateField, src.translate.asJson.noSpaces)
        .put(LineField, src.line.asJson.noSpaces)
        .put(FontField, src.font.asJson.noSpaces)
        .put(TranslationFontField, src.translationFont.asJson.noSpaces)
        .put(PartOfSpeechNodesField, src.partOfSpeechNodes.map(_.toDocument).asJava)

    private def updateDocument(document: Document): Document =
      document
        .put(TextPointField, src.text.asJson.noSpaces)
        .put(TranslateField, src.translate.asJson.noSpaces)
        .put(LineField, src.line.asJson.noSpaces)
        .put(FontField, src.font.asJson.noSpaces)
        .put(TranslationFontField, src.translationFont.asJson.noSpaces)
        .put(PartOfSpeechNodesField, src.partOfSpeechNodes.map(_.toDocument).asJava)
  }

  private[persistence] def apply(db: Nitrite): GraphNodeMetaInfoCollection = new GraphNodeMetaInfoCollection(db)
}