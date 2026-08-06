package com.alphasystem
package arabic
package vocabulary
package ui

import morphologicalengine.conjugation.model.NamedTemplate
import io.circe.Decoder
import io.circe.generic.auto.*
import io.circe.yaml.v12.parser

import java.nio.file.Path
import scala.io.Source
import scala.util.{ Failure, Success, Using }

package object storage {

  final case class Word(text: String, family: NamedTemplate, translation: String)

  object Word {
    given ordering: Ordering[Word] = (x: Word, y: Word) => x.family.compareTo(y.family)
  }

  final case class WordList(root: String, words: Seq[Word])

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

  private[ui] def toWordList(path: Path): WordList =
    fromFile(path, fromString[WordList])

  private[storage] def fromFile[T](path: Path, toDataType: String => T)(using dec: Decoder[T]): T =
    Using(Source.fromFile(path.toFile))(source => toDataType(source.mkString)) match
      case Failure(ex)    => throw ex
      case Success(value) => value

  private[storage] def fromString[T](ymlString: String)(using dec: Decoder[T]): T =
    parser.parse(ymlString) match {
      case Left(ex) => throw ex
      case Right(value) =>
        value.as[T] match {
          case Left(ex)     => throw ex
          case Right(value) => value
        }
    }
}
