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

private[chapter] trait ChapterTableRepository extends ChapterTable {

  import jdbcProfile.api.*

  def addOrUpdateChapter(chapter: Chapter): Insert = chapterTableQuery.insertOrUpdate(chapter)

  def getByChapterNumber(chapterNumber: Int): Single[Chapter] = getByChapterNumberQuery(chapterNumber).result.headOption

  def getByChapterName(chapterName: String): Single[Chapter] = getByChapterNameQuery(chapterName).result.headOption

  def findAll: Multi[Chapter] = chapterTableQuery.sortBy(_.chapterNumber).result

  private lazy val getByChapterNumberQuery = Compiled { (chapterNumber: Rep[Int]) =>
    chapterTableQuery.filter(row => row.chapterNumber === chapterNumber)
  }

  private lazy val getByChapterNameQuery = Compiled { (chapterName: Rep[String]) =>
    chapterTableQuery.filter(row => row.chapterName === chapterName)
  }
}
