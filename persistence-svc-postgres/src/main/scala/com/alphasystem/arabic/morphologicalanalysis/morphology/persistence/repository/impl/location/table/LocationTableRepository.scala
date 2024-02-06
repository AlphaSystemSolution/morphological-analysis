package com.alphasystem
package arabic
package morphologicalanalysis
package morphology
package persistence
package repository
package impl
package location
package table

import persistence.model.Location

import java.util.UUID

private[impl] trait LocationTableRepository extends LocationTable {

  import jdbcProfile.api.*

  def createLocations(locations: Seq[Location]): MultiInsert = locationTableQuery ++= locations
  def insertOrUpdateLocation(location: Location): Insert = locationTableQuery.insertOrUpdate(location)
  def findLocationsByTokenId(tokenId: Long): Multi[Location] = getLocationsByTokenIdQuery(tokenId).result
  def findLocationsByVerseId(verseId: Long): Multi[Location] = getLocationsByVerseIdQuery(verseId).result

  private lazy val getLocationsByTokenIdQuery = Compiled { (tokenId: Rep[Long]) =>
    locationTableQuery.filter(_.tokenId === tokenId)
  }

  private lazy val getLocationsByVerseIdQuery = Compiled { (verseId: Rep[Long]) =>
    locationTableQuery.filter(_.verseId === verseId)
  }
}
