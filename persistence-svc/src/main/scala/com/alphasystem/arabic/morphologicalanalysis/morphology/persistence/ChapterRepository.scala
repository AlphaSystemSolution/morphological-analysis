package com.alphasystem.arabic.morphologicalanalysis.morphology.persistence

import com.alphasystem.arabic.morphologicalanalysis.morphology.model.Chapter
import com.alphasystem.arabic.morphologicalanalysis.morphology.persistence.*
import com.alphasystem.arabic.morphologicalanalysis.morphology.persistence.model.ChapterLifted
import io.circe.generic.*
import io.circe.generic.auto.*
import io.circe.parser.*
import io.circe.syntax.*
import io.getquill.*
import io.getquill.context.*

class ChapterRepository(dataSource: CloseableDataSource)
    extends BaseRepository[Chapter, ChapterLifted](dataSource) {

  import ctx.*

  override protected val schema: Quoted[EntityQuery[ChapterLifted]] =
    quote(
      querySchema[ChapterLifted](
        "chapter"
      )
    )

  def create(
    chapter: Chapter
  ): Long =
    run(
      quote(
        schema.insertValue(
          lift(
            ChapterLifted(
              chapter.id,
              chapter.asJson.noSpaces
            )
          )
        )
      )
    )

  override protected def runQuery(
    q: Quoted[EntityQuery[ChapterLifted]]
  ): Seq[ChapterLifted] = run(q)

  protected def decodeDocument(lifted: ChapterLifted): Chapter = {
    decode[Chapter](lifted.document) match
      case Left(error)  => throw error
      case Right(value) => value
  }
}

object ChapterRepository {

  def apply(dataSource: CloseableDataSource): ChapterRepository =
    new ChapterRepository(dataSource)
}
