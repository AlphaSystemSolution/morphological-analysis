package com.alphasystem
package arabic
package morphologicalanalysis
package morphology
package persistence
package repository
package impl
package verse
package table

import persistence.model.Verse
import slick.jdbc.JdbcProfile
import slick.lifted.{ PrimaryKey, ProvenShape }

private[table] trait VerseTable {

  val jdbcProfile: JdbcProfile

  import jdbcProfile.api.*

  private[table] class VerseTable(tag: Tag) extends Table[Verse](tag, "verse") {

    lazy val id: Rep[Long] = column("id")
    lazy val chapterNumber: Rep[Int] = column("chapter_number")
    lazy val verseNumber: Rep[Int] = column("verse_number")
    lazy val tokenCount: Rep[Int] = column("token_count")
    lazy val verseText: Rep[String] = column("verse_text")
    lazy val pk: PrimaryKey = primaryKey("pk_verse", id)

    override def * : ProvenShape[Verse] =
      (id, chapterNumber, verseNumber, tokenCount, verseText) <> (Verse.apply.tupled, Verse.unapply)
  }

  protected lazy val verseTableQuery: TableQuery[VerseTable] = TableQuery[VerseTable]
}
