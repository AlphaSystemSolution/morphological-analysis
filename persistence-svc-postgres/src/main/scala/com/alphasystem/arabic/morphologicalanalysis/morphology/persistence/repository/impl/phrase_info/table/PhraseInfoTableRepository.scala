package com.alphasystem
package arabic
package morphologicalanalysis
package morphology
package persistence
package repository
package impl
package phrase_info
package table

import persistence.model.{ PhraseInfo, PhraseLocationRelation }

import java.util.UUID
import scala.concurrent.ExecutionContext

private[phrase_info] trait PhraseInfoTableRepository extends PhraseInfoTable with SequenceSupport {

  import jdbcProfile.api.*

  def insertPhraseInfo(phraseInfo: PhraseInfo): Insert = phraseInfoTableQuery += phraseInfo
  def insertRelationships(relations: Seq[PhraseLocationRelation]): MultiInsert = relationsTableQuery ++= relations

  def updateDependencyGraphId(
    phraseId: Long,
    dependencyGraphId: UUID
  )(implicit ec: ExecutionContext
  ): DBIOAction[Int, NoStream, Effect.Write] =
    for {
      _ <- phraseInfoTableQuery.filter(_.id === phraseId).map(_.dependencyGraphId).update(Some(dependencyGraphId))
      r <- relationsTableQuery.filter(_.phraseId === phraseId).map(_.dependencyGraphId).update(Some(dependencyGraphId))
    } yield r

  def getByPhraseId(id: Long): Multi[(PhraseInfo, PhraseLocationRelation)] =
    (for {
      phraseInfo <- phraseInfoTableQuery.filter(_.id === id)
      relations <- relationsTableQuery.filter(_.phraseId === id)
    } yield (phraseInfo, relations)).result

  def getByLocationId(locationId: Long): Multi[(PhraseInfo, PhraseLocationRelation)] =
    (for {
      relations <- relationsTableQuery.filter(_.locationId === locationId)
      phrases <- phraseInfoTableQuery.filter(_.id === relations.phraseId)
    } yield (phrases, relations)).result

  def getByDependencyGraphId(id: UUID): Multi[(PhraseInfo, PhraseLocationRelation)] = {
    (for {
      phrases <- phraseInfoTableQuery.filter(_.dependencyGraphId === id)
      relations <- relationsTableQuery.filter(_.dependencyGraphId === id)
    } yield (phrases, relations)).result
  }

  def removeById(id: Long)(implicit ec: ExecutionContext): DBIOAction[Int, NoStream, Effect.Write] =
    for {
      r <- phraseInfoTableQuery.filter(_.id === id).delete
      _ <- relationsTableQuery.filter(_.phraseId === id).delete
    } yield r

  def removeByDependencyGraphId(id: UUID)(implicit ec: ExecutionContext): DBIOAction[Int, NoStream, Effect.Write] =
    for {
      _ <- phraseInfoTableQuery.filter(_.dependencyGraphId === id).delete
      r <- relationsTableQuery.filter(_.dependencyGraphId === id).delete
    } yield r
}
