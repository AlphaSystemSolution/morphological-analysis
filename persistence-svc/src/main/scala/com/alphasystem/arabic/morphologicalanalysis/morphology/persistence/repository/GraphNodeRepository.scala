package com.alphasystem
package arabic
package morphologicalanalysis
package morphology
package persistence
package repository

import com.alphasystem.arabic.morphologicalanalysis.morphology.persistence.model.GraphNodeLifted
import morphologicalanalysis.graph.model.GraphNodeType
import graph.model.{ GraphNode, PartOfSpeechNode, PhraseNode, RelationshipNode, RootNode, TerminalNode }
import morphology.persistence.*
import morphology.model.*
import io.circe.generic.auto.*
import io.circe.parser.*
import io.circe.syntax.*
import io.getquill.*
import io.getquill.context.*

class GraphNodeRepository(dataSource: CloseableDataSource) {

  private val ctx: PostgresJdbcContext[Literal] =
    new PostgresJdbcContext(Literal, dataSource)

  import ctx.*

  private val schema: Quoted[EntityQuery[GraphNodeLifted]] = quote(
    querySchema[GraphNodeLifted](
      "graph_node",
      _.graphId -> "graph_id",
      _.id -> "node_id",
      _.graphNodeType -> "graph_type"
    )
  )

  def create(entity: GraphNode): Long = run(quote(schema.insertValue(lift(toLifted(entity)))))

  def createAll(entities: Seq[GraphNode]): Unit = {
    inline def query = quote {
      liftQuery(entities.map(toLifted)).foreach { lifted =>
        querySchema[GraphNodeLifted](
          "graph_node",
          _.graphId -> "graph_id",
          _.id -> "node_id",
          _.graphNodeType -> "graph_type"
        ).insertValue(lifted)
      }
    }

    run(query)
  }

  def findByGraphId(graphId: String): Seq[GraphNode] = {
    inline def query = quote(schema.filter(e => e.graphId == lift(graphId)))
    run(query).map(decodeDocument)
  }

  private def toLifted(entity: GraphNode) =
    GraphNodeLifted(
      id = entity.id,
      graphId = entity.dependencyGraphId,
      graphNodeType = entity.graphNodeType.name(),
      document = entity.asJson.noSpaces
    )

  private def decodeDocument(lifted: GraphNodeLifted) =
    decode[GraphNode](lifted.document) match
      case Left(ex)     => throw ex
      case Right(value) => value
}

object GraphNodeRepository {
  def apply(dataSource: CloseableDataSource): GraphNodeRepository = new GraphNodeRepository(dataSource)
}
