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
import slick.dbio.DBIO
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

  override def createPhraseInfo(phraseInfo: PhraseInfo): Future[Done] =
    executor
      .exec(DBIO.sequence(phraseInfo.toLifted.map(repository.insertOrUpdate)))
      .map(_ => Done)

  override def findById(id: UUID): Future[Option[PhraseInfo]] =
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
