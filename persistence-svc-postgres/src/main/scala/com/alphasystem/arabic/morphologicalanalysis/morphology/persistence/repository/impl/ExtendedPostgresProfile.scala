package com.alphasystem
package arabic
package morphologicalanalysis
package morphology
package persistence
package repository
package impl

import morphology.model.PhraseType
import com.github.tminglei.slickpg.*

trait ExtendedPostgresProfile extends ExPostgresProfile with PgArraySupport {

  // Add back `capabilities.insertOrUpdate` to enable native `upsert` support; for postgres 9.5+
  override protected def computeCapabilities: Set[slick.basic.Capability] =
    super.computeCapabilities + slick.jdbc.JdbcCapabilities.insertOrUpdate

  override val api: ExtPostgresAPI = ExtendedApi

  object ExtendedApi extends ExtPostgresAPI with ArrayImplicits {
    implicit val phraseTypeListTypeWrapper: DriverJdbcType[List[PhraseType]] = new SimpleArrayJdbcType[String]("text")
      .mapTo[PhraseType](PhraseType.valueOf, _.name())
      .to(_.toList)
  }
}

object ExtendedPostgresProfile extends ExtendedPostgresProfile
