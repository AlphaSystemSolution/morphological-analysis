package com.alphasystem.arabic.morphologicalanalysis.morphology.persistence

import com.alphasystem.arabic.morphologicalanalysis.morphology.model.*
import com.alphasystem.arabic.morphologicalanalysis.morphology.persistence.*
import com.alphasystem.arabic.morphologicalanalysis.morphology.persistence.model.TokenLifted
import io.circe.generic.auto.*
import io.circe.syntax.*
import io.getquill.*
import io.getquill.context.*

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

  override def create(token: Token): Long =
    run(quote(schema.insertValue(lift(toLifted(token)))))

  override def bulkCreate(entities: List[Token]): Unit = {
    inline def query = quote {
      liftQuery(entities.map(toLifted)).foreach { c =>
        querySchema[TokenLifted](
          "token",
          _.verseId -> "verse_id"
        ).insertValue(c)
      }
    }

    run(query)
  }

  def findByChapterAndVerse(chapterNumber: Int, verseNumber: Int): Seq[Token] =
    findByVerseId(verseNumber.toVerseId(chapterNumber))

  def findByVerseId(
    verseId: String
  ): Seq[Token] = {
    inline def q = quote(schema.filter(e => e.verseId == lift(verseId)))
    runQuery(q).map(decodeDocument)
  }

  override protected def runQuery(
    q: Quoted[EntityQuery[TokenLifted]]
  ): Seq[TokenLifted] = run(q)

  private def toLifted(token: Token) =
    TokenLifted(
      token.id,
      token.verseId,
      token.asJson.noSpaces
    )
}

object TokenRepository {

  def apply(dataSource: CloseableDataSource): TokenRepository =
    new TokenRepository(dataSource)
}
