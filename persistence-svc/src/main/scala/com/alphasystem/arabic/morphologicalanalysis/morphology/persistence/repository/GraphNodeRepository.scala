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
      _.id -> "node_id"
    )
  )

  def create(entity: GraphNode): Long = {
    val lifted = toLifted(entity)
    run(
      quote(
        schema
          .insert(_.id -> lift(lifted.id), _.graphId -> lift(lifted.graphId), _.document -> lift(lifted.document))
          .onConflictIgnore
      )
    )
  }

  def createAll(entities: Seq[GraphNode]): Unit = {
    val graphIds = entities.map(_.dependencyGraphId).toSet
    if graphIds.size > 1 then {
      throw new IllegalArgumentException(s"Can add nodes for single graph ids: ${graphIds.mkString("[", ", ", "]")}")
    }

    if entities.nonEmpty then {
      // get the difference between existing entities and given entities, the difference will be entities to delete
      val graphId = graphIds.head
      val entitiesToAdd = entities.map(_.id)

      val existingEntities = findByGraphId(graphId).map(_.id)
      if existingEntities.nonEmpty then {
        val entitiesToDelete = existingEntities.diff(entitiesToAdd)
        deleteNodes(graphId, entitiesToDelete)
      }

      entities.map(create)
    }
  }

  def findByPK(graphId: String, nodeId: String): Option[GraphNode] = {
    inline def query = quote(schema.filter(e => e.graphId == lift(graphId)).filter(e => e.id == lift(nodeId)))
    run(query).map(decodeDocument).headOption
  }

  def findByGraphId(graphId: String): List[GraphNode] = {
    inline def query = quote(schema.filter(e => e.graphId == lift(graphId)))
    run(query).map(decodeDocument)
  }

  def deleteNodes(graphId: String, nodesToDelete: Seq[String]): Unit = {
    inline def query = quote(
      schema.filter(e => e.graphId == lift(graphId)).filter(e => liftQuery(nodesToDelete).contains(e.id))
    )
    run(query.delete)
  }

  private def toLifted(entity: GraphNode) =
    GraphNodeLifted(
      id = entity.id,
      graphId = entity.dependencyGraphId,
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
