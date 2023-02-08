package com.alphasystem
package arabic
package morphologicalanalysis
package morphology

import arabic.model.{ VerbType as VT, * }
import model.*

package object model {

  lazy val defaultNounProperties: NounProperties =
    NounProperties(
      partOfSpeech = NounPartOfSpeechType.Noun,
      status = NounStatus.Nominative,
      number = NumberType.Singular,
      gender = GenderType.Masculine,
      nounType = NounType.Indefinite,
      nounKind = NounKind.None
    )

  lazy val defaultProNounProperties: ProNounProperties =
    ProNounProperties(
      partOfSpeech = ProNounPartOfSpeechType.Pronoun,
      status = NounStatus.Nominative,
      number = NumberType.Singular,
      gender = GenderType.Masculine,
      conversationType = ConversationType.ThirdPerson,
      proNounType = ProNounType.Detached
    )

  lazy val defaultVerbProperties: VerbProperties =
    VerbProperties(
      partOfSpeech = VerbPartOfSpeechType.Verb,
      number = NumberType.Singular,
      gender = GenderType.Masculine,
      conversationType = ConversationType.ThirdPerson,
      verbType = MorphologyVerbType.Imperfect,
      mode = VerbMode.None
    )

  lazy val defaultParticleProperties: ParticleProperties =
    ParticleProperties(
      partOfSpeech = ParticlePartOfSpeechType.DefiniteArticle
    )

  private val defaultTokenText = "(*)"

  lazy val defaultLocation: Location =
    Location(
      chapterNumber = 0,
      verseNumber = 0,
      tokenNumber = 0,
      locationNumber = 0,
      hidden = true,
      startIndex = 0,
      endIndex = defaultTokenText.length - 1,
      derivedText = defaultTokenText,
      text = defaultTokenText,
      alternateText = defaultTokenText,
      wordType = WordType.NOUN,
      properties = defaultNounProperties
    )

  private lazy val defaultPronounLocation: Location =
    defaultLocation.copy(
      endIndex = ProNoun.ThirdPersonMasculineSingular.label.length - 1,
      derivedText = ProNoun.ThirdPersonMasculineSingular.label,
      text = ProNoun.ThirdPersonMasculineSingular.label,
      wordType = WordType.PRO_NOUN,
      properties = defaultProNounProperties
    )

  private lazy val defaultVerbLocation: Location =
    defaultLocation.copy(wordType = WordType.VERB, properties = defaultVerbProperties)

  lazy val defaultToken: Token =
    Token(
      chapterNumber = 0,
      verseNumber = 0,
      tokenNumber = 0,
      token = defaultTokenText,
      hidden = true,
      locations = Seq(defaultLocation)
    )

  lazy val defaultPronounToken: Token =
    defaultToken.copy(token = ProNoun.ThirdPersonMasculineSingular.label, locations = Seq(defaultPronounLocation))

  lazy val defaultVerbToken: Token = defaultToken.copy(locations = Seq(defaultVerbLocation))

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
