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
  def getById(id: UUID): Single[PhraseInfo] = getByIdQuery(id).result.headOption
  def getByDependencyGraphId(id: UUID): Multi[PhraseInfo] = getByDependencyGraphIdQuery(id).result
  def removeById(id: UUID): Insert = getByIdQuery(id).delete
  def removeByDependencyGraphId(id: UUID): Insert = getByDependencyGraphIdQuery(id).delete

  private lazy val getByIdQuery = Compiled { (id: Rep[UUID]) =>
    phraseInfoTableQuery.filter(row => row.id === id)
  }

  private lazy val getByDependencyGraphIdQuery = Compiled { (id: Rep[UUID]) =>
    phraseInfoTableQuery.filter(row => row.dependencyGraphId === id)
  }
}
