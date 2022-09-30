package com.alphasystem.arabic.morphologicalanalysis.morphology.persistence.cache

import com.alphasystem.arabic.morphologicalanalysis.morphology.model.WordProperties
import com.alphasystem.arabic.morphologicalanalysis.morphology.persistence.repository.WordPropertiesRepository
import com.github.blemale.scaffeine.{ LoadingCache, Scaffeine }

import scala.concurrent.duration.*

class PropertiesCache(repository: WordPropertiesRepository) {

  lazy val propertiesByParent
    : LoadingCache[PropertiesRequest, Seq[WordProperties]] =
    Scaffeine()
      .recordStats()
      .expireAfterWrite(1.hour)
      .maximumSize(500)
      .build(request =>
        repository.findByChapterVerseTokenAndLocation(
          request.chapterNumber,
          request.verseNumber,
          request.tokenNumber,
          request.locationNumber
        )
      )

}

object PropertiesCache {

  def apply(repository: WordPropertiesRepository) =
    new PropertiesCache(repository)
}
