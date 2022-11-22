package com.alphasystem
package arabic
package morphologicalanalysis
package morphology
package persistence

import morphology.persistence.model.{
  Chapter as ChapterLifted,
  Location as LocationLifted,
  Token as TokenLifted,
  Verse as VerseLifted
}
import morphology.model.{ Chapter, Location, NamedTag, Token, Verse, WordProperties, WordType }
import io.circe.generic.auto.*
import io.circe.parser.*
import io.circe.syntax.*

package object repository {

  extension (src: Chapter) {
    def toLifted: ChapterLifted =
      ChapterLifted(chapter_number = src.chapterNumber, chapter_name = src.chapterName, verse_count = src.verseCount)
  }

  extension (src: ChapterLifted) {
    def toEntity: Chapter =
      Chapter(chapterName = src.chapter_name, chapterNumber = src.chapter_number, verseCount = src.verse_count)
  }

  extension (src: Verse) {
    def toLifted: VerseLifted =
      VerseLifted(
        chapter_number = src.chapterNumber,
        verse_number = src.verseNumber,
        verse_text = src.text,
        translation = src.translation
      )
  }

  extension (src: VerseLifted) {
    def toEntity: Verse =
      Verse(
        chapterNumber = src.chapter_number,
        verseNumber = src.verse_number,
        text = src.verse_text,
        tokenCount = 0,
        translation = src.translation
      )
  }

  extension (src: TokenLifted) {
    def toEntity: Token =
      Token(
        chapterNumber = src.chapter_number,
        verseNumber = src.verse_number,
        tokenNumber = src.token_number,
        token = src.token,
        translation = src.translation
      )
  }

  extension (src: Token) {
    def toLifted: TokenLifted =
      TokenLifted(
        chapter_number = src.chapterNumber,
        verse_number = src.verseNumber,
        token_number = src.tokenNumber,
        token = src.token,
        derived_text = src.token,
        translation = src.translation
      )
  }

  extension (src: LocationLifted) {
    def toEntity: Location =
      Location(
        chapterNumber = src.chapter_number,
        verseNumber = src.verse_number,
        tokenNumber = src.token_number,
        locationNumber = src.location_number,
        hidden = src.hidden,
        startIndex = src.start_index,
        endIndex = src.end_index,
        derivedText = src.derived_text,
        text = src.location_text,
        alternateText = src.alternate_text,
        wordType = WordType.valueOf(src.word_type),
        properties = decode[WordProperties](src.properties) match {
          case Left(ex)     => throw ex
          case Right(value) => value
        },
        translation = src.translation,
        namedTag = src.named_tag.map(NamedTag.valueOf)
      )
  }

  extension (src: Location) {
    def toLifted: LocationLifted =
      LocationLifted(
        chapter_number = src.chapterNumber,
        verse_number = src.verseNumber,
        token_number = src.locationNumber,
        location_number = src.locationNumber,
        hidden = src.hidden,
        start_index = src.startIndex,
        end_index = src.endIndex,
        derived_text = src.derivedText,
        location_text = src.text,
        alternate_text = src.alternateText,
        word_type = src.wordType.name(),
        properties = src.properties.asJson.noSpaces,
        translation = src.translation,
        named_tag = src.namedTag.map(_.name())
      )
  }

}
