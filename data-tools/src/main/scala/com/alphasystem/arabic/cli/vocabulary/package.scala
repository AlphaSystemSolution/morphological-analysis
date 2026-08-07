package com.alphasystem
package arabic
package cli
package vocabulary

import arabic.morphologicalengine.conjugation.model.NamedTemplate
import io.circe.{ Decoder, Encoder, HCursor, Json }
import io.circe.generic.auto.*
import java.nio.file.Path
import scala.util.Try

final case class Word(text: String, family: NamedTemplate, translation: String)

object Word {
  given ordering: Ordering[Word] = (x: Word, y: Word) => x.family.compareTo(y.family)
}

given Encoder[NamedTemplate] = Encoder.encodeString.contramap(_.toString)

given Decoder[NamedTemplate] = Decoder.decodeString.emap { value =>
  Try(NamedTemplate.getByAlias(value))
    .orElse(Try(NamedTemplate.valueOf(value)))
    .toEither
    .left
    .map(_ => s"Invalid template value: $value")
}

final case class WordList(root: String, words: Seq[Word])

final case class TranslationSearchResult(root: String, text: String, family: NamedTemplate, translation: String)

object WordList {
  given ordering: Ordering[WordList] = (x: WordList, y: WordList) => {
    val iteratorX = x.words.iterator
    val iteratorY = y.words.iterator
    var result = 0
    while iteratorX.hasNext && iteratorY.hasNext && result == 0 do {
      result = summon[Ordering[Word]].compare(iteratorX.next(), iteratorY.next())
    }
    if result != 0 then result else x.words.length.compareTo(y.words.length)
  }
}

private[cli] def toWordList(path: Path): WordList =
  fromFile(path, fromString[WordList])
