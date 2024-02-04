package com.alphasystem
package arabic
package morphologicalanalysis
package morphology
package persistence
package repository
package impl
package phrase_info

import morphology.graph.model.PhraseInfo
import impl.location.table.LocationTableRepository
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

  private val locationRepository: LocationTableRepository = new LocationTableRepository {
    override val jdbcProfile: ExtendedPostgresProfile = PostgresProfileWrapper.profile
  }

  override def createPhraseInfo(phraseInfo: PhraseInfo): Future[Done] =
    executor
      .exec(
        DBIO
          .seq(
            repository.insertOrUpdate(phraseInfo.toLifted),
            DBIO.sequence(
              phraseInfo.locations.map(locationId => locationRepository.updatePhraseInfoId(locationId, phraseInfo.id))
            )
          )
          .withPinnedSession
      )
      .map(_ => Done)

  override def findById(id: UUID): Future[Option[PhraseInfo]] = {
    val action = repository.getById(id).zip(locationRepository.findLocationsByPhraseInfoId(id).map(_.map(_.id)))
    executor.exec(action).map { case (maybePhraseInfo, locations) =>
      maybePhraseInfo.map(_.toEntity(locations))
    }
  }

  override def findByDependencyGraphId(id: UUID): Future[Seq[PhraseInfo]] = ???
}

object PhraseInfoRepository {
  def apply(db: Database)(implicit ec: ExecutionContext): PhraseInfoRepository = {
    val executor = new JdbcExecutorFactory(PostgresProfileWrapper)(db)
    new PostgresPhraseInfoRepository(executor)
  }
}
