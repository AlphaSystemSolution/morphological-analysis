package com.alphasystem
package arabic
package morphologicalanalysis
package morphology
package persistence
package repository
package impl
package phrase_info

import morphology.graph.model.PhraseInfo
import morphology.persistence.model.PhraseInfo as PhraseInfoLifted
import slick.jdbc.JdbcBackend.Database
import table.PhraseInfoTableRepository

import java.util.UUID
import scala.concurrent.{ ExecutionContext, Future }

class PostgresPhraseInfoRepository private[impl] (
  executor: JdbcExecutorFactory#JdbcExecutor
)(implicit ec: ExecutionContext)
    extends PhraseInfoRepository {

  private val repository = new PhraseInfoTableRepository {
    override val jdbcProfile: ExtendedPostgresProfile = PostgresProfileWrapper.profile
  }

  override def createPhraseInfo(phraseInfo: PhraseInfo): Future[Long] =
    executor.exec(repository.sequenceQuery).flatMap { id =>
      executor.exec(repository.insert(phraseInfo.copy(id = id).toLifted)).map(_ => id)
    }

  override def updateDependencyGraphId(phraseId: Long, dependencyGraphId: UUID): Future[Done] =
    executor.exec(repository.updateDependencyGraphId(phraseId, dependencyGraphId)).map(_ => Done)

  override def findById(id: Long): Future[Option[PhraseInfo]] =
    executor.exec(repository.getByPhraseId(id).map(mergeToSinglePhraseInfo))

  override def findByDependencyGraphId(id: UUID): Future[Seq[PhraseInfo]] =
    executor.exec(repository.getByDependencyGraphId(id).map(mergePhraseInfo))

  private def mergePhraseInfo(values: Seq[PhraseInfoLifted]) =
    values.groupBy(_.id).values.flatMap(mergeToSinglePhraseInfo).toSeq

  /*
   * This function assumes that all PhraseInfos have same ids.
   */
  private def mergeToSinglePhraseInfo(values: Seq[PhraseInfoLifted]) = {
    if values.isEmpty then None
    else {
      val locations = values.map(phraseInfo => (phraseInfo.locationId, phraseInfo.locationNumber)).sortBy(_._2)
      Some(values.head.toEntity(locations))
    }
  }
}

object PhraseInfoRepository {
  def apply(db: Database)(implicit ec: ExecutionContext): PhraseInfoRepository = {
    val executor = new JdbcExecutorFactory(PostgresProfileWrapper)(db)
    new PostgresPhraseInfoRepository(executor)
  }
}
