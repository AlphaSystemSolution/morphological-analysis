package com.alphasystem
package arabic
package morphologicalanalysis
package morphology
package persistence
package repository

import morphology.persistence.model.Location as LocationLifted
import morphology.model.{ Entity, Location, LocationId, TokenId }
import io.getquill.*
import io.getquill.context.*

class LocationRepository private (ctx: PostgresJdbcContext[Literal])
    extends BaseRepository2[LocationId, Location, LocationLifted](ctx) {

  import ctx.*

  override protected val schema: Quoted[EntityQuery[LocationLifted]] = quote(query[LocationLifted])

  inline def insertAll(locations: Seq[Location]): Quoted[BatchAction[Insert[LocationLifted]]] =
    quote(liftQuery(locations.map(_.toLifted)).foreach(l => schema.insertValue(l)))

  inline def findByChapterVerseAndTokenNumber(
    chapterNumber: Int,
    verseNumber: Int,
    tokenNUmber: Int
  ): Quoted[EntityQuery[LocationLifted]] =
    quote(
      schema
        .filter(_.chapter_number == lift(chapterNumber))
        .filter(_.verse_number == lift(verseNumber))
        .filter(_.token_number == lift(tokenNUmber))
    )

  inline def findByChapterAndVerseNumber(
    chapterNumber: Int,
    verseNumber: Int
  ): Quoted[EntityQuery[LocationLifted]] =
    quote(schema.filter(_.chapter_number == lift(chapterNumber)).filter(_.verse_number == lift(verseNumber)))

  inline def findAll(
    chapterIds: Seq[Int],
    verseIds: Seq[Int],
    tokenIds: Seq[Int]
  ): Quoted[EntityQuery[LocationLifted]] = {
    quote(
      schema
        .filter(e => liftQuery(chapterIds).contains(e.chapter_number))
        .filter(e => liftQuery(verseIds).contains(e.verse_number))
        .filter(e => liftQuery(tokenIds).contains(e.token_number))
    )
  }

  override protected def toLifted(entity: Location): LocationLifted = entity.toLifted
}

object LocationRepository {
  def apply(ctx: PostgresJdbcContext[Literal]): LocationRepository = new LocationRepository(ctx)
}
