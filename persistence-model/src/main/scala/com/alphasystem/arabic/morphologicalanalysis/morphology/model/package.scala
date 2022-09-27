package com.alphasystem.arabic.morphologicalanalysis.morphology

package object model {

  implicit class IdOps(src: Int) {

    def toChapterId: String = s"chapter:$src"

    def toVerseId(chapterNumber: Int): String = s"verse:$chapterNumber:$src"

    def toTokenId(chapterNumber: Int, verseNumber: Int): String =
      s"token:$chapterNumber:$verseNumber:$src"

    def toLocationId(
      chapterNumber: Int,
      verseNumber: Int,
      tokenNumber: Int
    ): String =
      s"location:$chapterNumber:$verseNumber:$tokenNumber:$src"
  }

}
