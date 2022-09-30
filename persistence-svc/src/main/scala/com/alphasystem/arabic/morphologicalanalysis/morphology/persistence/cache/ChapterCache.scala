package com.alphasystem.arabic.morphologicalanalysis.morphology.persistence.cache

import com.alphasystem.arabic.morphologicalanalysis.morphology.model.Chapter
import com.alphasystem.arabic.morphologicalanalysis.morphology.persistence.repository.ChapterRepository
import com.github.blemale.scaffeine.{ LoadingCache, Scaffeine }

import scala.concurrent.duration.*

class ChapterCache(repository: ChapterRepository) {

  lazy val allChapters: LoadingCache[Int, Seq[Chapter]] =
    Scaffeine()
      .recordStats()
      .expireAfterWrite(1.hour)
      .maximumSize(200)
      .build(_ => repository.findAll)
}

object ChapterCache {

  def apply(repository: ChapterRepository): ChapterCache =
    new ChapterCache(repository)
}
