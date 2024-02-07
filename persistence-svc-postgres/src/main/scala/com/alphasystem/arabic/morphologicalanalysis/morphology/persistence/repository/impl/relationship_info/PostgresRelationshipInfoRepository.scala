package com.alphasystem
package arabic
package morphologicalanalysis
package morphology
package persistence
package repository
package impl
package relationship_info

import morphology.graph.model.RelationshipInfo
import relationship_info.table.RelationshipInfoTableRepository
import slick.jdbc.JdbcBackend.Database

import java.util.UUID
import scala.concurrent.{ ExecutionContext, Future }

class PostgresRelationshipInfoRepository private[impl] (
  executor: JdbcExecutorFactory#JdbcExecutor
)(implicit ec: ExecutionContext)
    extends RelationshipInfoRepository {

  private val repository = new RelationshipInfoTableRepository {
    override val jdbcProfile: ExtendedPostgresProfile = PostgresProfileWrapper.profile
  }
  override def insert(relationshipInfo: RelationshipInfo): Future[RelationshipInfo] =
    executor.exec(repository.sequenceQuery).flatMap { id =>
      val updatedInfo = relationshipInfo.copy(id = id)
      executor.exec(repository.insert(updatedInfo.toLifted)).map(_ => updatedInfo)
    }

  override def update(relationshipInfo: RelationshipInfo): Future[Done] =
    executor.exec(repository.update(relationshipInfo.toLifted)).map(_ => Done)

  override def updateDependencyGraphId(relationshipInfoId: Long, dependencyGraphId: UUID): Future[Done] =
    executor.exec(repository.updateDependencyGraphId(relationshipInfoId, dependencyGraphId)).map(_ => Done)

  override def remove(id: Long): Future[Done] = executor.exec(repository.remove(id)).map(_ => Done)

  override def find(id: Long): Future[Option[RelationshipInfo]] =
    executor.exec(repository.getById(id).map(_.map(_.toEntity)))

  override def findByDependencyGraphId(id: UUID): Future[Seq[RelationshipInfo]] =
    executor.exec(repository.getByDependencyGraphId(id).map(_.map(_.toEntity)))
}

object RelationshipInfoRepository {
  def apply(db: Database)(implicit ec: ExecutionContext): RelationshipInfoRepository = {
    val executor = new JdbcExecutorFactory(PostgresProfileWrapper)(db)
    new PostgresRelationshipInfoRepository(executor)
  }
}
