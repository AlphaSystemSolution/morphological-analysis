package com.alphasystem
package arabic
package morphologicalanalysis
package morphology
package persistence
package repository
package impl
package phrase_info
package table

import persistence.model.PhraseInfo

import java.util.UUID

private[phrase_info] trait PhraseInfoTableRepository extends PhraseInfoTable with SequenceSupport {

  import jdbcProfile.api.*

  def insert(phraseInfos: Seq[PhraseInfo]): MultiInsert = phraseInfoTableQuery ++= phraseInfos
  def updateDependencyGraphId(phraseId: Long, dependencyGraphId: UUID): Insert =
    updateDependencyGraphIdQuery(phraseId).update(Some(dependencyGraphId))
  def getByPhraseAndLocationId(id: Long, locationId: Long): Single[PhraseInfo] =
    getByPkQuery((id, locationId)).result.headOption
  def getByPhraseId(id: Long): Multi[PhraseInfo] = getByIdQuery(id).result
  def getByDependencyGraphId(id: UUID): Multi[PhraseInfo] = getByDependencyGraphIdQuery(id).result
  def removeById(id: Long): Insert = getByIdQuery(id).delete
  def removeByDependencyGraphId(id: UUID): Insert = getByDependencyGraphIdQuery(id).delete

  private lazy val getByPkQuery = Compiled { (id: Rep[Long], locationId: Rep[Long]) =>
    phraseInfoTableQuery.filter(row => row.id === id && row.locationId === locationId)
  }

  private lazy val getByIdQuery = Compiled { (id: Rep[Long]) =>
    phraseInfoTableQuery.filter(_.id === id)
  }

  private lazy val getByDependencyGraphIdQuery = Compiled { (id: Rep[UUID]) =>
    phraseInfoTableQuery.filter(_.dependencyGraphId === id)
  }

  private lazy val updateDependencyGraphIdQuery = Compiled { (id: Rep[Long]) =>
    phraseInfoTableQuery.filter(_.id === id).map(_.dependencyGraphId)
  }
}
