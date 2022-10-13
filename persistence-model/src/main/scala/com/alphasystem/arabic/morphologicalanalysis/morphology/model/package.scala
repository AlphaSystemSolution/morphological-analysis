package com.alphasystem.arabic.morphologicalanalysis.morphology

import com.alphasystem.morphologicalanalysis.morphology.model.{
  GenderType,
  NounKind,
  NounPartOfSpeechType,
  NounStatus,
  NounType,
  NumberType
}

package object model {

  val defaultProperties: NounProperties =
    NounProperties(
      partOfSpeech = NounPartOfSpeechType.NOUN,
      status = NounStatus.ACCUSATIVE,
      number = NumberType.SINGULAR,
      gender = GenderType.MASCULINE,
      nounType = NounType.INDEFINITE,
      nounKind = NounKind.NONE
    )

  implicit class IdOps(src: Int) {

    def toChapterId: String = s"chapter:$src"

    def toVerseId(chapterNumber: Int): String = s"verse:$chapterNumber:$src"

    def toTokenId(chapterNumber: Int, verseNumber: Int): String =
      s"token:$chapterNumber:$verseNumber:$src"

    def toLocationDisplayName(
      chapterNumber: Int,
      verseNumber: Int,
      tokenNumber: Int
    ): String =
      s"$chapterNumber:$verseNumber:$tokenNumber:$src"

    def toLocationId(
      chapterNumber: Int,
      verseNumber: Int,
      tokenNumber: Int
    ): String =
      s"location:${src.toLocationDisplayName(chapterNumber, verseNumber, tokenNumber)}"
  }

}
