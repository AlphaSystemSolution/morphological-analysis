package com.alphasystem
package arabic
package morphologicalanalysis
package morphology
package persistence
package repository
package impl

import morphology.model.Chapter
import impl.table.ChapterTableRepository
import slick.jdbc.JdbcBackend.Database
import slick.jdbc.JdbcProfile

import scala.concurrent.{ ExecutionContext, Future }

class PostgresChapterRepository private[impl] (override protected val db: Database)(implicit ec: ExecutionContext)
    extends PostgresDatabase
    with ChapterRepository {

  private class ChapterTableRepositoryImpl(override protected val db: Database)
      extends PostgresDatabase
      with ChapterTableRepository

  private val repository = new ChapterTableRepositoryImpl(db)

  override def addOrUpdateChapter(chapter: Chapter): Future[Done] =
    doRepoCall(db.run(repository.addOrUpdateChapter(chapter))).map(_ => Done)

  override def getByChapterNumber(chapterNumber: Int): Future[Option[Chapter]] =
    doRepoCall(db.run(repository.getByChapterNumber(chapterNumber)))

  override def findAll: Future[Seq[Chapter]] = doRepoCall(db.run(repository.findAll))
}

object ChapterRepository {
  def apply(db: Database)(implicit ec: ExecutionContext): ChapterRepository = new PostgresChapterRepository(db)
}
