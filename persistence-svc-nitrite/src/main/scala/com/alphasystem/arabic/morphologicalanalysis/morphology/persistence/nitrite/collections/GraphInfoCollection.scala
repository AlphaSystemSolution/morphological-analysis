package com.alphasystem
package arabic
package morphologicalanalysis
package morphology
package persistence
package nitrite
package collections

import morphologicalanalysis.graph.model.GraphNodeType
import morphologicalanalysis.graph.model.GraphNodeType.*
import morphology.graph.model.{ GraphInfo, PartOfSpeechInfo, TerminalInfo }
import morphology.model.{ Location, Token }
import morphology.utils.*
import org.dizitart.no2.filters.Filters
import org.dizitart.no2.{ Document, IndexOptions, IndexType, Nitrite }

import java.util.UUID

class GraphInfoCollection private (db: Nitrite) {

  import GraphInfoCollection.*

  private[persistence] val collection = db.getCollection("graph_info")

  private[collections] def upsertTerminalInfo(graphNodeType: GraphNodeType, token: Token): Unit =
    findByIdInternal(token.id.toUUID) match
      case Some(document) => collection.update(document.put(TokenField, token.toTokenDocument))

      case None =>
        val document =
          Document
            .createDocument(IdField, token.id.toUUID.toString)
            .put(NodeTypeField, graphNodeType.name())
            .put(TokenField, token.toTokenDocument)
        collection.insert(document)

  private[collections] def upsertPartOfSpeechInfo(location: Location): Unit =
    findByIdInternal(location.id.toUUID) match
      case Some(document) => collection.update(document.put(LocationField, location.toLocationDocument))

      case None =>
        val document =
          Document
            .createDocument(IdField, location.id.toUUID.toString)
            .put(NodeTypeField, GraphNodeType.PartOfSpeech.name())
            .put(LocationField, location.toLocationDocument)
        collection.insert(document)

  private[persistence] def findById(id: UUID): Option[GraphInfo] =
    findByIdInternal(id) match
      case Some(document) =>
        GraphNodeType.valueOf(document.getString(NodeTypeField)) match
          case Terminal | Reference | Hidden | Implied => Some(document.toTerminalInfo)
          case PartOfSpeech                            => Some(document.toPartOfSpeechInfo)
          case Phrase                                  => None
          case Relationship                            => None
          case Root                                    => None

      case None => None

  private def findByIdInternal(id: UUID): Option[Document] =
    collection.find(Filters.eq(IdField, id.toString)).asScalaList.headOption

}

object GraphInfoCollection {

  private val NodeTypeField = "node_type"
  private val IdField = "id"
  private val LocationField = "location"
  private val TokenField = "token"

  private[persistence] def apply(db: Nitrite) = new GraphInfoCollection(db)

  extension (src: Document) {
    private def toTerminalInfo: TerminalInfo =
      TerminalInfo(
        graphNodeType = GraphNodeType.valueOf(src.getString(NodeTypeField)),
        token = src.getDocument(TokenField).toToken
      )

    private def toPartOfSpeechInfo: PartOfSpeechInfo =
      PartOfSpeechInfo(src.getDocument(LocationField).toLocation)
  }
}
