package com.alphasystem
package arabic
package morphologicalanalysis
package morphology

import model.*

package object model {

  type VerseId = (Int, Int)

  type TokenId = (Int, Int, Int)

  type LocationId = (Int, Int, Int, Int)

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

  implicit class IdOps(src: Int) {

    def toChapterId: String = s"chapter:$src"

    def toVerseId(chapterNumber: Int): String = s"verse:$chapterNumber:$src"

    def toTokenDisplayName(chapterNumber: Int, verseNumber: Int): String =
      s"$chapterNumber:$verseNumber:$src"

    def toTokenId(chapterNumber: Int, verseNumber: Int): String =
      s"token:${src.toTokenDisplayName(chapterNumber, verseNumber)}"

    def toLocationDisplayName(
      chapterNumber: Int,
      verseNumber: Int,
      tokenNumber: Int
    ): String = s"$chapterNumber:$verseNumber:$tokenNumber:$src"

    def toLocationId(
      chapterNumber: Int,
      verseNumber: Int,
      tokenNumber: Int
    ): String = s"location:${src.toLocationDisplayName(chapterNumber, verseNumber, tokenNumber)}"
  }

}
