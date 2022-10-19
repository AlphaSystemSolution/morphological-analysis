package com.alphasystem
package arabic
package morphologicalanalysis
package morphology

import com.alphasystem.arabic.morphologicalanalysis.morphology.model.*

package object model {

  val defaultNounProperties: NounProperties =
    NounProperties(
      partOfSpeech = NounPartOfSpeechType.NOUN,
      status = NounStatus.NOMINATIVE,
      number = NumberType.SINGULAR,
      gender = GenderType.MASCULINE,
      nounType = NounType.INDEFINITE,
      nounKind = NounKind.NONE
    )

  val defaultProNounProperties: ProNounProperties =
    ProNounProperties(
      partOfSpeech = ProNounPartOfSpeechType.PRONOUN,
      status = NounStatus.NOMINATIVE,
      number = NumberType.SINGULAR,
      gender = GenderType.MASCULINE,
      conversationType = ConversationType.THIRD_PERSON,
      proNounType = ProNounType.DETACHED
    )

  val defaultVerbProperties: VerbProperties =
    VerbProperties(
      partOfSpeech = VerbPartOfSpeechType.VERB,
      number = NumberType.SINGULAR,
      gender = GenderType.MASCULINE,
      conversationType = ConversationType.THIRD_PERSON,
      verbType = VerbType.PERFECT,
      mode = VerbMode.NONE
    )

  val defaultParticleProperties: ParticleProperties =
    ParticleProperties(
      partOfSpeech = ParticlePartOfSpeechType.DEFINITE_ARTICLE
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
