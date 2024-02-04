package com.alphasystem
package arabic
package morphologicalanalysis
package morphology
package persistence
package repository
package impl
package token
package table

import persistence.model.Token
import slick.jdbc.JdbcProfile
import slick.lifted.ProvenShape

private[table] trait TokenTable extends SlickSupport {

  val jdbcProfile: JdbcProfile

  import jdbcProfile.api.*

  private[table] class TokenTable(tag: Tag) extends Table[Token](tag, "token") {

    lazy val id: Rep[Long] = column("id", O.PrimaryKey)
    lazy val chapterNumber: Rep[Int] = column("chapter_number")
    lazy val verseNumber: Rep[Int] = column("verse_number")
    lazy val tokenNumber: Rep[Int] = column("token_number")
    lazy val verseId: Rep[Long] = column("verse_id")
    lazy val tokenText: Rep[String] = column("token_text")
    lazy val derivedText: Rep[String] = column("derived_text")
    lazy val hidden: Rep[Boolean] = column("hidden")
    lazy val translation: Rep[Option[String]] = column("translation")

    override def * : ProvenShape[Token] =
      (
        id,
        chapterNumber,
        verseNumber,
        tokenNumber,
        verseId,
        tokenText,
        derivedText,
        hidden,
        translation
      ) <> ((Token.apply _).tupled, Token.unapply)
  }

  protected lazy val tokenTableQuery: TableQuery[TokenTable] = TableQuery[TokenTable]
}
