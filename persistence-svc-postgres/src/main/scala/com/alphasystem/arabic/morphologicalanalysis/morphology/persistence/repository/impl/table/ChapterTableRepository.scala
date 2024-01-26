package com.alphasystem
package arabic
package morphologicalanalysis
package morphology
package persistence
package repository
package impl
package table

import morphology.model.Chapter
import slick.sql.{ FixedSqlStreamingAction, SqlAction }

private[impl] trait ChapterTableRepository extends ChapterTable {

  this: Repository =>

  import driver.api.*

  private type Insert = SqlAction[Int, NoStream, Effect.Write]
  private type Single = SqlAction[Option[Chapter], NoStream, Effect.Read]
  private type Multi = FixedSqlStreamingAction[Seq[Chapter], Chapter, Effect.Read]

  def addOrUpdateChapter(chapter: Chapter): Insert = chapterTableQuery.insertOrUpdate(chapter)

  def getByChapterNumber(chapterNumber: Int): Single = getByChapterNumberQuery(chapterNumber).result.headOption

  def getByChapterName(chapterName: String): Single = getByChapterNameQuery(chapterName).result.headOption

  def findAll: Multi = chapterTableQuery.result

  private lazy val getByChapterNumberQuery = Compiled { (chapterNumber: Rep[Int]) =>
    chapterTableQuery.filter(row => row.chapterNumber === chapterNumber)
  }

  private lazy val getByChapterNameQuery = Compiled { (chapterName: Rep[String]) =>
    chapterTableQuery.filter(row => row.chapterName === chapterName)
  }
}
