package com.alphasystem
package arabic
package morphologicalanalysis
package morphology
package persistence
package repository
package impl
package phrase_info
package table

import morphology.model.{ NounStatus, PhraseType }
import persistence.model.{ PhraseInfo, PhraseLocationRelation }
import slick.lifted.ProvenShape

import java.util.UUID

private[table] trait PhraseInfoTable extends SlickSupport {

  val jdbcProfile: ExtendedPostgresProfile

  import jdbcProfile.ExtendedApi.*

  private[phrase_info] class PhraseInfoTable(tag: Tag) extends Table[PhraseInfo](tag, "phrase_info") {

    lazy val id: Rep[Long] = column("id", O.PrimaryKey)
    lazy val text: Rep[String] = column("phrase_text")
    lazy val phraseTypes: Rep[List[PhraseType]] = column("phrase_types")
    lazy val status: Rep[Option[NounStatus]] = column("status")
    lazy val dependencyGraphId: Rep[Option[UUID]] = column("dependency_graph_id")

    override def * : ProvenShape[PhraseInfo] =
      (
        id,
        text,
        phraseTypes,
        status,
        dependencyGraphId
      ) <> ((PhraseInfo.apply _).tupled, PhraseInfo.unapply)
  }

  private[phrase_info] class PhraseLocationRelationTable(tag: Tag)
      extends Table[PhraseLocationRelation](tag, "phrase_location_rln") {

    lazy val phraseId: Rep[Long] = column("phrase_id", O.PrimaryKey)
    lazy val locationId: Rep[Long] = column("location_id", O.PrimaryKey)
    lazy val locationNumber: Rep[Int] = column("location_number")
    lazy val dependencyGraphId: Rep[Option[UUID]] = column("dependency_graph_id")
    override def * : ProvenShape[PhraseLocationRelation] =
      (
        phraseId,
        locationId,
        locationNumber,
        dependencyGraphId
      ) <> ((PhraseLocationRelation.apply _).tupled, PhraseLocationRelation.unapply)
  }

  protected lazy val phraseInfoTableQuery: TableQuery[PhraseInfoTable] = TableQuery[PhraseInfoTable]
  protected lazy val relationsTableQuery: TableQuery[PhraseLocationRelationTable] =
    TableQuery[PhraseLocationRelationTable]
}
