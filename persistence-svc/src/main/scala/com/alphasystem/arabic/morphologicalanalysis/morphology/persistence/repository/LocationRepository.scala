package com.alphasystem
package arabic
package morphologicalanalysis
package morphology
package persistence
package repository

import com.alphasystem.arabic.morphologicalanalysis.morphology.model.*
import com.alphasystem.arabic.morphologicalanalysis.morphology.persistence.*
import com.alphasystem.arabic.morphologicalanalysis.morphology.persistence.model.LocationLifted
import io.circe.generic.auto.*
import io.circe.syntax.*
import io.getquill.*
import io.getquill.context.*

class LocationRepository(dataSource: CloseableDataSource) extends BaseRepository[Location, LocationLifted](dataSource) {

  import ctx.*

  override protected val schema: Quoted[EntityQuery[LocationLifted]] =
    quote(
      querySchema[LocationLifted](
        "location",
        _.tokenId -> "token_id"
      )
    )

  override def create(location: Location): Long = run(quote(schema.insertValue(lift(toLifted(location)))))

  override def bulkCreate(entities: List[Location]): Unit = {
    inline def query = quote {
      liftQuery(entities.map(toLifted)).foreach { c =>
        querySchema[LocationLifted](
          "location",
          _.tokenId -> "token_id"
        ).insertValue(c)
      }
    }

    run(query)
  }

  def findByChapterVerseAndToken(chapterNumber: Int, verseNumber: Int, tokenNumber: Int): Seq[Location] =
    findByTokenId(tokenNumber.toTokenId(chapterNumber, verseNumber))

  def findByTokenId(tokenId: String): Seq[Location] = runQuery(findByTokenIdQuery(tokenId)).map(decodeDocument)

  def deleteByChapterVerseAndToken(chapterNumber: Int, verseNumber: Int, tokenNumber: Int): Unit =
    ctx.run(findByTokenIdQuery(tokenNumber.toTokenId(chapterNumber, verseNumber)).delete)

  override protected def runQuery(q: Quoted[EntityQuery[LocationLifted]]): Seq[LocationLifted] = run(q)

  private def toLifted(location: Location) = LocationLifted(location.id, location.tokenId, location.asJson.noSpaces)

  private inline def findByTokenIdQuery(tokenId: String) = quote(schema.filter(e => e.tokenId == lift(tokenId)))
}

object LocationRepository {

  def apply(dataSource: CloseableDataSource): LocationRepository =
    new LocationRepository(dataSource)
}
