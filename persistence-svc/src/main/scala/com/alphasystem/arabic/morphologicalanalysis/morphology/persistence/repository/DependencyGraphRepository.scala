package com.alphasystem
package arabic
package morphologicalanalysis
package morphology
package persistence
package repository

import morphology.graph.model.{ DependencyGraph, GraphMetaInfo }
import model.DependencyGraphLifted
import morphology.persistence.*
import morphology.model.*
import io.circe.generic.auto.*
import io.circe.parser.decode
import io.circe.syntax.*
import io.getquill.*
import io.getquill.context.*

class DependencyGraphRepository(dataSource: CloseableDataSource)
    extends BaseRepository[DependencyGraph, DependencyGraphLifted](dataSource) {

  import ctx.*

  override protected val schema: Quoted[EntityQuery[DependencyGraphLifted]] =
    quote(
      querySchema[DependencyGraphLifted](
        "dependency_graph",
        _.chapterNumber -> "chapter_number",
        _.verseNumber -> "verse_number",
        _.startTokenNumber -> "start_token_number",
        _.endTokenNumber -> "end_token_number",
        _.graphText -> "graph_text"
      )
    )

  override def create(entity: DependencyGraph): Long = {
    val lifted = toLifted(entity)
    run(
      quote(
        schema
          .insert(
            _.id -> lift(lifted.id),
            _.chapterNumber -> lift(lifted.chapterNumber),
            _.verseNumber -> lift(lifted.verseNumber),
            _.startTokenNumber -> lift(lifted.startTokenNumber),
            _.endTokenNumber -> lift(lifted.endTokenNumber),
            _.graphText -> lift(lifted.graphText),
            _.document -> lift(lifted.document)
          )
          .onConflictIgnore
      )
    )
  }

  def findAll: Seq[DependencyGraph] = {
    inline def query = quote(
      schema.groupByMap(e => (e.id, e.chapterNumber, e.verseNumber, e.startTokenNumber))(e =>
        (e.id, e.chapterNumber, e.verseNumber, e.startTokenNumber, e.endTokenNumber, e.graphText, e.document)
      )
    )
    run(query)
      .map { case (id, chapterNumber, verseNumber, startTokenNumber, endTokenNumber, graphText, document) =>
        DependencyGraphLifted(
          id,
          chapterNumber,
          verseNumber,
          startTokenNumber,
          endTokenNumber,
          graphText,
          document
        )
      }
      .map(decodeDocument)
  }

  override def bulkCreate(entities: List[DependencyGraph]): Unit =
    throw new RuntimeException("function not implemented")

  override protected def runQuery(q: Quoted[EntityQuery[DependencyGraphLifted]]): Seq[DependencyGraphLifted] = run(q)

  private def toLifted(dependencyGraph: DependencyGraph) =
    DependencyGraphLifted(
      id = dependencyGraph.id,
      chapterNumber = dependencyGraph.chapterNumber,
      verseNumber = dependencyGraph.verseNumber,
      startTokenNumber = dependencyGraph.startTokenNumber,
      endTokenNumber = dependencyGraph.endTokenNumber,
      graphText = dependencyGraph.text,
      document = dependencyGraph.metaInfo.asJson.noSpaces
    )

  override protected def decodeDocument(lifted: DependencyGraphLifted): DependencyGraph = {
    val graphMetaInfo = decode[GraphMetaInfo](lifted.document) match
      case Left(ex)     => throw ex
      case Right(value) => value

    DependencyGraph(
      id = lifted.id,
      chapterNumber = lifted.chapterNumber,
      verseNumber = lifted.verseNumber,
      startTokenNumber = lifted.startTokenNumber,
      endTokenNumber = lifted.endTokenNumber,
      text = lifted.graphText,
      metaInfo = graphMetaInfo
    )
  }
}

object DependencyGraphRepository {

  def apply(dataSource: CloseableDataSource) = new DependencyGraphRepository(dataSource)
}
