package com.alphasystem
package arabic
package morphologicalanalysis
package morphology
package persistence
package repository
package impl
package phrase_info
package table

import persistence.model.{ PhraseInfo, PhraseLocationRelation }

import java.util.UUID

private[phrase_info] trait PhraseInfoTableRepository extends PhraseInfoTable with SequenceSupport {

  import jdbcProfile.api.*

  def insertPhraseInfo(phraseInfo: PhraseInfo): Insert = phraseInfoTableQuery += phraseInfo
  def insertRelationships(relations: Seq[PhraseLocationRelation]): MultiInsert = relationsTableQuery ++= relations
  def updateDependencyGraphId(phraseId: Long, dependencyGraphId: UUID): Insert =
    updateDependencyGraphIdQuery(phraseId).update(Some(dependencyGraphId))
  def updateRelationsDependencyGraphId(phraseId: Long, dependencyGraphId: UUID): Insert =
    updateRelationsDependencyGraphIdQuery(phraseId).update(Some(dependencyGraphId))
  def getByPhraseId(id: Long): Single[PhraseInfo] = getByPhraseIdQuery(id).result.headOption
  def getRelationsByPhraseId(id: Long): Multi[PhraseLocationRelation] = getRelationsByPhraseIdQuery(id).result
  def getByDependencyGraphId(id: UUID): Multi[PhraseInfo] = getByDependencyGraphIdQuery(id).result
  def getByRelationsDependencyGraphId(id: UUID): Multi[PhraseLocationRelation] =
    getByRelationsDependencyGraphIdQuery(id).result
  def removeById(id: Long): Insert = getByPhraseIdQuery(id).delete
  def removeRelationsById(id: Long): Insert = getRelationsByPhraseIdQuery(id).delete
  def removeByDependencyGraphId(id: UUID): Insert = getByDependencyGraphIdQuery(id).delete
  def removeByRelationsDependencyGraphId(id: UUID): Insert = getByRelationsDependencyGraphIdQuery(id).delete

  private lazy val getByPhraseIdQuery = Compiled { (id: Rep[Long]) =>
    phraseInfoTableQuery.filter(_.id === id)
  }

  private lazy val getRelationsByPhraseIdQuery = Compiled { (id: Rep[Long]) =>
    relationsTableQuery.filter(_.phraseId === id)
  }

  private lazy val getByDependencyGraphIdQuery = Compiled { (id: Rep[UUID]) =>
    phraseInfoTableQuery.filter(_.dependencyGraphId === id)
  }

  private lazy val getByRelationsDependencyGraphIdQuery = Compiled { (id: Rep[UUID]) =>
    relationsTableQuery.filter(_.dependencyGraphId === id)
  }

  private lazy val updateDependencyGraphIdQuery = Compiled { (id: Rep[Long]) =>
    phraseInfoTableQuery.filter(_.id === id).map(_.dependencyGraphId)
  }

  private lazy val updateRelationsDependencyGraphIdQuery = Compiled { (id: Rep[Long]) =>
    relationsTableQuery.filter(_.phraseId === id).map(_.dependencyGraphId)
  }
}
