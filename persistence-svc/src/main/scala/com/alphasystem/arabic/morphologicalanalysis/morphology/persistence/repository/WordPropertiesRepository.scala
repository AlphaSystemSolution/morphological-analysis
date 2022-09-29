package com.alphasystem.arabic.morphologicalanalysis.morphology.persistence.repository

import com.alphasystem.arabic.morphologicalanalysis.morphology.model.*
import com.alphasystem.arabic.morphologicalanalysis.morphology.persistence.*
import com.alphasystem.arabic.morphologicalanalysis.morphology.persistence.model.PropertiesLifted
import com.alphasystem.morphologicalanalysis.morphology.model.*
import io.circe.generic.auto.*
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

  override def create(properties: WordProperties): Long =
    run(quote(schema.insertValue(lift(toLifted(properties)))))

  override def bulkCreate(entities: List[WordProperties]): Unit = {
    inline def query = quote {
      liftQuery(entities.map(toLifted)).foreach { c =>
        querySchema[PropertiesLifted](
          "word_properties",
          _.locationId -> "location_id"
        ).insertValue(c)
      }
    }

    run(query)
  }

  def findByChapterVerseTokenAndLocation(
    chapterNumber: Int,
    verseNumber: Int,
    tokenNumber: Int,
    locationNumber: Int
  ): Seq[WordProperties] =
    findByLocationId(
      locationNumber.toLocationId(chapterNumber, verseNumber, tokenNumber)
    )

  def findByLocationId(
    id: String
  ): Seq[WordProperties] = {
    inline def q = quote(schema.filter(e => e.locationId == lift(id)))
    runQuery(q).map(decodeDocument)
  }

  override protected def runQuery(
    q: Quoted[EntityQuery[PropertiesLifted]]
  ): Seq[PropertiesLifted] = run(q)

  private def toLifted(properties: WordProperties) =
    PropertiesLifted(
      properties.id,
      properties.locationId,
      properties.asJson.noSpaces
    )
}

object WordPropertiesRepository {

  def apply(dataSource: CloseableDataSource): WordPropertiesRepository =
    new WordPropertiesRepository(
      dataSource
    )
}
