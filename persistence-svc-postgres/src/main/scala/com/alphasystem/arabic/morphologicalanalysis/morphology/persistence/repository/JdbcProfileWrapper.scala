package com.alphasystem
package arabic
package morphologicalanalysis
package morphology
package persistence
package repository

import org.postgresql.util.{ PSQLException, PSQLState }
import org.slf4j.{ Logger, LoggerFactory }
import slick.jdbc.{ JdbcProfile, PostgresProfile }
import slick.jdbc.JdbcBackend.Database

import scala.concurrent.{ ExecutionContext, Future }
import scala.util.control.NonFatal

trait JdbcProfileWrapper {

  val profile: JdbcProfile

  val logger: Logger = LoggerFactory.getLogger(getClass)

  def exceptionHandler[T]: PartialFunction[Throwable, Future[T]] = Map.empty
}

class JdbcExecutorFactory(val base: JdbcProfileWrapper) {
  self =>

  class JdbcExecutor(val db: Database) {

    val base: JdbcProfileWrapper = self.base

    import base.profile.api.*

    def exec[T](
      action: DBIOAction[T, NoStream, Nothing]
    )(implicit
      ec: ExecutionContext
    ): Future[T] =
      db.run(action)
        .recoverWith(base.exceptionHandler)
        .recoverWith { case NonFatal(e) =>
          base.logger.error("Error encountered during repository operation", e)
          Future.failed(DatabaseQueryException(e))
        }
  }

  def apply(db: Database) = new JdbcExecutor(db)
}

trait PostgresProfileWrapper extends JdbcProfileWrapper {

  val profile = PostgresProfile

  override def exceptionHandler[T]: PartialFunction[Throwable, Future[T]] = {
    case e: PSQLException if PSQLState.isConnectionError(e.getSQLState) =>
      logger.error(
        "Encountered a connection error while accessing the repository",
        e
      )
      Future.failed(DatabaseConnectionException(e))
  }
}

object PostgresProfileWrapper extends PostgresProfileWrapper

case class DatabaseConnectionException(cause: Throwable) extends Exception(cause)
case class DatabaseQueryException(cause: Throwable) extends Exception(cause)
