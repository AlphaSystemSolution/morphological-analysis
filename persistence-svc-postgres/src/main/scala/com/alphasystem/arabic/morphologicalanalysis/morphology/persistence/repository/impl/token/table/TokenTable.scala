package com.alphasystem
package arabic
package morphologicalanalysis
package morphology
package persistence
package repository
package impl
package token
package table

import morphology.model.{ NamedTag, WordProperties, WordType }
import persistence.model.{ Location, Token }
import slick.jdbc.JdbcProfile
import slick.lifted.{ PrimaryKey, ProvenShape }

private[table] trait TokenTable extends SlickSupport {

  val jdbcProfile: JdbcProfile

  import jdbcProfile.api.*

  private[table] class TokenTable(tag: Tag) extends Table[Token](tag, "token") {

    lazy val id: Rep[Long] = column("id")
    lazy val chapterNumber: Rep[Int] = column("chapter_number")
    lazy val verseNumber: Rep[Int] = column("verse_number")
    lazy val tokenNumber: Rep[Int] = column("token_number")
    lazy val verseId: Rep[Long] = column("verse_id")
    lazy val tokenText: Rep[String] = column("token_text")
    lazy val derivedText: Rep[String] = column("derived_text")
    lazy val hidden: Rep[Boolean] = column("hidden")
    lazy val translation: Rep[Option[String]] = column("translation")
    lazy val pk: PrimaryKey = primaryKey("pk_token", id)

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

  private[table] class LocationTable(tag: Tag) extends Table[Location](tag, "location") {

    lazy val id: Rep[Long] = column("id")
    lazy val chapterNumber: Rep[Int] = column("chapter_number")
    lazy val verseNumber: Rep[Int] = column("verse_number")
    lazy val tokenNumber: Rep[Int] = column("token_number")
    lazy val locationNumber: Rep[Int] = column("location_number")
    lazy val tokenId: Rep[Long] = column("token_id")
    lazy val hidden: Rep[Boolean] = column("hidden")
    lazy val startIndex: Rep[Int] = column("start_index")
    lazy val endIndex: Rep[Int] = column("end_index")
    lazy val derivedText: Rep[String] = column("derived_text")
    lazy val locationText: Rep[String] = column("location_text")
    lazy val alternateText: Rep[String] = column("alternate_text")
    lazy val wordType: Rep[WordType] = column("word_type")
    lazy val properties: Rep[WordProperties] = column("properties")
    lazy val translation: Rep[Option[String]] = column("translation")
    lazy val namedTag: Rep[Option[NamedTag]] = column("named_tag")
    lazy val pk: PrimaryKey = primaryKey("pk_location", id)
    lazy val tokens = foreignKey("fk_token_id", tokenId, tokenTableQuery)(_.id)

    override def * : ProvenShape[Location] = {
      (
        id,
        chapterNumber,
        verseNumber,
        tokenNumber,
        locationNumber,
        tokenId,
        hidden,
        startIndex,
        endIndex,
        derivedText,
        locationText,
        alternateText,
        wordType,
        properties,
        translation,
        namedTag
      ) <> ((Location.apply _).tupled, Location.unapply)
    }
  }

  protected lazy val tokenTableQuery: TableQuery[TokenTable] = TableQuery[TokenTable]
  protected lazy val locationTableQuery: TableQuery[LocationTable] = TableQuery[LocationTable]
}
