package com.alphasystem.arabic.morphologicalanalysis.morphology.persistence

import com.alphasystem.arabic.morphologicalanalysis.morphology.model.Verse
import com.alphasystem.arabic.morphologicalanalysis.morphology.persistence.*
import com.alphasystem.arabic.morphologicalanalysis.morphology.persistence.model.VerseLifted
import io.getquill.*
import io.getquill.context.*
import io.circe.generic.*
import io.circe.parser.*
import io.circe.generic.auto.*
import io.circe.syntax.*

class VerseRepository(dataSource: CloseableDataSource)
    extends BaseRepository[Verse, VerseLifted](dataSource) {

  import ctx.*

  override protected val schema: Quoted[EntityQuery[VerseLifted]] =
    quote(
      querySchema[VerseLifted](
        "verse",
        _.chapterId -> "chapter_id"
      )
    )

  def create(
    chapterId: String,
    verse: Verse
  ): Long =
    run(
      quote(
        schema.insertValue(
          lift(
            VerseLifted(
              verse.id,
              chapterId,
              verse.asJson.noSpaces
            )
          )
        )
      )
    )

  def findByChapterId(
    verseId: String
  ): Seq[Verse] = {
    inline def q = quote(schema.filter(e => e.chapterId == lift(verseId)))
    runQuery(q).map(decodeDocument)
  }

  override protected def runQuery(
    q: Quoted[EntityQuery[VerseLifted]]
  ): Seq[VerseLifted] = run(q)

  override protected def decodeDocument(lifted: VerseLifted): Verse = {
    decode[Verse](lifted.document) match
      case Left(error)  => throw error
      case Right(value) => value
  }
}

object VerseRepository {

  def apply(dataSource: CloseableDataSource): VerseRepository =
    new VerseRepository(dataSource)
}
