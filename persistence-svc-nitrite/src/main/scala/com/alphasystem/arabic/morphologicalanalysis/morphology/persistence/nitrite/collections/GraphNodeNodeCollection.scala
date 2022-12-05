package com.alphasystem
package arabic
package morphologicalanalysis
package morphology
package persistence
package nitrite
package collections

import morphologicalanalysis.graph.model.GraphNodeType
import morphology.model.{ Location, Token }
import morphology.graph.model.{ PartOfSpeechNode, TerminalNode }
import org.dizitart.no2.filters.Filters
import org.dizitart.no2.{ Document, FindOptions, IndexOptions, IndexType, Nitrite, SortOrder }

import java.util.UUID
import scala.jdk.CollectionConverters.*

class GraphNodeNodeCollection private (db: Nitrite) {

  import GraphNodeNodeCollection.*

  private[persistence] val collection = db.getCollection("graph_node")
  if !collection.hasIndex(TokenField) then {
    collection.createIndex(TokenField, IndexOptions.indexOptions(IndexType.Unique))
    collection.createIndex(VerseIdField, IndexOptions.indexOptions(IndexType.NonUnique))
  }

  private[persistence] def upsertTerminalNode(token: Token) =
    findByTokenIdInternal(token.id) match
      case Some(document) =>
        val terminalNode = createTerminalNode(token)
        val updatedDocument =
          document
            .put(TokenIdField, token.id)
            .put(TokenField, token.toTokenDocument)
            .put(PartOfSpeechNodesField, terminalNode.partOfSpeechNodes.map(_.toDocument).asJava)
        collection.update(updatedDocument)

      case None =>
        val terminalNode = createTerminalNode(token)
        collection.insert(terminalNode.toDocument)

  private[persistence] def upsertTerminalNodes(nodes: Seq[Token]): Unit =
    nodes.foreach(upsertTerminalNode)

  private[persistence] def findTerminalNode(id: Long): TerminalNode =
    findTerminalNodeInternal(id) match
      case Some(document) => toTerminalNode(document)
      case None           => throw EntityNotFound(classOf[TerminalNode], id.toString)

  private[persistence] def findByTokenIds(tokenIds: Seq[Long]): Seq[TerminalNode] = tokenIds.flatMap(findByTokenId)

  private[persistence] def findByTokenId(tokenId: Long): Option[TerminalNode] =
    findByTokenIdInternal(tokenId).map(toTerminalNode)

  private[persistence] def deleteByVerseId(verseId: Long): Int =
    collection.remove(Filters.eq(VerseIdField, verseId)).getAffectedCount

  private def findTerminalNodeInternal(id: Long): Option[Document] =
    collection.find(Filters.eq(NodeIdField, id)).asScalaList.headOption

  private def findByTokenIdInternal(tokenId: Long) =
    collection.find(Filters.eq(TokenIdField, tokenId)).asScalaList.headOption
}

object GraphNodeNodeCollection {

  private val GraphNodeTypeField = "graph_node_type"
  private val HiddenField = "hidden"
  private val LocationIdField = "location_id"
  private val NodeIdField = "node_id"
  private val PartOfSpeechNodesField = "part_of_speech_nodes"
  private val TokenIdField = "token_id"
  private val TokenField = "token"
  private val VerseIdField = "verse_id"

  private def toTerminalNode(src: Document): TerminalNode = {
    val token = src.getDocument(TokenField).toToken
    TerminalNode(
      graphNodeType = GraphNodeType.valueOf(src.getString(GraphNodeTypeField)),
      token = token,
      partOfSpeechNodes = token.locations.flatMap { location =>
        src.getDocuments(PartOfSpeechNodesField).map(document => toPartOfSpeechNode(document, location))
      }
    )
  }

  private def createTerminalNode(src: Token): TerminalNode =
    if src.hidden then TerminalNode.createHiddenNode(src)
    else TerminalNode.createTerminalNode(src)

  private def toPartOfSpeechNode(src: Document, location: Location): PartOfSpeechNode =
    PartOfSpeechNode(
      location = location,
      hidden = src.getBoolean(HiddenField)
    )

  extension (src: PartOfSpeechNode) {
    private def toDocument: Document =
      Document
        .createDocument(NodeIdField, src.id)
        .put(LocationIdField, src.location.id)
        .put(HiddenField, src.hidden)
  }

  extension (src: TerminalNode) {
    private def toDocument: Document =
      Document
        .createDocument(NodeIdField, src.id)
        .put(GraphNodeTypeField, src.graphNodeType.name())
        .put(TokenIdField, src.token.id)
        .put(TokenField, src.token.toTokenDocument)
        .put(VerseIdField, src.token.verseId)
        .put(PartOfSpeechNodesField, src.partOfSpeechNodes.map(_.toDocument).asJava)
  }

  private[persistence] def apply(db: Nitrite): GraphNodeNodeCollection = new GraphNodeNodeCollection(db)
}
