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
    extends BaseRepository[WordProperties, PropertiesLifted](dataSource) {

  import ctx.*

  override protected val schema: Quoted[EntityQuery[PropertiesLifted]] =
    quote(
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

  def findByLocationId(
    id: String
  ): Seq[WordProperties] = {
    inline def q = quote(schema.filter(e => e.locationId == lift(id)))
    run(q).map(decodeDocument)
  }

  override protected def runQuery(
    q: Quoted[EntityQuery[PropertiesLifted]]
  ): Seq[PropertiesLifted] = run(q)

  override protected def decodeDocument(
    lifted: PropertiesLifted
  ): WordProperties =
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
