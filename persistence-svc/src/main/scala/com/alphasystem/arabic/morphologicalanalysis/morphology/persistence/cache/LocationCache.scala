package com.alphasystem.arabic.morphologicalanalysis.morphology.persistence.cache

import com.alphasystem.arabic.morphologicalanalysis.morphology.model.Location
import com.alphasystem.arabic.morphologicalanalysis.morphology.persistence.repository.LocationRepository
import com.github.blemale.scaffeine.{ LoadingCache, Scaffeine }

import scala.concurrent.duration.*

class LocationCache(repository: LocationRepository) {

  lazy val propertiesByParent: LoadingCache[LocationRequest, Seq[Location]] =
    Scaffeine()
      .recordStats()
      .expireAfterWrite(1.hour)
      .maximumSize(500)
      .build(request =>
        repository.findByChapterVerseAndToken(
          request.chapterNumber,
          request.verseNumber,
          request.tokenNumber
        )
      )
}

object LocationCache {

  def apply(repository: LocationRepository): LocationCache =
    new LocationCache(repository)
}
