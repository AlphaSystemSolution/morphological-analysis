package com.alphasystem
package arabic
package morphologicalanalysis
package morphology
package persistence
package repository

import morphology.graph.model.RelationshipInfo

import java.util.UUID
import scala.concurrent.Future

trait RelationshipInfoRepository {

  def insert(relationshipInfo: RelationshipInfo): Future[RelationshipInfo]
  def update(relationshipInfo: RelationshipInfo): Future[Done]
  def updateDependencyGraphId(relationshipInfoId: Long, dependencyGraphId: UUID): Future[Done]
  def remove(id: Long): Future[Done]
  def find(id: Long): Future[Option[RelationshipInfo]]
  def findByDependencyGraphId(id: UUID): Future[Seq[RelationshipInfo]]
}
