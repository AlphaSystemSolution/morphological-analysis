package com.alphasystem
package arabic
package morphologicalanalysis
package morphology
package persistence
package repository
package impl
package verse

import morphology.model.{ Verse, toVerseId }
import slick.jdbc.JdbcBackend.Database
import verse.table.VerseTableRepository
import slick.jdbc.JdbcProfile

import scala.concurrent.{ ExecutionContext, Future }

class PostgresVerseRepository private[impl] (
  executor: JdbcExecutorFactory#JdbcExecutor
)(implicit ec: ExecutionContext)
    extends VerseRepository {

  private val repository = new VerseTableRepository {
    override val jdbcProfile: JdbcProfile = PostgresProfileWrapper.profile
  }

  override def addVerses(verses: Seq[Verse]): Future[Done] =
    executor.exec(repository.addVerses(verses.map(_.toLifted))).map(_ => Done)

  override def addOrUpdateVerse(verse: Verse): Future[Done] =
    executor.exec(repository.addOrUpdateVerse(verse.toLifted)).map(_ => Done)

  override def getById(id: Long): Future[Option[Verse]] = executor.exec(repository.getById(id)).map(_.map(_.toEntity))

  override def getByChapterAndVerseNumber(chapterNumber: Int, verseNumber: Int): Future[Option[Verse]] =
    getById(verseNumber.toVerseId(chapterNumber))

  override def getByChapterNumber(chapterNumber: Int): Future[Seq[Verse]] =
    executor.exec(repository.getByChapterNumber(chapterNumber)).map(_.map(_.toEntity))
}

object VerseRepository {
  def apply(db: Database)(implicit ec: ExecutionContext): VerseRepository = {
    val executor = new JdbcExecutorFactory(PostgresProfileWrapper)(db)
    new PostgresVerseRepository(executor)
  }
}
