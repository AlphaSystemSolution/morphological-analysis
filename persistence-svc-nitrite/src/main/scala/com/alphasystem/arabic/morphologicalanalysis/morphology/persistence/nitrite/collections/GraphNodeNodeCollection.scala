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
import org.dizitart.no2.{ Document, FindOptions, IndexOptions, IndexType, Lookup, Nitrite, SortOrder }

import java.util.UUID
import scala.jdk.CollectionConverters.*

class GraphNodeNodeCollection private (db: Nitrite) {

  import GraphNodeNodeCollection.*

  private val tokenCollection = TokenCollection(db)
  private[persistence] val collection = db.getCollection("graph_node")
  if !collection.hasIndex(TokenIdField) then {
    collection.createIndex(TokenIdField, IndexOptions.indexOptions(IndexType.Unique))
    collection.createIndex(VerseIdField, IndexOptions.indexOptions(IndexType.NonUnique))
  }

  private[persistence] def upsertTerminalNode(token: Token) =
    findByTokenIdInternal(token.id) match
      case Some(document) =>
        val terminalNode = token.createTerminalNode
        val updatedDocument =
          document.put(PartOfSpeechNodesField, terminalNode.partOfSpeechNodes.map(_.toDocument).asJava)
        collection.update(updatedDocument)

      case None => collection.insert(token.toDocument)

  private[persistence] def upsertTerminalNodes(nodes: Seq[Token]): Unit =
    nodes.foreach(upsertTerminalNode)

  private[persistence] def findTerminalNode(id: Long): TerminalNode =
    findTerminalNodeInternal(id) match
      case Some(document) => document.toTerminalNode(findToken(document.getLong(TokenIdField)))
      case None           => throw EntityNotFound(classOf[TerminalNode], id.toString)

  private[persistence] def findByTokenIds(tokenIds: Seq[Long]): Seq[TerminalNode] = tokenIds.flatMap(findByTokenId)

  private[persistence] def findByTokenId(tokenId: Long): Option[TerminalNode] = {
    val lookup = new Lookup()
    lookup.setLocalField(TokenIdField)
    lookup.setForeignField(TokenIdField)
    lookup.setTargetField("token")
    collection
      .find(Filters.eq(TokenIdField, tokenId))
      .join(tokenCollection.collection.find(Filters.eq(TokenIdField, tokenId)), lookup)
      .asScala
      .toList
      .headOption
      .map { document =>
        val tokenDocuments = document.getDocumentAsSet("token")
        if tokenDocuments.isEmpty then throw EntityNotFound(classOf[Token], tokenId.toString)
        else document.toTerminalNode(tokenDocuments.head.toToken)
      }
  }

  private[persistence] def deleteByVerseId(verseId: Long): Int =
    collection.remove(Filters.eq(VerseIdField, verseId)).getAffectedCount

  private def findTerminalNodeInternal(id: Long): Option[Document] =
    collection.find(Filters.eq(NodeIdField, id)).asScalaList.headOption

  private def findByTokenIdInternal(tokenId: Long) =
    collection.find(Filters.eq(TokenIdField, tokenId)).asScalaList.headOption

  private def findToken(tokenId: Long): Token =
    tokenCollection.findById(tokenId) match
      case Some(token) => token
      case None        => throw EntityNotFound(classOf[Token], tokenId.toString)
}

object GraphNodeNodeCollection {

  extension (src: PartOfSpeechNode) {
    private def toDocument: Document =
      Document
        .createDocument(NodeIdField, src.id)
        .put(LocationIdField, src.location.id)
        .put(HiddenField, src.hidden)
  }

  extension (src: Token) {
    private def createTerminalNode: TerminalNode =
      if src.hidden then TerminalNode.createHiddenNode(src)
      else TerminalNode.createTerminalNode(src)

    private def toDocument: Document = src.createTerminalNode.toDocument
  }

  extension (src: TerminalNode) {
    private def toDocument: Document =
      Document
        .createDocument(NodeIdField, src.id)
        .put(GraphNodeTypeField, src.graphNodeType.name())
        .put(TokenIdField, src.token.id)
        .put(VerseIdField, src.token.verseId)
        .put(PartOfSpeechNodesField, src.partOfSpeechNodes.map(_.toDocument).asJava)
  }

  private[persistence] def apply(db: Nitrite): GraphNodeNodeCollection = new GraphNodeNodeCollection(db)
}
