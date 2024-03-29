package com.alphasystem
package arabic
package morphologicalanalysis
package morphology
package persistence
package nitrite
package collections

import morphologicalanalysis.graph.model.GraphNodeType
import morphology.model.{ Chapter, Token }
import morphology.graph.model.*
import io.circe.generic.auto.*
import io.circe.parser.*
import io.circe.syntax.*
import org.dizitart.no2.*
import org.dizitart.no2.filters.Filters

import java.util.UUID
import scala.jdk.CollectionConverters.*

class DependencyGraphCollection private (db: Nitrite) {

  import DependencyGraphCollection.*

  private val graphNodeCollection = GraphNodeCollection(db)
  private[persistence] val collection = db.getCollection("dependency_graph")
  if !collection.hasIndex(ChapterNumberField) then {
    collection.createIndex(ChapterNumberField, IndexOptions.indexOptions(IndexType.NonUnique, true))
  }

  private[persistence] def upsertDependencyGraph(dependencyGraph: DependencyGraph): Unit = {
    findByIdInternal(dependencyGraph.id) match
      case Some(document) => dependencyGraph.toUpdateDocument(document)
      case None           => collection.insert(dependencyGraph.toDocument)

    graphNodeCollection.upsertNodes(dependencyGraph.nodes)
  }

  private[persistence] def findByChapterAndVerseNumber(chapterNumber: Int, verseNumber: Int): Seq[DependencyGraph] = {
    val filter = Filters.and(
      Filters.eq(ChapterNumberField, chapterNumber),
      Filters.elemMatch(VerseNumbersField, Filters.eq("$", verseNumber))
    )
    collection.find(filter, FindOptions.sort(InitialTokenId, SortOrder.Ascending)).asScalaList.map { document =>
      val dependencyGraphId = document.getUUID(DependencyGraphIdField)
      val (nodes, tokens) = getNodes(dependencyGraphId)
      document.toDependencyGraph(tokens, nodes)
    }
  }

  private[persistence] def findById(dependencyGraphId: UUID): Option[DependencyGraph] = {
    findByIdInternal(dependencyGraphId) match
      case Some(document) =>
        val (nodes, tokens) = getNodes(dependencyGraphId)
        Some(document.toDependencyGraph(tokens, nodes))
      case None => None
  }

  private[persistence] def removeGraph(dependencyGraphId: UUID): Int =
    collection.remove(Filters.eq(DependencyGraphIdField, dependencyGraphId.toString)).getAffectedCount

  private def findByIdInternal(dependencyGraphId: UUID) =
    collection.find(Filters.eq(DependencyGraphIdField, dependencyGraphId.toString)).asScalaList.headOption

  private def getNodes(dependencyGraphId: UUID) = {
    val nodes = graphNodeCollection.findByDependencyGraphId(dependencyGraphId)
    val tokens =
      nodes
        .flatMap {
          case n: TerminalNode if n.graphNodeType == GraphNodeType.Terminal => Some(n.token)
          case _                                                            => None
        }
        .sortBy(_.id)

    (nodes, tokens)
  }
}

object DependencyGraphCollection {

  private val ChapterNameField = "chapter_name"
  private val ChapterNumberField = "chapter_number"
  private val DependencyGraphIdField = "dependency_graph_id"
  private val GraphMetaInfoField = "graph_meta_info"
  private val InitialTokenId = "initial_token_id"
  private val TextField = "text"
  private val TokenIdsField = "token_ids"
  private val VerseNumbersField = "verse_numbers"

  extension (src: Document) {
    private def toDependencyGraph(tokens: Seq[Token], nodes: Seq[GraphNode]): DependencyGraph =
      DependencyGraph(
        id = src.getUUID(DependencyGraphIdField),
        chapterNumber = src.getInt(ChapterNumberField),
        chapterName = src.getString(ChapterNameField),
        metaInfo = src.getString(GraphMetaInfoField).toGraphMetaInfo,
        verseNumbers = src.getIntList(VerseNumbersField),
        tokens = tokens,
        nodes = nodes
      )
  }

  extension (src: DependencyGraph) {
    def toDocument: Document =
      Document
        .createDocument(DependencyGraphIdField, src.id.toString)
        .put(ChapterNumberField, src.chapterNumber)
        .put(ChapterNameField, src.chapterName)
        .put(InitialTokenId, src.tokens.map(_.id).head)
        .put(TextField, src.text)
        .put(GraphMetaInfoField, src.metaInfo.asJson.noSpaces)
        .put(VerseNumbersField, src.verseNumbers.asJava)
        .put(TokenIdsField, src.tokens.map(_.id).asJava)

    def toUpdateDocument(document: Document): Document =
      document.put(GraphMetaInfoField, src.metaInfo.asJson.noSpaces)
  }
  private[persistence] def apply(db: Nitrite): DependencyGraphCollection = new DependencyGraphCollection(db)
}
