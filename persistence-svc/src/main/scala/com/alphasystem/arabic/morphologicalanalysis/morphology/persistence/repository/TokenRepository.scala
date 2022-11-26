package com.alphasystem
package arabic
package morphologicalanalysis
package morphology
package persistence
package repository

import com.alphasystem.arabic.morphologicalanalysis.morphology.model.*
import morphology.model.Token
import morphology.persistence.model.Token as TokenLifted
import io.getquill.*
import io.getquill.context.*

class TokenRepository private (ctx: PostgresJdbcContext[Literal]) {

  import ctx.*

  private val schema: Quoted[EntityQuery[TokenLifted]] = quote(query[TokenLifted])

  inline def insertAll(tokens: Seq[Token]): Quoted[BatchAction[Insert[TokenLifted]]] =
    quote(liftQuery(tokens.map(_.toLifted)).foreach(l => schema.insertValue(l)))

  inline def findByChapterAndVerseNumber(chapterNumber: Int, verseNumber: Int): Quoted[EntityQuery[TokenLifted]] =
    quote(schema.filter(_.verse_id == lift(verseNumber.toVerseId(chapterNumber))))

  def update(token: Token): Quoted[Update[TokenLifted]] =
    quote(schema.filter(_.id == lift(token._id)).update(_.translation -> lift(token.translation)))
}

object TokenRepository {
  def apply(ctx: PostgresJdbcContext[Literal]): TokenRepository = new TokenRepository(ctx)
}
