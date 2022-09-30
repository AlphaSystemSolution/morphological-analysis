package com.alphasystem.arabic.morphologicalanalysis.morphology.persistence.cache

import com.alphasystem.arabic.morphologicalanalysis.morphology.model.Token
import com.alphasystem.arabic.morphologicalanalysis.morphology.persistence.repository.TokenRepository
import com.github.blemale.scaffeine.{ LoadingCache, Scaffeine }

import scala.concurrent.duration.*

class TokenCache(repository: TokenRepository) {

  lazy val propertiesByParent: LoadingCache[TokenRequest, Seq[Token]] =
    Scaffeine()
      .recordStats()
      .expireAfterWrite(1.hour)
      .maximumSize(500)
      .build(request =>
        repository.findByChapterAndVerse(
          request.chapterNumber,
          request.verseNumber
        )
      )
}

object TokenCache {

  def apply(repository: TokenRepository): TokenCache =
    new TokenCache(repository)
}
