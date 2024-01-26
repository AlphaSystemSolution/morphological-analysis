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
import scala.util.Try
import scala.util.control.NonFatal

trait Repository {

  protected val logger: Logger = LoggerFactory.getLogger(getClass)
  protected val driver: JdbcProfile

  protected val db: Database

  private def close(): Unit = Try(db.close)

  Runtime.getRuntime.addShutdownHook(new Thread(() => close()))

  protected def doRepoCall[T](
    call: => Future[T]
  )(implicit
    ec: ExecutionContext
  ): Future[T] =
    call.recoverWith {
      case e: PSQLException if PSQLState.isConnectionError(e.getSQLState) =>
        logger.error(
          "Encountered a connection error while accessing the repository",
          e
        )
        Future.failed(DatabaseConnectionException(e))
      case NonFatal(e) =>
        logger.error(
          "Encountered an unknown error while accessing the repository",
          e
        )
        Future.failed(DatabaseQueryException(e))
    }
}

trait PostgresDatabase extends Repository {
  override protected val driver: JdbcProfile = PostgresProfile
}

case class DatabaseConnectionException(cause: Throwable) extends Exception(cause)
case class DatabaseQueryException(cause: Throwable) extends Exception(cause)
