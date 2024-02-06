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

private[phrase_info] trait PhraseInfoTableRepository extends PhraseInfoTable {

  import jdbcProfile.api.*

  def insertOrUpdate(phraseInfo: PhraseInfo): Insert = phraseInfoTableQuery.insertOrUpdate(phraseInfo)
  def getByPhraseAndLocationId(id: UUID, locationId: Long): Single[PhraseInfo] =
    getByPkQuery((id, locationId)).result.headOption
  def getByPhraseId(id: UUID): Multi[PhraseInfo] = getByIdQuery(id).result
  def getByDependencyGraphId(id: UUID): Multi[PhraseInfo] = getByDependencyGraphIdQuery(id).result
  def removeById(id: UUID): Insert = getByIdQuery(id).delete
  def removeByDependencyGraphId(id: UUID): Insert = getByDependencyGraphIdQuery(id).delete

  private lazy val getByPkQuery = Compiled { (id: Rep[UUID], locationId: Rep[Long]) =>
    phraseInfoTableQuery.filter(row => row.id === id && row.locationId === locationId)
  }

  private lazy val getByIdQuery = Compiled { (id: Rep[UUID]) =>
    phraseInfoTableQuery.filter(row => row.id === id)
  }

  private lazy val getByDependencyGraphIdQuery = Compiled { (id: Rep[UUID]) =>
    phraseInfoTableQuery.filter(row => row.dependencyGraphId === id)
  }
}
