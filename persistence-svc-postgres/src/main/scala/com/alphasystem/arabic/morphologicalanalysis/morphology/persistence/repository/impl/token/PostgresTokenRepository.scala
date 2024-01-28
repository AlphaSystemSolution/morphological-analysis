package com.alphasystem
package arabic
package morphologicalanalysis
package morphology
package persistence
package repository
package impl
package token

import morphology.model.Token
import token.table.TokenTableRepository
import slick.jdbc.JdbcBackend.Database
import slick.jdbc.JdbcProfile

import scala.concurrent.{ ExecutionContext, Future }

class PostgresTokenRepository private[impl] (executor: JdbcExecutorFactory#JdbcExecutor)(implicit ec: ExecutionContext)
    extends TokenRepository {

  private val repository = new TokenTableRepository {
    override val jdbcProfile: JdbcProfile = PostgresProfileWrapper.profile
  }

  override def createTokens(tokens: Seq[Token]): Future[Done] =
    executor.exec(repository.createTokens(tokens.map(_.toLifted)).map(_ => Done))

  override def updateToken(token: Token): Future[Done] =
    executor.exec(repository.insertOrUpdateToken(token.toLifted)).map(_ => Done)

  override def findTokenById(tokenId: Long): Future[Option[Token]] =
    executor.exec(repository.findTokenById(tokenId)).map(_.map(_.toEntity))

  override def findTokensByVerseId(verseId: Long): Future[Seq[Token]] =
    executor.exec(repository.findTokensByVerseId(verseId)).map(_.map(_.toEntity))

  override def removeTokensByVerseId(verseId: Long): Future[Done] =
    executor.exec(repository.removeTokensByVerseId(verseId)).map(_ => Done)
}

object TokenRepository {
  def apply(db: Database)(implicit ec: ExecutionContext): TokenRepository = {
    val executor = new JdbcExecutorFactory(PostgresProfileWrapper)(db)
    new PostgresTokenRepository(executor)
  }
}
