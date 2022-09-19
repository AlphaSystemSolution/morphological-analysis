package com.alphasystem.arabic.morphologicalanalysis.morphology.persistence

import com.alphasystem.arabic.morphologicalanalysis.morphology.model.*
import com.alphasystem.arabic.morphologicalanalysis.morphology.persistence.*
import com.alphasystem.arabic.morphologicalanalysis.morphology.persistence.model.PropertiesLifted
import com.alphasystem.morphologicalanalysis.morphology.model.*
import io.circe.generic.*
import io.circe.generic.auto.*
import io.circe.parser.*
import io.circe.syntax.*
import io.getquill.*
import io.getquill.context.*

class WordPropertiesRepository(dataSource: CloseableDataSource)
    extends BaseRepository(dataSource) {

  import ctx.*

  private val schema = quote(
    querySchema[PropertiesLifted](
      "word_properties",
      _.locationId -> "location_id"
    )
  )

  def create(
    locationId: String,
    properties: WordProperties
  ): Long =
    run(
      quote(
        schema.insertValue(
          lift(
            PropertiesLifted(
              properties.id,
              locationId,
              properties.asJson.noSpaces
            )
          )
        )
      )
    )

  def findById(
    id: String
  ): Option[WordProperties] = {
    inline def q = quote(schema.filter(e => e.id == lift(id)))
    run(q).headOption.map(decodeProperties)
  }

  def findByLocationId(
    id: String
  ): Seq[WordProperties] = {
    inline def q = quote(schema.filter(e => e.locationId == lift(id)))
    run(q).map(decodeProperties)
  }

  private def decodeProperties(lifted: PropertiesLifted) =
    decode[WordProperties](lifted.document) match
      case Left(error)  => throw error
      case Right(value) => value

}

object WordPropertiesRepository {

  def apply(dataSource: CloseableDataSource): WordPropertiesRepository =
    new WordPropertiesRepository(
      dataSource
    )
}
