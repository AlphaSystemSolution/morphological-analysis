package com.alphasystem.arabic.morphologicalanalysis.morphology.persistence.repository

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

  override def create(location: Location): Long =
    run(quote(schema.insertValue(lift(toLifted(location)))))

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

  def findByChapterVerseAndToken(
    chapterNumber: Int,
    verseNumber: Int,
    tokenNumber: Int
  ): Seq[Location] =
    findByTokenId(tokenNumber.toTokenId(chapterNumber, verseNumber))

  def findByTokenId(
    tokenId: String
  ): Seq[Location] = {
    inline def q = quote(schema.filter(e => e.tokenId == lift(tokenId)))
    runQuery(q).map(decodeDocument)
  }

  override protected def runQuery(
    q: Quoted[EntityQuery[LocationLifted]]
  ): Seq[LocationLifted] = run(q)

  private def toLifted(location: Location) =
    LocationLifted(
      location.id,
      location.tokenId,
      location.asJson.noSpaces
    )
}

object LocationRepository {

  def apply(dataSource: CloseableDataSource): LocationRepository =
    new LocationRepository(dataSource)
}
