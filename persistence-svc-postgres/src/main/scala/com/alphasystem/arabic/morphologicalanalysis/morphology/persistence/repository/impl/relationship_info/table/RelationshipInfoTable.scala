package com.alphasystem
package arabic
package morphologicalanalysis
package morphology
package persistence
package repository
package impl
package relationship_info
package table

import morphology.model.RelationshipType
import persistence.model.RelationshipInfo
import slick.lifted.ProvenShape

import java.util.UUID

private[table] trait RelationshipInfoTable extends SlickSupport {

  val jdbcProfile: ExtendedPostgresProfile

  import jdbcProfile.ExtendedApi.*

  private[relationship_info] class RelationshipInfoTable(tag: Tag)
      extends Table[RelationshipInfo](tag, "relationship_info") {

    lazy val id: Rep[Long] = column("id", O.PrimaryKey)
    lazy val text: Rep[String] = column("relationship_text")
    lazy val relationshipType: Rep[RelationshipType] = column("relationship_type")
    lazy val ownerLocationId: Rep[Option[Long]] = column("owner_location_id")
    lazy val ownerPhraseId: Rep[Option[Long]] = column("owner_phrase_id")
    lazy val dependentLocationId: Rep[Option[Long]] = column("dependent_location_id")
    lazy val dependentPhraseId: Rep[Option[Long]] = column("dependent_phrase_id")
    lazy val dependencyGraphId: Rep[Option[UUID]] = column("dependency_graph_id")

    override def * : ProvenShape[RelationshipInfo] = (
      id,
      text,
      relationshipType,
      ownerLocationId,
      ownerPhraseId,
      dependentLocationId,
      dependentPhraseId,
      dependencyGraphId
    ) <> (RelationshipInfo.apply.tupled, RelationshipInfo.unapply)
  }

  protected lazy val relationshipInfoTableQuery: TableQuery[RelationshipInfoTable] = TableQuery[RelationshipInfoTable]
}
