package com.alphasystem
package arabic
package morphologicalanalysis
package morphology
package persistence
package repository

import com.alphasystem.arabic.morphologicalanalysis.morphology.graph.model.PhraseInfo

import java.util.UUID
import scala.concurrent.Future

trait PhraseInfoRepository {

  def createPhraseInfo(phraseInfo: PhraseInfo): Future[Done]
  def findById(id: UUID): Future[Option[PhraseInfo]]
  def findByDependencyGraphId(id: UUID): Future[Seq[PhraseInfo]]
}
