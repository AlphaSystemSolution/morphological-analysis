package com.alphasystem
package arabic
package morphologicalanalysis
package morphology
package persistence
package repository
package impl

import slick.sql.SqlAction

trait SequenceSupport {

  val jdbcProfile: ExtendedPostgresProfile

  import jdbcProfile.api.*

  private[impl] lazy val sequenceQuery: SqlAction[Long, NoStream, Effect] =
    sql"""select nextval('morphological_analysis_sequence');""".as[Long].head
}
