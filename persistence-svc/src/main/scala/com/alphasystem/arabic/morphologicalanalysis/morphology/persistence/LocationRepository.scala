package com.alphasystem.arabic.morphologicalanalysis.morphology.persistence

import com.alphasystem.arabic.morphologicalanalysis.morphology.model.Location
import com.alphasystem.arabic.morphologicalanalysis.morphology.persistence.*
import com.alphasystem.arabic.morphologicalanalysis.morphology.persistence.model.LocationLifted
import io.getquill.*
import io.getquill.context.*
import io.circe.generic.*
import io.circe.parser.*
import io.circe.generic.auto.*
import io.circe.syntax.*

class LocationRepository(dataSource: CloseableDataSource)
    extends BaseRepository(dataSource) {

  import ctx.*

  private val schema = quote(
    querySchema[LocationLifted](
      "location",
      _.tokenId -> "token_id"
    )
  )

  def create(
    tokenId: String,
    location: Location
  ): Long = createLocation(tokenId, location)

  def findById(
    id: String
  ): Option[Location] = {
    inline def q = quote(schema.filter(e => e.id == lift(id)))
    run(q).headOption.map(decodeDocument)
  }

  def findByTokenId(
    tokenId: String
  ): Seq[Location] = {
    inline def q = quote(schema.filter(e => e.tokenId == lift(tokenId)))
    run(q).map(decodeDocument)
  }

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

  private def decodeDocument(lifted: LocationLifted) = {
    decode[Location](lifted.document) match
      case Left(error)  => throw error
      case Right(value) => value
  }

}

object LocationRepository {

  def apply(dataSource: CloseableDataSource): LocationRepository =
    new LocationRepository(dataSource)
}
