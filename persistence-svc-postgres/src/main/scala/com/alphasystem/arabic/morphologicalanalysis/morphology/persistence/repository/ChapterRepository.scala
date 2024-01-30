package com.alphasystem
package arabic
package morphologicalanalysis
package morphology
package persistence
package repository

import morphology.model.Chapter

import scala.concurrent.Future

trait ChapterRepository {

  def addOrUpdateChapter(chapter: Chapter): Future[Done]

  def getByChapterNumber(chapterNumber: Int): Future[Option[Chapter]]

  def findAll: Future[Seq[Chapter]]
}
