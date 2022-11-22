package com.alphasystem
package arabic
package morphologicalanalysis
package morphology
package persistence
package repository

import morphology.model.Verse
import morphology.persistence.model.Verse as VerseLifted
import io.getquill.*
import io.getquill.context.*

class VerseRepository private (ctx: PostgresJdbcContext[Literal]) {

  import ctx.*

  private val schema: Quoted[EntityQuery[VerseLifted]] = quote(query[VerseLifted])

  inline def insertAll(verses: Seq[Verse]): Quoted[BatchAction[Insert[VerseLifted]]] =
    quote(liftQuery(verses.map(_.toLifted)).foreach(l => schema.insertValue(l)))

  inline def findByIdQuery(chapterNumber: Int, verseNumber: Int): Quoted[EntityQuery[VerseLifted]] =
    quote(schema.filter(_.chapter_number == lift(chapterNumber)).filter(_.verse_number == lift(verseNumber)))

  inline def findByChapterNumber(chapterNumber: Int): Quoted[EntityQuery[VerseLifted]] =
    quote(schema.filter(_.chapter_number == lift(chapterNumber)))
}

object VerseRepository {
  def apply(ctx: PostgresJdbcContext[Literal]): VerseRepository = new VerseRepository(ctx)
}
