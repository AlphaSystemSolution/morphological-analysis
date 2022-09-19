package com.alphasystem.arabic.morphologicalanalysis.morphology.persistence

import com.alphasystem.arabic.morphologicalanalysis.morphology.model.Token
import com.alphasystem.arabic.morphologicalanalysis.morphology.persistence.*
import com.alphasystem.arabic.morphologicalanalysis.morphology.persistence.model.TokenLifted
import io.getquill.*
import io.getquill.context.*
import io.circe.generic.*
import io.circe.parser.*
import io.circe.generic.auto.*
import io.circe.syntax.*

class TokenRepository(dataSource: CloseableDataSource)
    extends BaseRepository[Token, TokenLifted](dataSource) {

  import ctx.*

  override protected val schema: Quoted[EntityQuery[TokenLifted]] =
    quote(
      querySchema[TokenLifted](
        "token",
        _.verseId -> "verse_id"
      )
    )

  def create(
    verseId: String,
    token: Token
  ): Long =
    run(
      quote(
        schema.insertValue(
          lift(
            TokenLifted(
              token.id,
              verseId,
              token.asJson.noSpaces
            )
          )
        )
      )
    )

  def findByVerseId(
    verseId: String
  ): Seq[Token] = {
    inline def q = quote(schema.filter(e => e.verseId == lift(verseId)))
    runQuery(q).map(decodeDocument)
  }

  override protected def runQuery(
    q: Quoted[EntityQuery[TokenLifted]]
  ): Seq[TokenLifted] = run(q)

  protected def decodeDocument(lifted: TokenLifted): Token = {
    decode[Token](lifted.document) match
      case Left(error)  => throw error
      case Right(value) => value
  }
}

object TokenRepository {

  def apply(dataSource: CloseableDataSource): TokenRepository =
    new TokenRepository(dataSource)
}
