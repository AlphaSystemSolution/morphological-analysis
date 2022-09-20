package com.alphasystem.arabic.morphologicalanalysis.morphology.persistence

import com.alphasystem.arabic.morphologicalanalysis.morphology.model.Location
import com.alphasystem.arabic.morphologicalanalysis.morphology.persistence.*
import com.alphasystem.arabic.morphologicalanalysis.morphology.persistence.model.LocationLifted
import io.circe.generic.auto.*
import io.circe.syntax.*
import io.getquill.*
import io.getquill.context.*

class LocationRepository(dataSource: CloseableDataSource)
    extends BaseRepository[Location, LocationLifted](dataSource) {

  import ctx.*

  override protected val schema: Quoted[EntityQuery[LocationLifted]] =
    quote(
      querySchema[LocationLifted](
        "location",
        _.tokenId -> "token_id"
      )
    )

  def create(
    tokenId: String,
    location: Location
  ): Long = createLocation(tokenId, location)

  def findByTokenId(
    tokenId: String
  ): Seq[Location] = {
    inline def q = quote(schema.filter(e => e.tokenId == lift(tokenId)))
    runQuery(q).map(decodeDocument)
  }

  override protected def runQuery(
    q: Quoted[EntityQuery[LocationLifted]]
  ): Seq[LocationLifted] = run(q)

  private def createLocation(
    tokenId: String,
    location: Location
  ) = {
    run(
      quote(
        schema.insertValue(
          lift(
            LocationLifted(
              location.id,
              tokenId,
              location.asJson.noSpaces
            )
          )
        )
      )
    )
  }
}

object LocationRepository {

  def apply(dataSource: CloseableDataSource): LocationRepository =
    new LocationRepository(dataSource)
}
