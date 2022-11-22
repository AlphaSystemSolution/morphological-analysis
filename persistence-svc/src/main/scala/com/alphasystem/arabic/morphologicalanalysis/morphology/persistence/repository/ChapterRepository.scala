package com.alphasystem
package arabic
package morphologicalanalysis
package morphology
package persistence
package repository

import morphology.persistence.model.Chapter as ChapterLifted
import morphology.model.{ Chapter, Entity }
import io.getquill.*
import io.getquill.context.*

class ChapterRepository private (ctx: PostgresJdbcContext[Literal])
    extends BaseRepository2[Int, Chapter, ChapterLifted](ctx) {

  import ctx.*

  override protected val schema: Quoted[EntityQuery[ChapterLifted]] = quote(query[ChapterLifted])

  inline def insert(verse: Chapter): Quoted[Insert[ChapterLifted]] = quote(schema.insertValue(lift(verse.toLifted)))

  inline def findByIdQuery(chapterNumber: Int): Quoted[EntityQuery[ChapterLifted]] =
    quote(schema.filter(e => e.chapter_number == lift(chapterNumber)))

  inline def findAllQuery: Quoted[EntityQuery[ChapterLifted]] = quote(schema)

  override protected def toLifted(entity: Chapter): ChapterLifted = entity.toLifted
}

object ChapterRepository {
  def apply(ctx: PostgresJdbcContext[Literal]): ChapterRepository = new ChapterRepository(ctx)
}
