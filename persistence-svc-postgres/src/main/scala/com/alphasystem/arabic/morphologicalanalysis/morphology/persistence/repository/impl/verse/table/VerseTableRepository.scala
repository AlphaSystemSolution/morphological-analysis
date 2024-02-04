package com.alphasystem
package arabic
package morphologicalanalysis
package morphology
package persistence
package repository
package impl
package verse
package table

import model.Verse

private[verse] trait VerseTableRepository extends VerseTable {

  import jdbcProfile.api.*

  def addVerses(verses: Seq[Verse]): MultiInsert = verseTableQuery ++= verses

  def addOrUpdateVerse(verse: Verse): Insert = verseTableQuery.insertOrUpdate(verse)

  def getById(id: Long): Single[Verse] = getByIdQuery(id).result.headOption

  def getByChapterNumber(chapterNumber: Int): Multi[Verse] = getByChapterNumberQuery(chapterNumber).result

  private lazy val getByIdQuery = Compiled { (id: Rep[Long]) =>
    verseTableQuery.filter(row => row.id === id)
  }

  private lazy val getByChapterNumberQuery = Compiled { (chapterNumber: Rep[Int]) =>
    verseTableQuery.filter(row => row.chapterNumber === chapterNumber).sortBy(_.verseNumber)
  }

}
