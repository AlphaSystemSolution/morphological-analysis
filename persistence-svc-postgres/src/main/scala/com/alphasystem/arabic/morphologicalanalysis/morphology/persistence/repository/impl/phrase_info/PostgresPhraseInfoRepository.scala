package com.alphasystem
package arabic
package morphologicalanalysis
package morphology
package persistence
package repository
package impl
package phrase_info

import morphology.graph.model.PhraseInfo
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

  override def findById(id: UUID): Future[Option[PhraseInfo]] = {
    val action =
      repository.getByPhraseId(id).map { results =>
        if results.isEmpty then None
        else {
          val locations = results.map(phraseInfo => (phraseInfo.locationId, phraseInfo.locationNumber)).sortBy(_._2)
          Some(results.head.toEntity(locations))
        }
      }
    executor.exec(action)
  }

  override def findByDependencyGraphId(id: UUID): Future[Seq[PhraseInfo]] = ???
}

object PhraseInfoRepository {
  def apply(db: Database)(implicit ec: ExecutionContext): PhraseInfoRepository = {
    val executor = new JdbcExecutorFactory(PostgresProfileWrapper)(db)
    new PostgresPhraseInfoRepository(executor)
  }
}
