package com.alphasystem
package arabic
package morphologicalanalysis
package morphology

import model.*

package object model {

  val defaultNounProperties: NounProperties =
    NounProperties(
      partOfSpeech = NounPartOfSpeechType.Noun,
      status = NounStatus.Nominative,
      number = NumberType.Singular,
      gender = GenderType.Masculine,
      nounType = NounType.Indefinite,
      nounKind = NounKind.None
    )

  val defaultProNounProperties: ProNounProperties =
    ProNounProperties(
      partOfSpeech = ProNounPartOfSpeechType.Pronoun,
      status = NounStatus.Nominative,
      number = NumberType.Singular,
      gender = GenderType.Masculine,
      conversationType = ConversationType.ThirdPerson,
      proNounType = ProNounType.Detached
    )

  val defaultVerbProperties: VerbProperties =
    VerbProperties(
      partOfSpeech = VerbPartOfSpeechType.Verb,
      number = NumberType.Singular,
      gender = GenderType.Masculine,
      conversationType = ConversationType.ThirdPerson,
      verbType = VerbType.Perfect,
      mode = VerbMode.None
    )

  val defaultParticleProperties: ParticleProperties =
    ParticleProperties(
      partOfSpeech = ParticlePartOfSpeechType.DefiniteArticle
    )

  extension (src: Int) {
    def appendZeros: String = "%04d".format(src)

    def toVerseId(chapterNumber: Int): Long = (chapterNumber * 100_000) + src

    def toTokenDisplayName(chapterNumber: Int, verseNumber: Int): String =
      s"$chapterNumber:$verseNumber:$src"

    def toTokenId(chapterNumber: Int, verseNumber: Int): Long =
      (verseNumber.toVerseId(chapterNumber).toString + src.appendZeros).toLong

    def toLocationDisplayName(
      chapterNumber: Int,
      verseNumber: Int,
      tokenNumber: Int
    ): String = s"$chapterNumber:$verseNumber:$tokenNumber:$src"

    def toLocationId(
      chapterNumber: Int,
      verseNumber: Int,
      tokenNumber: Int
    ): Long = (tokenNumber.toTokenId(chapterNumber, verseNumber).toString + src.appendZeros).toLong
  }

}
