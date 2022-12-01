package com.alphasystem
package arabic
package morphologicalanalysis
package morphology
package persistence
package cache

import morphology.model.{ Token, Verse }
import com.github.blemale.scaffeine.{ LoadingCache, Scaffeine }

import scala.concurrent.duration.*

class CacheFactory(val database: Database) {

  lazy val verses: LoadingCache[Int, Seq[Verse]] =
    Scaffeine()
      .recordStats()
      .expireAfterAccess(6.hour)
      .maximumSize(1000)
      .build(chapterNumber => database.findVersesByChapterNumber(chapterNumber))

  lazy val tokens: LoadingCache[TokenRequest, Seq[Token]] =
    Scaffeine()
      .recordStats()
      .expireAfterAccess(6.hour)
      .maximumSize(1000)
      .build(request => database.findTokensByVerseId(request.verseId).sortBy(_.tokenNumber))
}

object CacheFactory {
  def apply(database: Database) = new CacheFactory(database)
}
