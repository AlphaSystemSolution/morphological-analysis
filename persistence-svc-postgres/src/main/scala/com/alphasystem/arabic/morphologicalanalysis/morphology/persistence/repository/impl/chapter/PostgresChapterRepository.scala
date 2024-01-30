package com.alphasystem
package arabic
package morphologicalanalysis
package morphology
package persistence
package repository
package impl
package chapter

import morphology.model.Chapter
import chapter.table.ChapterTableRepository
import slick.jdbc.JdbcBackend.Database
import slick.jdbc.JdbcProfile

import scala.concurrent.{ ExecutionContext, Future }

class PostgresChapterRepository private[impl] (
  executor: JdbcExecutorFactory#JdbcExecutor
)(implicit ec: ExecutionContext)
    extends ChapterRepository {

  private val repository = new ChapterTableRepository {
    override val jdbcProfile: JdbcProfile = PostgresProfileWrapper.profile
  }

  override def addOrUpdateChapter(chapter: Chapter): Future[Done] =
    executor.exec(repository.addOrUpdateChapter(chapter)).map(_ => Done)

  override def getByChapterNumber(chapterNumber: Int): Future[Option[Chapter]] =
    executor.exec(repository.getByChapterNumber(chapterNumber))

  override def findAll: Future[Seq[Chapter]] = executor.exec(repository.findAll)
}

object ChapterRepository {
  def apply(db: Database)(implicit ec: ExecutionContext): ChapterRepository = {
    val executor = new JdbcExecutorFactory(PostgresProfileWrapper)(db)
    new PostgresChapterRepository(executor)
  }
}
