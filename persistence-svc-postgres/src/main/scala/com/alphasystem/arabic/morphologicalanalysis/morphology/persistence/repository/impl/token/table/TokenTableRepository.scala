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
  def createLocations(locations: Seq[Location]): MultiInsert = locationTableQuery ++= locations
  def insertOrUpdateToken(token: Token): Insert = tokenTableQuery.insertOrUpdate(token)
  def insertOrUpdateLocation(location: Location): Insert = locationTableQuery.insertOrUpdate(location)
  def findTokenById(tokenId: Long): Single[Token] = getTokenByIdQuery(tokenId).result.headOption
  def findLocationsByTokenId(tokenId: Long): Multi[Location] = getLocationsByTokenIdQuery(tokenId).result
  def findTokensByVerseId(verseId: Long): Multi[Token] = getTokensByVerseIdQuery(verseId).result
  def findLocationsByVerseId(verseId: Long): Multi[Location] = getLocationsByVerseIdQuery(verseId).result
  def removeTokensByVerseId(verseId: Long): Insert = getTokensByVerseIdQuery(verseId).delete

  private lazy val getTokenByIdQuery = Compiled { (id: Rep[Long]) =>
    tokenTableQuery.filter(row => row.id === id)
  }

  private lazy val getTokensByVerseIdQuery = Compiled { (verseId: Rep[Long]) =>
    tokenTableQuery.filter(row => row.verseId === verseId).sortBy(_.tokenNumber)
  }

  private lazy val getLocationsByTokenIdQuery = Compiled { (tokenId: Rep[Long]) =>
    locationTableQuery.filter(row => row.tokenId === tokenId)
  }

  private lazy val getLocationsByVerseIdQuery = Compiled { (verseId: Rep[Long]) =>
    locationTableQuery.filter(row => row.verseId === verseId)
  }
}
