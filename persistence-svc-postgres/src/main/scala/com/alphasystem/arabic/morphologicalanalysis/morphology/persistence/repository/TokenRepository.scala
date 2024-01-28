package com.alphasystem
package arabic
package morphologicalanalysis
package morphology
package persistence
package repository

import morphology.model.Token

import scala.concurrent.Future

trait TokenRepository {

  def createTokens(tokens: Seq[Token]): Future[Done]
  def updateToken(token: Token): Future[Done]
  def findTokenById(tokenId: Long): Future[Option[Token]]
  def findTokensByVerseId(verseId: Long): Future[Seq[Token]]
  def removeTokensByVerseId(verseId: Long): Future[Done]
}
