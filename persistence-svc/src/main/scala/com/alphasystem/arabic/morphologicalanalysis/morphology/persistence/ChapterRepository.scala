package com.alphasystem.arabic.morphologicalanalysis.morphology.persistence

import com.alphasystem.arabic.morphologicalanalysis.morphology.model.*
import com.alphasystem.arabic.morphologicalanalysis.morphology.persistence.*
import com.alphasystem.arabic.morphologicalanalysis.morphology.persistence.model.ChapterLifted
import io.circe.generic.auto.*
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

  override def create(chapter: Chapter): Long =
    run(
      quote(
        schema.insertValue(
          lift(toLifted(chapter))
        )
      )
    )

  override def bulkCreate(entities: List[Chapter]): Unit = {
    inline def query = quote {
      liftQuery(entities.map(toLifted)).foreach { c =>
        querySchema[ChapterLifted](
          "chapter"
        ).insertValue(c)
      }
    }

    run(query)
  }

  def findByChapterNumber(chapterNumber: Int): Option[Chapter] =
    findById(chapterNumber.toChapterId)

  def findAll: Seq[Chapter] = {
    inline def query = quote(schema)
    runQuery(query).map(decodeDocument)
  }

  override protected def runQuery(
    q: Quoted[EntityQuery[ChapterLifted]]
  ): Seq[ChapterLifted] = run(q)

  private def toLifted(chapter: Chapter) =
    ChapterLifted(
      chapter.id,
      chapter.asJson.noSpaces
    )
}

object ChapterRepository {

  def apply(dataSource: CloseableDataSource): ChapterRepository =
    new ChapterRepository(dataSource)
}
