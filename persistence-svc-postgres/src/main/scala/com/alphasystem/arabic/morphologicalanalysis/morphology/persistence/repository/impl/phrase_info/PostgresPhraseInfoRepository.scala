package com.alphasystem
package arabic
package morphologicalanalysis
package morphology
package persistence
package repository
package impl
package phrase_info

import morphology.graph.model.PhraseInfo
import morphology.persistence.model.{ PhraseLocationRelation, PhraseInfo as PhraseInfoLifted }
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

  override def createPhraseInfo(phraseInfo: PhraseInfo): Future[PhraseInfo] =
    executor.exec(repository.sequenceQuery).flatMap { id =>
      val updatedInfo = phraseInfo.copy(id = id)
      val (phraseInfoLifted, relations) = updatedInfo.toLifted
      val action =
        (for {
          _ <- repository.insertPhraseInfo(phraseInfoLifted)
          _ <- repository.insertRelationships(relations)
        } yield Done).withPinnedSession
      executor.exec(action).map(_ => updatedInfo)
    }

  override def updateDependencyGraphId(phraseId: Long, dependencyGraphId: UUID): Future[Done] = {
    val action =
      (for {
        _ <- repository.updateDependencyGraphId(phraseId, dependencyGraphId)
        _ <- repository.updateRelationsDependencyGraphId(phraseId, dependencyGraphId)
      } yield Done).withPinnedSession
    executor.exec(action)
  }

  override def findById(id: Long): Future[Option[PhraseInfo]] = {
    val action =
      (for {
        phraseInfo <- repository.getByPhraseId(id)
        relations <- repository.getRelationsByPhraseId(id)
        result = mergeToSinglePhraseInfo(phraseInfo)(relations)
      } yield result).withPinnedSession
    executor.exec(action)
  }

  override def findByDependencyGraphId(id: UUID): Future[Seq[PhraseInfo]] = {
    val action =
      (for {
        phraseInfos <- repository.getByDependencyGraphId(id)
        relations <- repository.getByRelationsDependencyGraphId(id)
        result = mergePhraseInfo(phraseInfos, relations)
      } yield result).withPinnedSession
    executor.exec(action)
  }

  private def mergePhraseInfo(phraseInfos: Seq[PhraseInfoLifted], values: Seq[PhraseLocationRelation]) = {
    val infoMap = phraseInfos.groupBy(_.id)
    values
      .groupBy(_.phraseId)
      .values
      .flatMap { relations =>
        val pi = relations.headOption.map(_.phraseId).flatMap(i => infoMap.get(i).flatMap(_.headOption))
        mergeToSinglePhraseInfo(pi)(relations)
      }
      .toSeq
  }

  /*
   * This function assumes that all PhraseInfos have same ids.
   */
  private def mergeToSinglePhraseInfo(phraseInfo: Option[PhraseInfoLifted])(values: Seq[PhraseLocationRelation]) =
    if values.isEmpty then phraseInfo.map(_.toEntity())
    else {
      val locations = values.map(phraseInfo => (phraseInfo.locationId, phraseInfo.locationNumber)).sortBy(_._2)
      phraseInfo.map(_.toEntity(locations))
    }
}

object PhraseInfoRepository {
  def apply(db: Database)(implicit ec: ExecutionContext): PhraseInfoRepository = {
    val executor = new JdbcExecutorFactory(PostgresProfileWrapper)(db)
    new PostgresPhraseInfoRepository(executor)
  }
}
