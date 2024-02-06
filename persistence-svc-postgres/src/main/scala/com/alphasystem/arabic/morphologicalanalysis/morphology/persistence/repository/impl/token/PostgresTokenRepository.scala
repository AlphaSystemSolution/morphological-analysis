package com.alphasystem
package arabic
package morphologicalanalysis
package morphology
package persistence
package repository
package impl
package token

import persistence.model.Location
import impl.location.table.LocationTableRepository
import morphology.model.Token
import slick.dbio.{ DBIO, DBIOAction, NoStream }
import token.table.TokenTableRepository
import slick.jdbc.JdbcBackend.Database
import slick.jdbc.JdbcProfile

import scala.collection.immutable
import scala.concurrent.{ ExecutionContext, Future }

class PostgresTokenRepository private[impl] (executor: JdbcExecutorFactory#JdbcExecutor)(implicit ec: ExecutionContext)
    extends TokenRepository {

  private val tokenRepository = new TokenTableRepository {
    override val jdbcProfile: JdbcProfile = PostgresProfileWrapper.profile
  }

  private val locationRepository = new LocationTableRepository {
    override val jdbcProfile: JdbcProfile = PostgresProfileWrapper.profile
  }

  override def createTokens(tokens: Seq[Token]): Future[Done] =
    executor
      .exec(
        DBIO
          .seq(
            tokenRepository.createTokens(tokens.map(_.toLifted)),
            locationRepository.createLocations(tokens.flatMap(_.locations).map(_.toLifted))
          )
          .withPinnedSession
      )
      .map(_ => Done)

  override def updateToken(token: Token): Future[Done] =
    executor
      .exec(
        DBIO
          .seq(
            tokenRepository.insertOrUpdateToken(token.toLifted),
            DBIO.sequence(token.locations.map(_.toLifted).map(locationRepository.insertOrUpdateLocation))
          )
          .withPinnedSession
      )
      .map(_ => Done)

  override def findTokenById(tokenId: Long): Future[Option[Token]] =
    executor
      // TODO: make one to many relationship work
      .exec(tokenRepository.findTokenById(tokenId).zip(locationRepository.findLocationsByTokenId(tokenId)))
      .map { case (maybeToken, locations) =>
        maybeToken.map(_.toEntity).map(_.copy(locations = locations.map(_.toEntity)))
      }

  override def findTokensByVerseId(verseId: Long): Future[Seq[Token]] = {
    // TODO: make one to many relationship work
    val action = tokenRepository.findTokensByVerseId(verseId).zip(locationRepository.findLocationsByVerseId(verseId))
    executor.exec(action).map { case (tokenLifted, locationsLifted) =>
      val locationsMap = locationsLifted.groupBy(_.tokenId)
      tokenLifted.map(_.toEntity).map { token =>
        val locations = locationsMap.getOrElse(token.id, Seq.empty[Location]).map(_.toEntity).sortBy(_.tokenNumber)
        token.copy(locations = locations)
      }
    }
  }

  override def removeTokensByVerseId(verseId: Long): Future[Done] =
    executor.exec(tokenRepository.removeTokensByVerseId(verseId)).map(_ => Done)
}

object TokenRepository {
  def apply(db: Database)(implicit ec: ExecutionContext): TokenRepository = {
    val executor = new JdbcExecutorFactory(PostgresProfileWrapper)(db)
    new PostgresTokenRepository(executor)
  }
}
