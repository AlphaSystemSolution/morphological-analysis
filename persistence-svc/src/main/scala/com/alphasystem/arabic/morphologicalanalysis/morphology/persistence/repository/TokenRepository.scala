package com.alphasystem
package arabic
package morphologicalanalysis
package morphology
package persistence
package repository

import morphology.persistence.model.Token as TokenLifted
import morphology.model.{ Entity, Token, TokenId }
import io.getquill.*
import io.getquill.context.*

class TokenRepository private (ctx: PostgresJdbcContext[Literal])
    extends BaseRepository2[TokenId, Token, TokenLifted](ctx) {

  import ctx.*

  override protected val schema: Quoted[EntityQuery[TokenLifted]] = quote(query[TokenLifted])

  inline def insertAll(tokens: Seq[Token]): Quoted[BatchAction[Insert[TokenLifted]]] =
    quote(liftQuery(tokens.map(_.toLifted)).foreach(l => schema.insertValue(l)))

  inline def findByChapterAndVerseNumber(chapterNumber: Int, verseNumber: Int): Quoted[EntityQuery[TokenLifted]] =
    quote(schema.filter(_.chapter_number == lift(chapterNumber)).filter(_.verse_number == lift(verseNumber)))

  def update(token: Token): Quoted[Update[TokenLifted]] =
    quote(
      schema
        .filter(_.chapter_number == lift(token.chapterNumber))
        .filter(_.verse_number == lift(token.verseNumber))
        .filter(_.token_number == lift(token.tokenNumber))
        .update(_.translation -> lift(token.translation))
    )

  override protected def toLifted(entity: Token): TokenLifted = entity.toLifted
}

object TokenRepository {
  def apply(ctx: PostgresJdbcContext[Literal]): TokenRepository = new TokenRepository(ctx)
}
