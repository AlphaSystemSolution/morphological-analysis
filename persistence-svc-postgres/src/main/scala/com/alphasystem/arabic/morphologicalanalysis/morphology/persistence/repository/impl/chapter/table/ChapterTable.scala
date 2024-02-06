package com.alphasystem
package arabic
package morphologicalanalysis
package morphology
package persistence
package repository
package impl
package chapter
package table

import morphology.model.Chapter
import slick.jdbc.JdbcProfile
import slick.lifted.ProvenShape

private[table] trait ChapterTable {

  val jdbcProfile: JdbcProfile

  import jdbcProfile.api.*

  private[chapter] class ChapterTable(tag: Tag) extends Table[Chapter](tag, "chapter") {

    lazy val chapterNumber: Rep[Int] = column("chapter_number", O.PrimaryKey)
    lazy val chapterName: Rep[String] = column("chapter_name")
    lazy val verseCount: Rep[Int] = column("verse_count")

    override def * : ProvenShape[Chapter] =
      (chapterName, chapterNumber, verseCount) <> ((Chapter.apply _).tupled, Chapter.unapply)
  }

  protected lazy val chapterTableQuery: TableQuery[ChapterTable] = TableQuery[ChapterTable]
}
