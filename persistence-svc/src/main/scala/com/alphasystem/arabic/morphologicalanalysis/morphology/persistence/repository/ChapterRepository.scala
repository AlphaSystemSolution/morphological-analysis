package com.alphasystem
package arabic
package morphologicalanalysis
package morphology
package persistence
package repository

import morphology.model.Chapter
import morphology.persistence.model.Chapter as ChapterLifted
import io.getquill.*
import io.getquill.context.*

class ChapterRepository private (ctx: PostgresJdbcContext[Literal]) {

  import ctx.*

  private val schema: Quoted[EntityQuery[ChapterLifted]] = quote(query[ChapterLifted])

  inline def insert(verse: Chapter): Quoted[Insert[ChapterLifted]] = quote(schema.insertValue(lift(verse.toLifted)))

  inline def findByIdQuery(chapterNumber: Int): Quoted[EntityQuery[ChapterLifted]] =
    quote(schema.filter(_.chapter_number == lift(chapterNumber)))

  inline def findAllQuery: Quoted[EntityQuery[ChapterLifted]] = quote(schema)
}

object ChapterRepository {
  def apply(ctx: PostgresJdbcContext[Literal]): ChapterRepository = new ChapterRepository(ctx)
}
