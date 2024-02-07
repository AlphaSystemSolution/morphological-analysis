package com.alphasystem
package arabic
package morphologicalanalysis
package morphology
package persistence
package repository
package impl
package relationship_info
package table

import persistence.model.RelationshipInfo

import java.util.UUID

private[relationship_info] trait RelationshipInfoTableRepository extends RelationshipInfoTable with SequenceSupport {

  import jdbcProfile.api.*

  def insert(relationshipInfo: RelationshipInfo): Insert = relationshipInfoTableQuery += relationshipInfo
  def update(relationshipInfo: RelationshipInfo): Insert = relationshipInfoTableQuery.update(relationshipInfo)
  def updateDependencyGraphId(relationshipInfoId: Long, dependencyGraphId: UUID): Insert =
    updateDependencyGraphIdQuery(relationshipInfoId).update(Some(dependencyGraphId))
  def remove(id: Long): Insert = getByIdQuery(id).delete
  def getById(id: Long): Single[RelationshipInfo] = getByIdQuery(id).result.headOption
  def getByDependencyGraphId(id: UUID): Multi[RelationshipInfo] = getByDependencyGraphIdQuery(id).result
  def removeByDependencyGraphId(id: UUID): Insert = getByDependencyGraphIdQuery(id).delete

  private lazy val getByIdQuery = Compiled { (id: Rep[Long]) =>
    relationshipInfoTableQuery.filter(_.id === id)
  }

  private lazy val getByDependencyGraphIdQuery = Compiled { (id: Rep[UUID]) =>
    relationshipInfoTableQuery.filter(_.dependencyGraphId === id)
  }

  private lazy val updateDependencyGraphIdQuery = Compiled { (id: Rep[Long]) =>
    relationshipInfoTableQuery.filter(_.id === id).map(_.dependencyGraphId)
  }
}
