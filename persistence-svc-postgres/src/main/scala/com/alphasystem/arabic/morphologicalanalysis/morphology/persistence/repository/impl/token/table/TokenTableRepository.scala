package com.alphasystem
package arabic
package morphologicalanalysis
package morphology
package persistence
package repository
package impl
package token
package table

import persistence.model.{ Location, Token }

private[token] trait TokenTableRepository extends TokenTable {

  import jdbcProfile.api.*

  def createTokens(tokens: Seq[Token]): MultiInsert = tokenTableQuery ++= tokens
  def insertOrUpdateToken(token: Token): Insert = tokenTableQuery.insertOrUpdate(token)
  def findTokenById(tokenId: Long): Single[Token] = getTokenByIdQuery(tokenId).result.headOption
  def findTokensByVerseId(verseId: Long): Multi[Token] = getTokensByVerseIdQuery(verseId).result
  def removeTokensByVerseId(verseId: Long): Insert = getTokensByVerseIdQuery(verseId).delete

  private lazy val getTokenByIdQuery = Compiled { (id: Rep[Long]) =>
    tokenTableQuery.filter(row => row.id === id)
  }

  private lazy val getTokensByVerseIdQuery = Compiled { (verseId: Rep[Long]) =>
    tokenTableQuery.filter(row => row.verseId === verseId).sortBy(_.tokenNumber)
  }
}
